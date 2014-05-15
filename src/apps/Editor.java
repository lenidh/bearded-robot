package apps;

import bios.BIOS;
import keyboard.Keyboard;
import keyboard.KeyboardListener;
import scheduling.Task;
import timer.Timer;
import video.Printer;

/**
 * Eine einfache Editoranwendung.
 */
public class Editor extends Task {

	/**
	 * Der Listener, welcher bei Tastatur-Events benachrichtigt wird.
	 */
	private Listener listener = new Listener(this);

	/**
	 * Printer zur Ausgabe des Editorinhalts.
	 */
	private Printer printer = new Printer();

	/**
	 * Der erste EditorChar des Editorinhalts.
	 */
	private final EditorChar firstChar = new EditorChar();

	/**
	 * Der letzte EditorChar des Editorinhalts.
	 */
	private EditorChar lastChar = firstChar;

	/**
	 * Der EditorChar des Editorinhalts, auf dem der Cursor steht.
	 */
	private EditorChar nowChar = firstChar;

	@Override
	protected void onStart() {
		// Cursor anzeigen
		BIOS.regs.EAX = 0x01 << 8;
		BIOS.regs.ECX = 0x0007;

		BIOS.rint(0x10);

		Keyboard.initstance().addListener(this.listener);
	}

	@Override
	protected void onSchedule() {
		printer.setCursor(0, 2);

		// Zeichenausgabe
		EditorChar c = firstChar;
		int i = 0;
		while (c != null) {
			if(c == nowChar) {
				// Cursor setzen
				BIOS.regs.EAX = 0x02 << 8;
				BIOS.regs.EBX = 0;
				BIOS.regs.EDX = (printer.getCursorY() << 8) | printer.getCursorX();

				BIOS.rint(0x10);
			}
			printer.setColor(Printer.WHITE, Printer.BLACK);

			if(c.value == '\n') {
				printer.print(' ');
				printer.println();
			} else {
				printer.print(c.value);
			}

			c = c.next;
		}

		printer.setColor(Printer.WHITE, Printer.BLACK);
	}

	@Override
	protected void onStop() {
		Keyboard.initstance().removeListener(this.listener);

		// Cursor ausblenden
		BIOS.regs.EAX = 0x01 << 8;
		BIOS.regs.ECX = 0x2607;

		BIOS.rint(0x10);
	}

	/**
	 * Ein Listener, der zum Empfang von Tastatur-Events genutzt werden kann.
	 */
	private static class Listener extends KeyboardListener {

		private Editor editor;

		public Listener(Editor editor) {
			this.editor = editor;
		}

		@Override
		public void onKeyDown(int value, int keyCode, boolean isChar, int flags) {
			if(!isChar && value == Keyboard.RETURN) {
				isChar = true;
				value = '\n';
			}

			if(isChar) {
				this.editor.nowChar.value = (char)value;
				if(this.editor.nowChar.next == null) {
					this.editor.lastChar = new EditorChar();
					this.editor.nowChar.next = this.editor.lastChar;
					this.editor.lastChar.previous = this.editor.nowChar;
					this.editor.nowChar = this.editor.lastChar;
				} else {
					this.editor.nowChar = this.editor.nowChar.next;
				}
			} else {
				switch (value) {
					case Keyboard.BACKSPACE:
						if(this.editor.nowChar.previous != null) {
							this.editor.nowChar.previous.value = this.editor.nowChar.value;
							this.editor.nowChar.previous.next = this.editor.nowChar.next;
							if(this.editor.nowChar.next != null)this.editor.nowChar.next.previous = this.editor.nowChar.previous;
							this.editor.nowChar = this.editor.nowChar.previous;
						}
						break;
				}
			}
		}

		@Override
		public void onKeyUp(int value, int keyCode, boolean isChar, int flags) {

		}
	}

	/**
	 * Ein char-Wrapper, der die doppelte Verkettung von chars ermöglicht.
	 */
	private static class EditorChar {
		public EditorChar next;
		public EditorChar previous;
		public char value = ' ';
	}
}
