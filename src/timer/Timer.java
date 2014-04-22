package timer;

import interrupts.Interrupts;

public class Timer {

	public static void init() {
		// Ein Interrupt pro Millisekunde (1193182Hz / 1000Hz = 1193)
		MAGIC.wIOs8(0x43, (byte)0x36); // Kanal 0 wählen
		MAGIC.wIOs8(0x40, (byte)(1193&0xFF)); // unteres Divisor-Byte
		MAGIC.wIOs8(0x40, (byte)((1193>>8)&0xFF)); // oberes Divisor-Byte

		Interrupts.HANDLERS[32] = new TimerInterruptHandler();
	}
}
