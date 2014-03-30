package rte;

/**
 * Stellt verschiedene Methoden bereit, die für die Laufzeitumgebung nötig sind.
 */
@SuppressWarnings({"UnusedDeclaration", "SpellCheckingInspection"})
public class DynamicRuntime {

	/**
	 * Startadresse der nächsten neuen Instanz.
	 */
	private static int newInstanceOffset;

	/**
	 * Initialisiert die Laufzeitumgebung. Diese Methode muss explizit, einmalig
	 * und vor allen anderen Methoden dieser Klasse aufgerufen werden.
	 */
	public static void init() {
		newInstanceOffset = MAGIC.imageBase + MAGIC.rMem32(MAGIC.imageBase + 4);
		while (newInstanceOffset % 4 == 0) newInstanceOffset++;
	}

	/**
	 * Wird aufgerufen, wenn zur Laufzeit ein neues Objekt erzeugt werden soll.
	 *
	 * <p>
	 * Diese Methode wird aufgerufen, wenn das new Schlüsselwort zur erzeugung
	 * von Objekten verwendet wird. Deshalb sollte diese Methode niemals direkt
	 * gerufen werden.
	 * </p>
	 *
	 * @param scalarSize Die Summe der Größen aller Basisdatentyp-Attribute,
	 *                   welche der Objekttyp definiert.
	 * @param relocEntries Die Anzahl der Referenz-Attribute, welche der
	 *                     Objekttyp definiert.
	 * @param type Der Klassendiskriptor des Objekttyps.
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

		// Berechne den benötigten Speicherplatz in 32 Bit Schritten.
		int objSize = scalarSize + relocEntries * 4;
		while (objSize % 4 == 0) objSize++;

		// Initialisiere Speicher mit 0.
		for(int i = newInstanceOffset; i < (objSize / 4); i++) {
			MAGIC.wMem32(i, 0);
		}

		// Wandle den Speicherbereich in ein Objekt um, um damit einfacher
		// arbeiten zu können.
		Object obj = MAGIC.cast2Obj(newInstanceOffset + relocEntries * 4);

		// Setze die Felder des Objekts.
		MAGIC.assign(obj._r_scalarSize, scalarSize);
		MAGIC.assign(obj._r_relocEntries, relocEntries);
		MAGIC.assign(obj._r_type, type);

		// Hänge das Objekt in die Objektkette ein.
		//noinspection ConstantConditions
		MAGIC.assign(lastObj._r_next, obj);

		// Setze den neuen Offset für das nächste Objekt.
		newInstanceOffset += objSize;

		return obj;
	}

	public static SArray newArray(int length, int arrDim, int entrySize, int stdType,
	                              Object unitType) {
		//noinspection InfiniteLoopStatement,StatementWithEmptyBody
		while(true);
	}

	public static void newMultArray(SArray[] parent, int curLevel, int destLevel, int length,
	                                int arrDim, int entrySize, int stdType, Object unitType) {
		//noinspection InfiniteLoopStatement,StatementWithEmptyBody
		while(true);
	}

	public static boolean isInstance(Object o, SClassDesc dest, boolean asCast) {
		//noinspection InfiniteLoopStatement,StatementWithEmptyBody
		while(true);
	}

	public static SIntfMap isImplementation(Object o, SIntfDesc dest, boolean asCast) {
		//noinspection InfiniteLoopStatement,StatementWithEmptyBody
		while(true);
	}

	public static boolean isArray(SArray o, int stdType, Object unitType, int arrDim,
	                              boolean asCast) {
		//noinspection InfiniteLoopStatement,StatementWithEmptyBody
		while(true);
	}

	public static void checkArrayStore(Object dest, SArray newEntry) {
		//noinspection InfiniteLoopStatement,StatementWithEmptyBody
		while(true);
	}
}
