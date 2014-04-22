package keyboard;

import container.IntegerRingBuffer;
import interrupts.Interrupts;

public class Keyboard {

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

	public static final int FLAG_SHIFT = 1;

	public static final int FLAG_CTRL = 2;

	public static final int FLAG_CAPS_LOCK = 4;

	public static final int FLAG_ALT = 8;

	public static final int FLAG_NUM_LOCK = 16;

	static IntegerRingBuffer buffer = null;

	private static KeyboardInterruptHandler interruptHandler = new KeyboardInterruptHandler();

	private static Layout layout = new Layout();

	private static KeyboardListener listener = null;

	private static int toggleFlags = 0;

	public static void init() {
		buffer = new IntegerRingBuffer(32);

		Interrupts.HANDLERS[33] = interruptHandler;
	}

	public static void setListener(KeyboardListener listener) {
		Keyboard.listener = listener;
	}

	public static void process() {
		while (buffer.size() > 0) {
			int scanCode = buffer.front();
			buffer.pop();

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

			if(listener != null) {
				if (isDown) {
					listener.onKeyDown(value, keyCode, isChar, toggleFlags);
				} else {
					listener.onKeyUp(value, keyCode, isChar, toggleFlags);
				}
			}
		}
	}

	public static boolean isMod1() {
		boolean caps = (toggleFlags & FLAG_CAPS_LOCK) == FLAG_CAPS_LOCK;
		return (caps) ? (toggleFlags & FLAG_SHIFT) != FLAG_SHIFT : (toggleFlags & FLAG_SHIFT) == FLAG_SHIFT;
	}

	private Keyboard() {
	}
}
