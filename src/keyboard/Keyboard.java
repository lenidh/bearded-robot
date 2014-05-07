package keyboard;

import container.IntegerRingBuffer;
import interrupts.Interrupts;
import scheduling.Task;

/**
 * Keyboard-Verwaltungsklasse
 */
public class Keyboard extends Task {

	// Konstanten für nicht als char darstellbare Tastenwerte.

	public static final int UNSPECIFIED = 0;

	public static final int F1 = 1;

	public static final int F2 = 2;

	public static final int F3 = 3;

	public static final int F4 = 4;

	public static final int F5 = 5;

	public static final int F6 = 6;

	public static final int F7 = 7;

	public static final int F8 = 8;

	public static final int F9 = 9;

	public static final int F10 = 10;

	public static final int F11 = 11;

	public static final int F12 = 12;

	public static final int SHIFT_LEFT = 13;

	public static final int SHIFT_RIGHT = 14;

	public static final int CTRL = 15;

	public static final int ALT = 16;

	public static final int CAPS_LOCK = 17;

	public static final int RETURN = 18;

	public static final int NUM_LOCK = 19;

	public static final int BACKSPACE = 20;


	// Flags-Werte von Modifizierungstasten

	public static final int FLAG_SHIFT = 1;

	public static final int FLAG_CTRL = 2;

	public static final int FLAG_CAPS_LOCK = 4;

	public static final int FLAG_ALT = 8;

	public static final int FLAG_NUM_LOCK = 16;

	/**
	 * Puffer für empfangene Tastatur-Scan-Codes.
	 */
	IntegerRingBuffer buffer = new IntegerRingBuffer(32);;

	/**
	 * Der Interrupt-Handler für IRQ1.
	 */
	private KeyboardInterruptHandler interruptHandler = new KeyboardInterruptHandler();

	/**
	 * Das Layout der Tastatur.
	 */
	private Layout layout = new Layout();

	/**
	 * Die Listener, die über eingehende Tastatur-Events benachrichtigt werden.
	 */
	private KeyboardListener listenerRoot = null;

	/**
	 * Flags die den Zustand der Modifizierungstasten angeben.
	 */
	private int toggleFlags = 0;

	private static Keyboard instance = null;

	/**
	 * Initialisiert die Keyboard-Funktionalität.
	 */
	public static Keyboard initstance() {
		if(instance == null) {
			instance = new Keyboard();
		}
		return instance;
	}

	private Keyboard() {
		Interrupts.HANDLERS[33] = interruptHandler;
	}

	public void addListener(KeyboardListener listener) {
		listener.next = this.listenerRoot;
		this.listenerRoot = listener;
	}

	public void removeListener(KeyboardListener listener) {
		if(this.listenerRoot == null) {
			return;
		}

		if(this.listenerRoot == listener) {
			this.listenerRoot = this.listenerRoot.next;
			return;
		}

		KeyboardListener prev = this.listenerRoot;
		KeyboardListener now = this.listenerRoot.next;
		while (true) {
			if(now == listener) {
				prev.next = now.next;
				return;
			}
			prev = now;
			now = now.next;
		}
	}

	/**
	 * Verarbeitet die aktuell im Puffer vorhandenen Scan-Codes.
	 */
	public void onSchedule() {
		while (this.buffer.size() > 0) {
			int scanCode = this.buffer.front();
			this.buffer.pop();

			int keyCode = scanCode & 0xFFFFFF7F;
			boolean isDown = (scanCode & 0x80) == 0;

			int value = layout.value(keyCode);
			boolean isChar = layout.isCharacter(keyCode);

			if(!isChar) {
				switch (value) {
					case SHIFT_LEFT:
					case SHIFT_RIGHT:
						toggleFlags = (isDown) ? (toggleFlags | FLAG_SHIFT) : (toggleFlags & ~FLAG_SHIFT);
						break;
					case CTRL:
						toggleFlags = (isDown) ? (toggleFlags | FLAG_CTRL) : (toggleFlags & ~FLAG_CTRL);
						break;
					case CAPS_LOCK:
						if(isDown) toggleFlags = ((toggleFlags & FLAG_CAPS_LOCK) == FLAG_CAPS_LOCK) ? (toggleFlags & ~FLAG_CAPS_LOCK) : (toggleFlags | FLAG_CAPS_LOCK);
						break;
					case ALT:
						toggleFlags = (isDown) ? (toggleFlags | FLAG_ALT) : (toggleFlags & ~FLAG_ALT);
						break;
					case NUM_LOCK:
						if(isDown) toggleFlags = ((toggleFlags & FLAG_NUM_LOCK) == FLAG_NUM_LOCK) ? (toggleFlags & ~FLAG_NUM_LOCK) : (toggleFlags | FLAG_NUM_LOCK);
						break;
				}
			}

			KeyboardListener listener = this.listenerRoot;
			while(listener != null) {
				if (isDown) {
					listener.onKeyDown(value, keyCode, isChar, toggleFlags);
				} else {
					listener.onKeyUp(value, keyCode, isChar, toggleFlags);
				}
				listener = listener.next;
			}
		}
	}

	/**
	 * Ermittelt, ob die Modifizierungstaste(n) für das zweite Level der
	 * Tastenwerte aktiv sind.
	 *
	 * @return true falls das zweite Level der Tasten genutzt werden soll, sonst
	 * false.
	 */
	public boolean isMod1() {
		boolean caps = (toggleFlags & FLAG_CAPS_LOCK) == FLAG_CAPS_LOCK;
		return (caps) ? (toggleFlags & FLAG_SHIFT) != FLAG_SHIFT : (toggleFlags & FLAG_SHIFT) == FLAG_SHIFT;
	}

	/**
	 * Ermittelt, ob der Num-Lock aktiv ist.
	 *
	 * @return true falls der Num-Lock aktiv ist, sonst false.
	 */
	public boolean isNumLk() {
		return ((toggleFlags & FLAG_NUM_LOCK) == FLAG_NUM_LOCK);
	}
}
