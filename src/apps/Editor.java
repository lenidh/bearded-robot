package apps;

import keyboard.Keyboard;
import keyboard.KeyboardListener;
import scheduling.Task;
import timer.Timer;
import video.Printer;

public class Editor extends Task {

	private Listener listener = new Listener(this);

	private Printer printer = new Printer();

	private final EditorChar firstChar = new EditorChar();

	private EditorChar lastChar = firstChar;

	private EditorChar nowChar = firstChar;

	private boolean cursorState = false;

	private int cursorDelay = 500;

	private long lastChangedTime = 0;

	private int oldCursorX = 0;

	private int oldCursorY = 0;

	@Override
	protected void onStart() {
		Keyboard.initstance().addListener(this.listener);
	}

	@Override
	protected void onSchedule() {
		printer.setCursor(0, 2);

		if(Timer.getUpTime() - lastChangedTime > cursorDelay) {
			lastChangedTime = Timer.getUpTime();
			cursorState = !cursorState;
		}

		EditorChar c = firstChar;
		while (c != null) {
			if(c == nowChar) {
				printer.setColor((cursorState) ? Printer.BLACK : Printer.WHITE,
						(cursorState) ? Printer.WHITE : Printer.BLACK);
			} else {
				printer.setColor(Printer.WHITE, Printer.BLACK);
			}

			if(c.value == '\n') {
				printer.print(' ');
				printer.println();
			} else {
				printer.print(c.value);
			}

			c = c.next;
		}
	}

	@Override
	protected void onStop() {
		Keyboard.initstance().removeListener(this.listener);
	}

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
							this.editor.nowChar.next.previous = this.editor.nowChar.previous;
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

	private static class EditorChar {
		public EditorChar next;
		public EditorChar previous;
		public char value = ' ';
	}
}
