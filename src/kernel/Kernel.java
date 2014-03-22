package kernel;

import video.VideoChar;
import video.VideoMemory;

@SuppressWarnings("unused")
public class Kernel {
	public static void main() {
		clear();
		print("Hello World!!!");
		while (true) {}
	}

	/**
	 * Löscht alle Zeichen auf dem Bildschirm.
	 */
	public static void clear() {
		for(VideoChar vidChar : VideoMemory.std.chars) {
			vidChar.ascii = 32; // Leerzeichen
			vidChar.color = 0;
		}
	}

	/**
	 * Gibt eine Zeichenkette auf dem Bildschirm aus. Die Ausgabe beginnt am
	 * Anfang des Bildschirms.
	 *
	 * @param str Die auszugebende Zeichenkette.
	 */
	public static void print(String str) {
		for (int i = 0; i < str.length(); i++) {
			VideoMemory.std.chars[i].ascii = (byte)str.charAt(i);
			VideoMemory.std.chars[i].color = (byte)(i % 5 + 10); // Farben 2, 3, 4, 5 und 6 in hell
		}
	}
}
