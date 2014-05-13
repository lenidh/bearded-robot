package java.lang;

import rte.SClassDesc;

/**
 * Die Klasse {@link java.lang.Object} ist die Wurzel der Objekthirachie. Jede
 * Klasse hat {@link java.lang.Object} als Elternklasse. Alle Objekte, Arrays
 * eingeschlossen, implementieren die Methoden dieser Klasse.
 */
@SuppressWarnings("all")
public class Object {

	/**
	 * Der {@link rte.SClassDesc} des Objekttyps.
	 */
	public final SClassDesc _r_type = null;

	/**
	 * Das nächste {@link java.lang.Object} in der Objektspeicherkette.
	 */
	public final Object _r_next = null;

	/**
	 * Die Anzahl der Referenz-Attribute, welche der Objekttyp definiert.
	 */
	public final int _r_relocEntries = 0;

	/**
	 * Die Summe der Größen aller Basisdatentyp-Attribute, welche der Objekttyp
	 * definiert.
	 */
	public final int _r_scalarSize = 0;

	/**
	 * Gibt an, ob ein Objekt markiert ist. 0 bedeutet "nicht markiert" alles
	 * andere "markiert".
	 */
	public final int _selected = 0;

	public String toString() {
		return this._r_type.name;
	}
}
