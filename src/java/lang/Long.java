package java.lang;

/**
 * Die {@link Long}-Klasse kapselt einen Wert mit dem
 * Standarddatentyp {@link long}. Ein Objekt vom Typ {@link Long}
 * enthält ein einzelnes Feld vom Typ {@link long}.
 */
public class Long extends Number {

	/**
	 * Eine Konstante welche den maximalen Wert angibt, der einem {@link long}
	 * zugewiesen werden kann. (2⁶³-1)
	 */
	public static final long MAX_VALUE = 9223372036854775807L;

	/**
	 * Eine Konstante welche den minimalen Wert angibt, der einem {@link long}
	 * zugewiesen werden kann. (-2⁶³)
	 */
	public static final long MIN_VALUE = -9223372036854775808L;

	/**
	 * Die Anzahl der Bits, welche verwendet werden, um einen {@link long}-Wert
	 * im Zweierkomplementbinärformat darzustellen.
	 */
	public static final int SIZE = 64;

	/**
	 * Der {@link long}-Wert, den dieses Objekt repräsentiert.
	 */
	private long value;

	/**
	 * Erstellt eine neue Instanz der Klasse {@link Long}, welche
	 * den spezifizierten {@link long}-Wert repräsentiert.
	 *
	 * @param value Der Wert, der von dem {@link Long}-Objekt
	 *              repräsentiert werden soll.
	 */
	public Long(long value) {
		this.value = value;
	}

	/**
	 * Liefert den Wert dieses {@link Long} als {@link double}.
	 *
	 * @return Der numerische Wert, welcher von diesem Objekt repräsentiert
	 * wird, nachdem er in den Typ {@link double} konvertiert wurde.
	 */
	@Override
	public double doubleValue() {
		return (double)this.value;
	}

	/**
	 * Liefert den Wert dieses {@link Long} als {@link float}.
	 *
	 * @return Der numerische Wert, welcher von diesem Objekt repräsentiert
	 * wird, nachdem er in den Typ {@link float} konvertiert wurde.
	 */
	@Override
	public float floatValue() {
		return (float)this.value;
	}

	/**
	 * Liefert den Wert dieses {@link Long} als {@link int}.
	 *
	 * @return Der numerische Wert, welcher von diesem Objekt repräsentiert
	 * wird, nachdem er in den Typ {@link int} konvertiert wurde.
	 */
	@Override
	public int intValue() {
		return (int)this.value;
	}

	/**
	 * Liefert den Wert dieses {@link Long} als {@link long}.
	 *
	 * @return Der numerische Wert, welcher von diesem Objekt repräsentiert
	 * wird, nachdem er in den Typ {@link long} konvertiert wurde.
	 */
	@Override
	public long longValue() {
		return this.value;
	}
}
