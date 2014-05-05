package dbg;

import interrupts.Interrupts;

public class Debugging {

	private static BreakpointExceptionHandler exceptionHandler = new BreakpointExceptionHandler();

	public static void init()
	{
		Interrupts.HANDLERS[3] = exceptionHandler;
	}
}
