package dbg;

import interrupts.Interrupts;

public class Debugging {

	private static BreakpointExceptionHandler breakpointHandler = new BreakpointExceptionHandler();

	private static PageFaultExceptionHandler pageFaultHandler = new PageFaultExceptionHandler();

	public static void init()
	{
		Interrupts.HANDLERS[3] = breakpointHandler;
		Interrupts.HANDLERS[14] = pageFaultHandler;
	}
}
