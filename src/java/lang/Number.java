package java.lang;

/**
 * Die abstrakte Klasse {@link Number} ist die Elternklasse der Klassen
 * {@link Byte}, {@link Integer}, {@link Long} und {@link Short}.
 * <p>
 * Kindklassen von {@link Number} müssen Methoden bereitstellen, welche den
 * repräsentierten numerischen Wert zu {@link byte}, {@link double},
 * {@link float}, {@link int}, {@link long} und {@link short}.
 * </p>
 */
public abstract class Number {

	/**
	 * Liefert den Wert der spezifizierten Nummer als {@link byte}. Dies kann
	 * Rundung und Abschneiden beinhalten.
	 *
	 * @return Der numerische Wert, welcher von diesem Objekt repräsentiert
	 * wird, nachdem er in den Typ {@link byte} konvertiert wurde.
	 */
	public byte byteValue() {
		return (byte)this.intValue();
	}

	/**
	 * Liefert den Wert der spezifizierten Nummer als {@link double}. Dies kann
	 * Rundung und Abschneiden beinhalten.
	 *
	 * @return Der numerische Wert, welcher von diesem Objekt repräsentiert
	 * wird, nachdem er in den Typ {@link double} konvertiert wurde.
	 */
	public abstract double doubleValue();

	/**
	 * Liefert den Wert der spezifizierten Nummer als {@link float}. Dies kann
	 * Rundung und Abschneiden beinhalten.
	 *
	 * @return Der numerische Wert, welcher von diesem Objekt repräsentiert
	 * wird, nachdem er in den Typ {@link float} konvertiert wurde.
	 */
	public abstract float floatValue();

	/**
	 * Liefert den Wert der spezifizierten Nummer als {@link int}. Dies kann
	 * Rundung und Abschneiden beinhalten.
	 *
	 * @return Der numerische Wert, welcher von diesem Objekt repräsentiert
	 * wird, nachdem er in den Typ {@link int} konvertiert wurde.
	 */
	public abstract int intValue();

	/**
	 * Liefert den Wert der spezifizierten Nummer als {@link long}. Dies kann
	 * Rundung und Abschneiden beinhalten.
	 *
	 * @return Der numerische Wert, welcher von diesem Objekt repräsentiert
	 * wird, nachdem er in den Typ {@link long} konvertiert wurde.
	 */
	public abstract long longValue();

	/**
	 * Liefert den Wert der spezifizierten Nummer als {@link short}. Dies kann
	 * Rundung und Abschneiden beinhalten.
	 *
	 * @return Der numerische Wert, welcher von diesem Objekt repräsentiert
	 * wird, nachdem er in den Typ {@link short} konvertiert wurde.
	 */
	public short shortValue() {
		return (short)this.intValue();
	}
}
