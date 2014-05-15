package dbg;

import interrupts.InterruptHandler;

public class PageFaultExceptionHandler extends InterruptHandler {
	@Override
	public void onInterrupt(int number, Integer errorCode) {
		Bluescreen.show(errorCode);
	}
}
