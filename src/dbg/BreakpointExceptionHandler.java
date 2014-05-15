package dbg;

import interrupts.InterruptHandler;

class BreakpointExceptionHandler extends InterruptHandler {

	@Override
	public void onInterrupt(int number, Integer errorCode) {
		Bluescreen.show();
	}
}
