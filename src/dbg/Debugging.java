package dbg;

import interrupts.Interrupts;

public class Debugging {

	private static BluescreenExceptionHandler bluescreenHandler = new BluescreenExceptionHandler();

	public static void init()
	{
		Interrupts.HANDLERS[3] = bluescreenHandler;
		Interrupts.HANDLERS[5] = bluescreenHandler;
		Interrupts.HANDLERS[14] = bluescreenHandler;
	}
}
