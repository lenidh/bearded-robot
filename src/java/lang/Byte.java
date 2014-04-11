package java.lang;

/**
 * Die {@link Byte}-Klasse kapselt einen Wert mit dem
 * Standarddatentyp {@link int}. Ein Objekt vom Typ {@link Byte}
 * enthält ein einzelnes Feld vom Typ {@link byte}.
 */
public class Byte extends Number {

	/**
	 * Eine Konstante welche den maximalen Wert angibt, der einem {@link byte}
	 * zugewiesen werden kann. (2⁷)
	 */
	public static final byte MAX_VALUE = 127;

	/**
	 * Eine Konstante welche den minimalen Wert angibt, der einem {@link byte}
	 * zugewiesen werden kann. (-2⁷)
	 */
	public static final byte MIN_VALUE = -128;

	/**
	 * Die Anzahl der Bits, welche verwendet werden, um einen {@link byte}-Wert
	 * im Zweierkomplementbinärformat darzustellen.
	 */
	public static final int SIZE = 8;

	/**
	 * Der {@link int}-Wert, den dieses Objekt repräsentiert.
	 */
	private byte value;

	/**
	 * Erstellt eine neue Instanz der Klasse {@link Byte}, welche
	 * den spezifizierten {@link byte}-Wert repräsentiert.
	 *
	 * @param value Der Wert, der von dem {@link Byte}-Objekt
	 *              repräsentiert werden soll.
	 */
	public Byte(byte value) {
		this.value = value;
	}

	/**
	 * Liefert den Wert dieses {@link Byte} als {@link double}.
	 *
	 * @return Der numerische Wert, welcher von diesem Objekt repräsentiert
	 * wird, nachdem er in den Typ {@link double} konvertiert wurde.
	 */
	@Override
	public double doubleValue() {
		return (double)this.value;
	}

	/**
	 * Liefert den Wert dieses {@link Byte} als {@link float}.
	 *
	 * @return Der numerische Wert, welcher von diesem Objekt repräsentiert
	 * wird, nachdem er in den Typ {@link float} konvertiert wurde.
	 */
	@Override
	public float floatValue() {
		return (float)this.value;
	}

	/**
	 * Liefert den Wert dieses {@link Byte} als {@link int}.
	 *
	 * @return Der numerische Wert, welcher von diesem Objekt repräsentiert
	 * wird, nachdem er in den Typ {@link int} konvertiert wurde.
	 */
	@Override
	public int intValue() {
		return (int)this.value;
	}

	/**
	 * Liefert den Wert dieses {@link Byte} als {@link long}.
	 *
	 * @return Der numerische Wert, welcher von diesem Objekt repräsentiert
	 * wird, nachdem er in den Typ {@link long} konvertiert wurde.
	 */
	@Override
	public long longValue() {
		return (long)this.value;
	}
}
