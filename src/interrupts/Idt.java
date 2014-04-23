package interrupts;

/**
 * Struktur zum Zugriff auf die IDT des Protected Mode.
 */
class Idt extends STRUCT {

	/**
	 * Anzahl der Tabelleneinträge
	 */
	public static final int SIZE = 48;

	/**
	 * Tabelleneinträge
	 *
	 * @see interrupts.IdtEntry
	 */
	@SJC(count = SIZE)
	public IdtEntry[] entries;
}
