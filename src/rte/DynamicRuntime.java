package rte;

import memory.Memory;

/**
 * Stellt verschiedene Methoden bereit, die für die Laufzeitumgebung nötig sind.
 */
@SuppressWarnings({"UnusedDeclaration", "SpellCheckingInspection", "JavaDoc"}) // TODO: JavaDoc
public class DynamicRuntime {

	static Object firstDynamicObject = null;

	/**
	 * Wird aufgerufen, wenn zur Laufzeit ein neues Objekt erzeugt werden soll.
	 * <p>
	 * Diese Methode wird aufgerufen, wenn das new Schlüsselwort zur Erzeugung
	 * von Objekten verwendet wird. Deshalb sollte diese Methode niemals direkt
	 * gerufen werden.
	 * </p>
	 *
	 * @param scalarSize   Die Summe der Größen aller Basisdatentyp-Attribute,
	 *                     welche der Objekttyp definiert.
	 * @param relocEntries Die Anzahl der Referenz-Attribute, welche der
	 *                     Objekttyp definiert.
	 * @param type         Der Klassendiskriptor des Objekttyps.
	 * @return Eine neues Objekt.
	 */
	public static Object newInstance(int scalarSize, int relocEntries, SClassDesc type) {
		// ACHTUNG: Innerhalb dieses Methodenblocks und der darin aufgerufenen
		// Methoden darf das new Schlüsselwort nicht verwendet werden.

		// Ermittle letztes Objekt.
		Object lastObj = type;
		while (lastObj._r_next != null) {
			lastObj = lastObj._r_next;
		}

		// Reserviere Speicher
		Object obj = Memory.allocate(relocEntries, scalarSize);
		if(firstDynamicObject == null) {
			firstDynamicObject = obj;
		}

		// Setze die Felder des Objekts.
		MAGIC.assign(obj._r_scalarSize, scalarSize);
		MAGIC.assign(obj._r_relocEntries, relocEntries);
		MAGIC.assign(obj._r_type, type);

		// Hänge das Objekt in die Objektkette ein.
		//noinspection ConstantConditions
		MAGIC.assign(lastObj._r_next, obj);

		return obj;
	}

	/**
	 * Wird aufgerufen, wenn zur Laufzeit ein neues Array erzeugt werden soll.
	 * <p>
	 * Diese Methode wird aufgerufen, wenn das new Schlüsselwort zur Erzeugung
	 * von Arrays verwendet wird. Deshalb sollte diese Methode niemals direkt
	 * gerufen werden.
	 * </p>
	 *
	 * @param length
	 * @param arrDim
	 * @param entrySize
	 * @param stdType
	 * @param unitType
	 * @return Eine neues Array.
	 */
	public static SArray newArray(int length, int arrDim, int entrySize, int stdType,
	                              Object unitType) {
		int scS, rlE;
		SArray me;

		if (stdType == 0 && unitType._r_type != MAGIC.clssDesc("SClassDesc"))
			MAGIC.inline(0xCC); //check type of unitType, we don't support interface arrays
		scS = MAGIC.getInstScalarSize("SArray");
		rlE = MAGIC.getInstRelocEntries("SArray");
		if (arrDim > 1 || entrySize < 0) rlE += length;
		else scS += length * entrySize;
		me = (SArray)newInstance(scS, rlE, (SClassDesc)MAGIC.clssDesc("SArray"));
		MAGIC.assign(me.length, length);
		MAGIC.assign(me._r_dim, arrDim);
		MAGIC.assign(me._r_stdType, stdType);
		MAGIC.assign(me._r_unitType, unitType);
		return me;
	}

	/**
	 * Wird aufgerufen, wenn zur Laufzeit ein neues mehrdimensionales Array
	 * erzeugt werden soll.
	 * <p>
	 * Diese Methode wird aufgerufen, wenn das new Schlüsselwort zur Erzeugung
	 * von mehrdimensionalen Arrays verwendet wird. Deshalb sollte diese Methode
	 * niemals direkt gerufen werden.
	 * </p>
	 *
	 * @param length
	 * @param arrDim
	 * @param entrySize
	 * @param stdType
	 * @param clssType
	 */
	public static void newMultArray(SArray[] parent, int curLevel, int destLevel, int length,
	                                int arrDim, int entrySize, int stdType, SClassDesc clssType) {
		int i;

		if (curLevel + 1 < destLevel) { //step down one level
			curLevel++;
			for (i = 0; i < parent.length; i++) {
				newMultArray((SArray[])((Object)parent[i]), curLevel, destLevel,
						length, arrDim, entrySize, stdType, clssType);
			}
		} else { //create the new entries
			destLevel = arrDim - curLevel;
			for (i = 0; i < parent.length; i++) {
				parent[i] = newArray(length, destLevel, entrySize, stdType, clssType);
			}
		}
	}

	/**
	 * Wird aufgerufen, wenn zur Laufzeit die Objekt-Klassen-Hierarchie geprüft
	 * wird.
	 * <p>
	 * Diese Methode wird aufgerufen, wenn das instanceof Schlüsselwort zur
	 * Prüfung der Objekt-Klassen-Hierarchie verwendet wird. Deshalb sollte
	 * diese Methode niemals direkt gerufen werden.
	 * </p>
	 *
	 * @param o
	 * @param dest
	 * @param asCast
	 * @return true falls der Typ des Objekts von der Klasse abgeleitet ist,
	 * sonst false.
	 */
	public static boolean isInstance(Object o, Object dest, boolean asCast) {
		SClassDesc check;

		if (o == null) {
			return asCast; // true: null matches all; false: null is not an instance
		}
		check = o._r_type;
		while (check != null) {
			if (check == dest) return true;
			check = check.parent;
		}
		if (asCast) MAGIC.inline(0xCC);
		return false;
	}

	/**
	 * Wird aufgerufen, wenn zur Laufzeit die Implementierung eines Interfaces
	 * geprüft wird.
	 * <p>
	 * Diese Methode wird aufgerufen, wenn das instanceof Schlüsselwort zur
	 * Prüfung auf Interfaceimplementierungen genutzt wird. Deshalb sollte diese
	 * Methode niemals direkt gerufen werden.
	 * </p>
	 *
	 * @param o
	 * @param dest
	 * @param asCast
	 * @return true falls der Typ des Objekts das Interface implementiert, sonst
	 * false.
	 */
	public static SIntfMap isImplementation(Object o, SIntfDesc dest, boolean asCast) {
		SIntfMap check;

		if (o == null) return null;
		check = o._r_type.implementations;
		while (check != null) {
			if (check.owner == dest) return check;
			check = check.next;
		}
		if (asCast) MAGIC.inline(0xCC);
		return null;
	}

	/**
	 * Wird aufgerufen, wenn zur Laufzeit den Typ eines Arrays.
	 * <p>
	 * Diese Methode wird aufgerufen, wenn das instanceof Schlüsselwort zur
	 * Prüfung auf Arraytypen genutzt wird. Deshalb sollte diese Methode niemals
	 * direkt gerufen werden.
	 * </p>
	 *
	 * @param o
	 * @param stdType
	 * @param clssType
	 * @param arrDim
	 * @param asCast
	 * @return true falls das Array vom gesuchten Typ ist, sonst false.
	 */
	public static boolean isArray(SArray o, int stdType, Object clssType, int arrDim,
	                              boolean asCast) {
		SClassDesc clss;

		//in fact o is of type "Object", _r_type has to be checked below - but this check is faster than "instanceof" and conversion
		if (o == null) {
			return asCast; // true: null matches all; false: null is not an instance
		}
		if (o._r_type != MAGIC.clssDesc("SArray")) { //will never match independently of arrDim
			if (asCast) MAGIC.inline(0xCC);
			return false;
		}
		if (clssType == MAGIC.clssDesc("SArray")) { //special test for arrays
			if (o._r_unitType == MAGIC.clssDesc("SArray"))
				arrDim--; //an array of SArrays, make next test to ">=" instead of ">"
			if (o._r_dim > arrDim)
				return true; //at least one level has to be left to have an object of type SArray
			if (asCast) MAGIC.inline(0xCC);
			return false;
		}
		//no specials, check arrDim and check for standard type
		if (o._r_stdType != stdType || o._r_dim < arrDim) { //check standard types and array dimension
			if (asCast) MAGIC.inline(0xCC);
			return false;
		}
		//noinspection ConstantConditions
		if (stdType != 0) {
			if (o._r_dim == arrDim) return true; //array of standard-type matching
			if (asCast) MAGIC.inline(0xCC);
			return false;
		}
		//array of objects, make deep-check for class type (PicOS does not support interface arrays)
		if (o._r_unitType._r_type != MAGIC.clssDesc("SClassDesc")) MAGIC.inline(0xCC);
		clss = (SClassDesc)o._r_unitType;
		while (clss != null) {
			if (clss == clssType) return true;
			clss = clss.parent;
		}
		if (asCast) MAGIC.inline(0xCC);
		return false;
	}

	/**
	 * Prüft beim Speichern von Elementen in einem Array, ob die Array- und
	 * Elementtypen zueinander passen.
	 *
	 * @param dest
	 * @param newEntry
	 */
	public static void checkArrayStore(SArray dest, SArray newEntry) {
		if (dest._r_dim > 1)
			isArray(newEntry, dest._r_stdType, dest._r_unitType, dest._r_dim - 1, true);
		else if (dest._r_unitType == null) MAGIC.inline(0xCC);
		else isInstance(newEntry, dest._r_unitType, true);
	}
}
