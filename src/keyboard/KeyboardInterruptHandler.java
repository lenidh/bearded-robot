package keyboard;

import interrupts.InterruptHandler;

class KeyboardInterruptHandler extends InterruptHandler {

	private int remaining;

	private int tmp;

	@Override
	public void onInterrupt(int number, Integer errorCode) {
		int b = MAGIC.rIOs8(0x60);
		if(b < 0) { // undo signed conversion
			b += 256;
			b |= 0x80;
		}

		if(remaining > 0) {
			tmp = (tmp << 8) | b;
			remaining--;
		} else {
			if(b >= 0xE2) return;
			tmp = b;
			remaining = b - 0xDF;
		}

		if(remaining <= 0) {
			Keyboard.buffer.push(tmp);
		}
	}
}
