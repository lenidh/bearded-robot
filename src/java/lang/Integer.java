package java.lang;

/**
 * Die {@link Integer}-Klasse kapselt einen Wert mit dem
 * Standarddatentyp {@link int}. Ein Objekt vom Typ {@link java.lang.Integer}
 * enthält ein einzelnes Feld vom Typ {@link int}.
 */
public class Integer extends Number {

	/**
	 * Eine Konstante welche den maximalen Wert angibt, der einem {@link int}
	 * zugewiesen werden kann. (2³¹-1)
	 */
	public static final int MAX_VALUE = 2147483647;

	/**
	 * Eine Konstante welche den minimalen Wert angibt, der einem {@link int}
	 * zugewiesen werden kann. (-2³¹)
	 */
	public static final int MIN_VALUE = -2147483648;

	/**
	 * Die Anzahl der Bits, welche verwendet werden, um einen {@link int}-Wert
	 * im Zweierkomplementbinärformat darzustellen.
	 */
	public static final int SIZE = 32;

	/**
	 * Der {@link int}-Wert, den dieses Objekt repräsentiert.
	 */
	private int value;

	/**
	 * Erstellt eine neue Instanz der Klasse {@link Integer}, welche
	 * den spezifizierten {@link int}-Wert repräsentiert.
	 *
	 * @param value Der Wert, der von dem {@link Integer}-Objekt
	 *              repräsentiert werden soll.
	 */
	public Integer(int value) {
		this.value = value;
	}

	/**
	 * Liefert den Wert dieses {@link Integer} als {@link double}.
	 *
	 * @return Der numerische Wert, welcher von diesem Objekt repräsentiert
	 * wird, nachdem er in den Typ {@link double} konvertiert wurde.
	 */
	@Override
	public double doubleValue() {
		return (double)this.value;
	}

	/**
	 * Liefert den Wert dieses {@link Integer} als {@link float}.
	 *
	 * @return Der numerische Wert, welcher von diesem Objekt repräsentiert
	 * wird, nachdem er in den Typ {@link float} konvertiert wurde.
	 */
	@Override
	public float floatValue() {
		return (float)this.value;
	}

	/**
	 * Liefert den Wert dieses {@link Integer} als {@link int}.
	 *
	 * @return Der numerische Wert, welcher von diesem Objekt repräsentiert
	 * wird, nachdem er in den Typ {@link int} konvertiert wurde.
	 */
	@Override
	public int intValue() {
		return this.value;
	}

	/**
	 * Liefert den Wert dieses {@link Integer} als {@link long}.
	 *
	 * @return Der numerische Wert, welcher von diesem Objekt repräsentiert
	 * wird, nachdem er in den Typ {@link long} konvertiert wurde.
	 */
	@Override
	public long longValue() {
		return (long)this.value;
	}
}
