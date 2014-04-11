package interrupts;

public abstract class InterruptHandler {
	public abstract void onInterrupt(int number, Integer errorCode);
}
