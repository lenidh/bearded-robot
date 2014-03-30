package video;

/**
 * Stellt verschiedene Methoden zur Ausgabe von Daten auf dem Bildschirm bereit.
 */
public class Printer {

	public static final int BLACK = 0;

	public static final int BLUE = 1;

	public static final int GREEN = 2;

	public static final int TURQUOISE = 3;

	public static final int RED = 4;

	public static final int PURPLE = 5;

	public static final int BROWN = 6;

	public static final int LIGHT_GRAY = 7;

	public static final int GRAY = 8;

	public static final int LIGHT_BLUE = 9;

	public static final int LIGHT_GREEN = 10;

	public static final int CYAN = 11;

	public static final int LIGHT_RED = 12;

	public static final int PINK = 13;

	public static final int YELLOW = 14;

	public static final int WHITE = 15;

	/**
	 * Anzahl der Zeichenspalten des Bildschirms.
	 */
	public static final int SCREEN_WIDTH = 80;

	/**
	 * Anzahl der Zeichenzeilen des Bildschirms.
	 */
	public static final int SCREEN_HEIGHT = 25;

	/**
	 * Feld zum Zugriff auf die Zeichenrepräsentation eines Wertes.
	 */
	private static char digits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C',
	                                'D', 'E', 'F'};

	/**
	 * Die Farbe, in der die {@link Printer}-Instanz Zeichen ausgibt.
	 */
	private byte color = 0x0F; // Weiß auf Schwarz

	/**
	 * Die relative Speicherposition, an die das nächste Zeichen geschrieben
	 * wird.
	 */
	private int cursorPosition = 0;

	/**
	 * Löscht und färbt den gesamten Bildschirm.
	 *
	 * @param color Die Farbe, mit welcher der Bildschirm gefärbt wird.
	 */
	public static void fillScreen(int color) {
		color = (color << 4) + (color & 0xf);
		for (VideoChar vidChar : VideoMemory.std.chars) {
			vidChar.ascii = (byte)' ';
			vidChar.color = (byte)color;
		}
	}

	/**
	 * Gibt ein Zeichen auf dem Bildschirm aus.
	 *
	 * @param value Das Zeichen, welches gedruckt wird.
	 * @param x Die Spalte, in die das Zeichen gedruckt wird.
	 * @param y Die Zeile, in die das Zeichen gedruckt wird.
	 * @param fg Die Farbe des Zeichens.
	 * @param bg Die Hintergrundfarbe des Zeichens.
	 */
	public static void directPrintChar(char value, int x, int y, int fg, int bg) {
		int index = (x + 80 * y) % 2000;
		VideoMemory.std.chars[index].ascii = (byte)value;
		VideoMemory.std.chars[index].color = (byte)(((bg & 0x0F) << 4) + (fg & 0x0F));
	}

	/**
	 * Gibt eine Zeichenkette auf dem Bildschirm aus.
	 *
	 * @param value Die Zeichenkette, welche gedruckt wird.
	 * @param x Die Spalte, in die das erste Zeichen gedruckt wird.
	 * @param y Die Zeile, in die das erste Zeichen gedruckt wird.
	 * @param fg Die Farbe der Zeichen.
	 * @param bg Die Hintergrundfarbe der Zeichen.
	 * @return Die Anzahl der Zeichen, die gedruckt wurden.
	 */
	public static int directPrintString(String value, int x, int y, int fg, int bg) {
		for (int i = 0; i < value.length(); i++) {
			directPrintChar(value.charAt(i), x++, y, fg, bg);
		}
		return value.length();
	}

	/**
	 * Gibt einen int-Wert auf dem Bildschirm aus.
	 *
	 * @param value Der int-Wert, welcher gedruckt wird.
	 * @param base Die Zahlenbasis, welche zur Darstellung des Wertes genutzt
	 *             wird.
	 * @param length Die minimale Anzahl der Zeichen die gedruckt werden.
	 *               Aufgefüllt wird mit 0.
	 * @param x Die Spalte, in die das erste Zeichen gedruckt wird.
	 * @param y Die Zeile, in die das erste Zeichen gedruckt wird.
	 * @param fg Die Farbe der Zeichen.
	 * @param bg Die Hintergrundfarbe der Zeichen.
	 * @return Die Anzahl der Zeichen, die gedruckt wurden.
	 */
	public static int directPrintInt(int value, int base, int length, int x, int y, int fg,
	                                 int bg) {
		if(base < 2 || base > 16) {
			base = 10; // Bei ungültiger Basis wird 10 genutzt.
		}

		byte color = (byte)(((bg & 0x0F) << 4) + (fg & 0x0F));
		int index = (x + 80 * y) % 2000;
		int intLength = 0;
		int charCount = 0;

		// Falls der Wert negativ ist, drucke ein '-' und ersetze den Wert
		// durch dessen Betrag.
		if(value < 0) {
			VideoMemory.std.chars[index].ascii = (byte)'-';
			VideoMemory.std.chars[index++].color = color;
			value = -value;
			charCount++;
		}

		// Zähle die Zeichen des Wertes.
		int tmp = value;
		do {
			intLength++;
			tmp /= base;
		} while (tmp != 0);
		charCount += (length > intLength) ? length : intLength;

		// Fülle mit führenden Nullen auf.
		while (length-- > intLength) {
			VideoMemory.std.chars[index].ascii = (byte)'0';
			VideoMemory.std.chars[index++].color = color;
		}

		// Drucke den Wert;
		while (intLength-- > 0) {
			VideoMemory.std.chars[index + intLength].ascii = (byte)digits[value % base];
			VideoMemory.std.chars[index + intLength].color = color;
			value /= base;
		}

		return charCount;
	}

	/**
	 * Setzt Vordergrundfarbe und Hintergrundfarbe.
	 * @param fg Die Vordergrundfarbe.
	 * @param bg Die Hintergrundfarbe.
	 */
	public void setColor(int fg, int bg) {
		fg &= 0x0F;
		bg &= 0x0F;
		this.color = (byte)((bg << 4) + fg);
	}

	/**
	 * Setzt den Cursor in eine bestimmte Zeile und Spalte.
	 * @param x Die Spalte.
	 * @param y Die Zeile.
	 */
	public void setCursor(int x, int y) {
		this.cursorPosition = (x % SCREEN_WIDTH) + SCREEN_WIDTH * (y % SCREEN_HEIGHT);
	}

	/**
	 * Gibt ein Zeichen auf dem Bildschirm aus.
	 *
	 * @param value Das Zeichen, welches gedruckt wird.
	 */
	public void print(char value) {
		VideoMemory.std.chars[cursorPosition].ascii = (byte)value;
		VideoMemory.std.chars[cursorPosition].color = this.color;
		this.cursorPosition = (cursorPosition + 1) % (SCREEN_WIDTH * SCREEN_HEIGHT);
	}

	/**
	 * Gibt einen int-Wert auf dem Bildschirm aus.
	 *
	 * @param value Der int-Wert, welcher gedruckt wird.
	 */
	public void print(int value) {
		int x = this.cursorPosition % SCREEN_WIDTH;
		int y = this.cursorPosition / SCREEN_WIDTH;
		this.cursorPosition += directPrintInt(value, 10, 0, x, y, this.color, this.color >> 4);
	}

	/**
	 * Gibt einen long-Wert auf dem Bildschirm aus.
	 *
	 * @param value Der long-Wert, welcher gedruckt wird.
	 */
	public void print(long value) {
		int intLength = 0;

		// Falls der Wert negativ ist, drucke ein '-' und ersetze den Wert
		// durch dessen Betrag.
		if(value < 0) {
			VideoMemory.std.chars[this.cursorPosition].ascii = (byte)'-';
			VideoMemory.std.chars[this.cursorPosition++].color = color;
			value = -value;
		}

		// Zähle die Zeichen des Wertes.
		long tmp = value;
		do {
			intLength++;
			tmp /= 10;
		} while (tmp != 0);

		// Drucke den Wert;
		while (intLength-- > 0) {
			VideoMemory.std.chars[this.cursorPosition + intLength].ascii
					= (byte)digits[(int)(value % 10)];
			VideoMemory.std.chars[this.cursorPosition++ + intLength].color = color;
			value /= 10;
		}
	}

	/**
	 * Gibt eine Zeichenkette auf dem Bildschirm aus.
	 *
	 * @param value Die Zeichenkette, welche gedruckt wird.
	 */
	public void print(String value) {
		for (int i = 0; i < value.length(); i++) {
			print(value.charAt(i));
		}
	}

	/**
	 * Gibt die Zeichenkettendarstellung eines Objekts auf dem Bildschirm aus.
	 *
	 * @param object Das Objekt, dessen Zeichenkettendarstellung gedruckt wird.
	 */
	public void print(Object object) {
		print(object.toString());
	}

	/**
	 * Gibt einen byte-Wert in Hexadezimaldarstellung mit "0x"-Präfix aus.
	 *
	 * @param value Der byte-Wert, welcher gedruckt wird.
	 */
	public void printHex(byte value) {
		print('0');
		print('x');
		for(int i = 4; i >= 0; i-=4) {
			int d = ((value >> i) & 0xF);
			print((char)((d > 9) ? d + 55 : d + 48));
		}
	}

	/**
	 * Gibt einen short-Wert in Hexadezimaldarstellung mit "0x"-Präfix aus.
	 *
	 * @param value Der short-Wert, welcher gedruckt wird.
	 */
	public void printHex(short value) {
		print('0');
		print('x');
		for(int i = 12; i >= 0; i-=4) {
			int d = ((value >> i) & 0xF);
			print((char)((d > 9) ? d + 55 : d + 48));
		}
	}

	/**
	 * Gibt einen int-Wert in Hexadezimaldarstellung mit "0x"-Präfix aus.
	 *
	 * @param value Der int-Wert, welcher gedruckt wird.
	 */
	public void printHex(int value) {
		print('0');
		print('x');
		for(int j = 28; j >= 0; j-=4) {
			int d = ((value >> j) & 0xF);
			print((char)((d > 9) ? d + 55 : d + 48));
		}
	}

	/**
	 * Gibt einen long-Wert in Hexadezimaldarstellung mit "0x"-Präfix aus.
	 *
	 * @param value Der long-Wert, welcher gedruckt wird.
	 */
	public void printHex(long value) {
		print('0');
		print('x');
		for(int i = 60; i >= 0; i-=4) {
			int d = (int)((value >> i) & 0xF);
			print((char)((d > 9) ? d + 55 : d + 48));
		}
	}

	/**
	 * Setzt den Cursor an den Beginn der nächsten Zeile.
	 */
	public void println() {
		this.cursorPosition = (this.cursorPosition / SCREEN_WIDTH + 1) * SCREEN_WIDTH;
	}

	/**
	 * Gibt eine Zeichen auf dem Bildschirm aus und setzt den Cursor in die
	 * nächste Zeile.
	 *
	 * @param c Das Zeichen, welches gedruckt wird.
	 */
	public void println(char c) {
		print(c);
		println();
	}

	/**
	 * Gibt einen int-Wert auf dem Bildschirm aus und setzt den Cursor in die
	 * nächste Zeile.
	 *
	 * @param value Der int-Wert, welcher gedruckt wird.
	 */
	public void println(int value) {
		print(value);
		println();
	}

	/**
	 * Gibt einen long-Wert auf dem Bildschirm aus und setzt den Cursor in die
	 * nächste Zeile.
	 *
	 * @param value Der long-Wert, welcher gedruckt wird.
	 */
	public void println(long value) {
		print(value);
		println();
	}

	/**
	 * Gibt eine Zeichenkette auf dem Bildschirm aus und setzt den Cursor in die
	 * nächste Zeile.
	 *
	 * @param value Die Zeichenkette, welche gedruckt wird.
	 */
	public void println(String value) {
		print(value);
		println();
	}

	/**
	 * Gibt die Zeichenkettendarstellung eines Objekts auf dem Bildschirm aus
	 * und setzt den Cursor in die nächste Zeile.
	 *
	 * @param object Das Objekt, dessen Zeichenkettendarstellung gedruckt wird.
	 */
	public void println(Object object) {
		print(object.toString());
	}

}
