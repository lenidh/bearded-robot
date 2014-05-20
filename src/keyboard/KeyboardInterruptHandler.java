package keyboard;

import interrupts.InterruptHandler;
import kernel.Kernel;

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
	private int resentBuffer;

	@Override
	public void onInterrupt(int number, boolean hasErrorCode, int errorCode) {
		int b = MAGIC.rIOs8(0x60) & 0xFF; // vorzeichenlose Konvertierung

		// Breakpoint-Exception bei Strg+Schift+Esc
		resentBuffer = ((resentBuffer << 8) | b) & 0xFFFFFF;
		if(resentBuffer == 0x1D2A01 || resentBuffer == 0x2A1D01) {
			MAGIC.inline(0xCC);
		}

		// Aktuellen Task abbrechen und Scheduler zurücksetzen
		if((resentBuffer & 0xFFFF) == 0x1D01 && Kernel.scheduler != null) {
			// EBP vom ISR ermitteln
			int ebp=0;
			MAGIC.inline(0x89, 0x6D); MAGIC.inlineOffset(1, ebp); //mov [ebp+xx],ebp
			ebp = MAGIC.rMem32(ebp); // Interrupt-Handler ausschließen

			Kernel.scheduler.stopCurrent();
			Kernel.scheduler.reset(ebp);
			return;
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
