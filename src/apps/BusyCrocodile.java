package apps;

import scheduling.Task;
import timer.Timer;
import video.Printer;

/**
 * Anwendung, die einen, sich zeitlich ändernen, Indikator ausgibt.
 */
public class BusyCrocodile extends Task {

	private int state = 0;
	private int delay = 100;
	private long lastChangedTime = 0;

	private char[] chars = new char[8];

	public BusyCrocodile() {
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
	protected void onSchedule() {
		if(Timer.getUpTime() - lastChangedTime > delay) {
			lastChangedTime = Timer.getUpTime();
			Printer.directPrintChar(this.chars[state], 79, 24, Printer.WHITE, Printer.BLACK);
			state = ++state % 8;
		}
	}
}
