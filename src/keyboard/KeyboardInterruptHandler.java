package keyboard;

import interrupts.InterruptHandler;

class KeyboardInterruptHandler extends InterruptHandler {

	/**
	 * Folgende Bytes, die für einen vollständigen Scan-Code nötig
	 * sind.
	 */
	private int remaining;

	/**
	 * Bisherige Teile eines Scancodes.
	 */
	private int scanCodeBuffer;

	/**
	 * Die letzten byte-Werte.
	 */
	private int breakpointBuffer;

	@Override
	public void onInterrupt(int number, Integer errorCode) {
		int b = MAGIC.rIOs8(0x60) & 0xFF; // vorzeichenlose Konvertierung

		breakpointBuffer = ((breakpointBuffer << 8) | b) & 0xFFFFFF;
		if(breakpointBuffer == 0x1D2A01 || breakpointBuffer == 0x2A1D01) {
			MAGIC.inline(0xCC);
		}

		if(remaining > 0) {
			scanCodeBuffer = (scanCodeBuffer << 8) | b;
			remaining--;
		} else {
			if(b >= 0xE2) return;
			scanCodeBuffer = b;
			remaining = b - 0xDF;
		}

		if(remaining <= 0) {
			Keyboard.initstance().buffer.push(scanCodeBuffer);
		}
	}
}
