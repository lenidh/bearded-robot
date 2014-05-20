package interrupts;

/**
 * Basisklasse für alle Interrupt-Handlerklassen. Eine Klasse, welche diese
 * Klasse erweitert, kann von ISRs benachrichtigt werden.
 */
public abstract class InterruptHandler {
	public abstract void onInterrupt(int number, boolean hasErrorCode, int errorCode);
}
