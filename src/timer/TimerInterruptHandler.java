package timer;

import interrupts.InterruptHandler;

public class TimerInterruptHandler extends InterruptHandler {

	TimerInterruptHandler() {
	}

	@Override
	public void onInterrupt(int number, Integer errorCode) {
		Timer.upTime++;
	}
}
