package test;

import keyboard.Keyboard;
import keyboard.KeyboardListener;
import video.Printer;

public class KeyboardTest extends KeyboardListener {

	public KeyboardTest() {
		Printer.fillScreen(Printer.BLACK);
		Printer.directPrintString("[Shift] [Caps] [Ctrl] [Alt] [Return] [Backspace] [Tab]", 0, 0, Printer.WHITE, Printer.BLACK);
	}

	@Override
	public void onKeyDown(int value, int keyCode, boolean isChar, int flags) {
		if(isChar) {
			switch (value) {
				case 8:
					Printer.directPrintString("[Backspace]", 37, 0, Printer.BLACK, Printer.WHITE);
					break;
				case 9:
					Printer.directPrintString("[Tab]", 49, 0, Printer.BLACK, Printer.WHITE);
					break;
				default:
					Printer.directPrintChar((char)value, 0, 2, Printer.BLACK, Printer.WHITE);
					break;
			}
		} else {
			switch (value) {
				case Keyboard.SHIFT_LEFT:
				case Keyboard.SHIFT_RIGHT:
					Printer.directPrintString("[Shift]", 0, 0, Printer.BLACK, Printer.WHITE);
					break;
				case Keyboard.CAPS_LOCK:
					Printer.directPrintString("[Caps]", 8, 0, Printer.BLACK, Printer.WHITE);
					break;
				case Keyboard.CTRL:
					Printer.directPrintString("[Ctrl]", 15, 0, Printer.BLACK, Printer.WHITE);
					break;
				case Keyboard.ALT:
					Printer.directPrintString("[Alt]", 22, 0, Printer.BLACK, Printer.WHITE);
					break;
				case Keyboard.RETURN:
					Printer.directPrintString("[Return]", 28, 0, Printer.BLACK, Printer.WHITE);
					break;
			}
		}
	}

	@Override
	public void onKeyUp(int value, int keyCode, boolean isChar, int flags) {
		if(isChar) {
			switch (value) {
				case 8:
					Printer.directPrintString("[Backspace]", 37, 0, Printer.WHITE, Printer.BLACK);
					break;
				case 9:
					Printer.directPrintString("[Tab]", 49, 0, Printer.WHITE, Printer.BLACK);
					break;
				default:
					Printer.directPrintChar((char)value, 0, 2, Printer.WHITE, Printer.BLACK);
					break;
			}
		} else {
			switch (value) {
				case Keyboard.SHIFT_LEFT:
				case Keyboard.SHIFT_RIGHT:
					Printer.directPrintString("[Shift]", 0, 0, Printer.WHITE, Printer.BLACK);
					break;
				case Keyboard.CAPS_LOCK:
					if((flags & Keyboard.FLAG_CAPS_LOCK) == Keyboard.FLAG_CAPS_LOCK) {
						Printer.directPrintString("[Caps]", 8, 0, Printer.RED, Printer.BLACK);
					} else {
						Printer.directPrintString("[Caps]", 8, 0, Printer.WHITE, Printer.BLACK);
					}
					break;
				case Keyboard.CTRL:
					Printer.directPrintString("[Ctrl]", 15, 0, Printer.WHITE, Printer.BLACK);
					break;
				case Keyboard.ALT:
					Printer.directPrintString("[Alt]", 22, 0, Printer.WHITE, Printer.BLACK);
					break;
				case Keyboard.RETURN:
					Printer.directPrintString("[Return]", 28, 0, Printer.WHITE, Printer.BLACK);
					break;
			}
		}
	}
}