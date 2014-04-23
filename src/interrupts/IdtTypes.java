package interrupts;

/**
 * Konstanten zur Unterscheidung unterschiedlicher IDT-Typen.
 */
public class IdtTypes {

	/**
	 * Real mode IDT
	 */
	public static final int REAL_MODE = 0;

	/**
	 * Protected mode IDT
	 */
	public static final int PROTECTED_MODE = 1;

	private IdtTypes() {
	}
}
