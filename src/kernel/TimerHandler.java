package kernel;

import interrupts.InterruptHandler;
import video.Printer;

public class TimerHandler extends InterruptHandler {

	private int state = 0;
	private int delay = 1000;

	private char[] chars = new char[8];

	public TimerHandler() {
		this.chars[0] = '>';
		this.chars[1] = '|';
		this.chars[2] = '<';
		this.chars[3] = '-';
		this.chars[4] = '<';
		this.chars[5] = '|';
		this.chars[6] = '>';
		this.chars[7] = '-';
	}

	@Override
	public void onInterrupt(int number, Integer errorCode) {
		if(delay-- <= 0) {
			delay = 1000;
			Printer.directPrintChar(this.chars[state], 0, 0, Printer.WHITE, Printer.BLACK);
			state = ++state % 8;
		}
	}
}
