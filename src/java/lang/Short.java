package java.lang;

/**
 * Die {@link Short}-Klasse kapselt einen Wert mit dem
 * Standarddatentyp {@link short}. Ein Objekt vom Typ {@link Short}
 * enthält ein einzelnes Feld vom Typ {@link short}.
 */
public class Short extends Number {

	/**
	 * Eine Konstante welche den maximalen Wert angibt, der einem {@link short}
	 * zugewiesen werden kann. (2¹⁶-1)
	 */
	public static final short MAX_VALUE = 32767;

	/**
	 * Eine Konstante welche den minimalen Wert angibt, der einem {@link short}
	 * zugewiesen werden kann. (-2¹⁶)
	 */
	public static final short MIN_VALUE = -32768;

	/**
	 * Die Anzahl der Bits, welche verwendet werden, um einen {@link short}-Wert
	 * im Zweierkomplementbinärformat darzustellen.
	 */
	public static final int SIZE = 16;

	/**
	 * Der {@link short}-Wert, den dieses Objekt repräsentiert.
	 */
	private short value;

	/**
	 * Erstellt eine neue Instanz der Klasse {@link Short}, welche
	 * den spezifizierten {@link short}-Wert repräsentiert.
	 *
	 * @param value Der Wert, der von dem {@link Short}-Objekt
	 *              repräsentiert werden soll.
	 */
	public Short(short value) {
		this.value = value;
	}

	/**
	 * Liefert den Wert dieses {@link Short} als {@link double}.
	 *
	 * @return Der numerische Wert, welcher von diesem Objekt repräsentiert
	 * wird, nachdem er in den Typ {@link double} konvertiert wurde.
	 */
	@Override
	public double doubleValue() {
		return (double)this.value;
	}

	/**
	 * Liefert den Wert dieses {@link Short} als {@link float}.
	 *
	 * @return Der numerische Wert, welcher von diesem Objekt repräsentiert
	 * wird, nachdem er in den Typ {@link float} konvertiert wurde.
	 */
	@Override
	public float floatValue() {
		return (float)this.value;
	}

	/**
	 * Liefert den Wert dieses {@link Short} als {@link int}.
	 *
	 * @return Der numerische Wert, welcher von diesem Objekt repräsentiert
	 * wird, nachdem er in den Typ {@link int} konvertiert wurde.
	 */
	@Override
	public int intValue() {
		return (int)this.value;
	}

	/**
	 * Liefert den Wert dieses {@link Short} als {@link long}.
	 *
	 * @return Der numerische Wert, welcher von diesem Objekt repräsentiert
	 * wird, nachdem er in den Typ {@link long} konvertiert wurde.
	 */
	@Override
	public long longValue() {
		return (long)this.value;
	}
}
