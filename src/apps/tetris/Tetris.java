package apps.tetris;

import bios.BIOS;
import keyboard.Keyboard;
import keyboard.KeyboardListener;
import scheduling.Task;
import timer.Timer;

public class Tetris extends Task {

	private static final int VideoMemoryBase = 0xA0000;

	private static final int ScreenWidth = 320;

	private static final int ScreenHeight = 200;

	private static final int XSquareNumber = 10;

	private static final int YSquareNumber = 20;

	private static final int Delay = 1000;

	private Listener listener = new Listener(this);

	private long lastChangedTime = 0;

	private Field field;

	private Tetromino currentTetromino = null;

	private int tetrominoX = 0;

	private int tetrominoY = 0;

	@Override
	protected void onStart() {
		// Wechsle in den Grafikmodus.
		BIOS.regs.EAX = 0x0013;
		BIOS.rint(0x10);

		Keyboard.initstance().addListener(this.listener);

		this.field = new Field(XSquareNumber, YSquareNumber);
		this.currentTetromino = null;
		this.tetrominoX = 0;
		this.tetrominoY = 0;
		this.lastChangedTime = 0;
	}

	@Override
	protected void onStop() {
		Keyboard.initstance().removeListener(this.listener);

		// Zurück in den Textmodus wechseln
		BIOS.regs.EAX = 0x0003;
		BIOS.rint(0x10);
	}

	@Override
	protected void onSchedule() {
		drawGui();
		for(int i = 0; i < XSquareNumber; i++) {
			for(int j = 0; j < YSquareNumber; j++) {
				byte color;
				if(this.field.isSet(i, j)) {
					color = this.field.colorAt(i, j);
				} else {
					color = 0;
					if(currentTetromino != null) {
						for(int k = 0; k < currentTetromino.squares.length; k++) {
							Square square = currentTetromino.squares[k];
							if(tetrominoX + square.x == i && tetrominoY + square.y == j) {
								color = currentTetromino.color();
							}
						}
					}
				}
				drawSquare(i, j, color);
			}
		}

		this.field.cleanUp();

		if(Timer.getUpTime() - this.lastChangedTime > Delay) {
			this.lastChangedTime = Timer.getUpTime();
			stepDown();
		}
	}

	private void drawGui() {
		for(int i = 0; i < 30; i++) {
			for(int j = 0; j < 200; j++) {
				MAGIC.wMem8(VideoMemoryBase + pixelOffset(i, j), (byte)0xbf);
			}
		}
		for(int i = 130; i < 320; i++) {
			for(int j = 0; j < 200; j++) {
				MAGIC.wMem8(VideoMemoryBase + pixelOffset(i, j), (byte)0xbf);
			}
		}
	}

	private void drawSquare(int x, int y, byte color) {
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				MAGIC.wMem8(VideoMemoryBase + pixelOffset(30 + 10 * x + i, 10 * YSquareNumber - 10 - 10 * y + j), color);
			}
		}
	}

	private void stepDown() {
		if(this.currentTetromino == null) {
			this.tetrominoY = YSquareNumber;
			this.tetrominoX = XSquareNumber / 2;
			this.currentTetromino = Tetromino.create();
		}

		Tetromino tetromino = this.currentTetromino;
		Square[] squares = tetromino.squares;

		for(int i = 0; i < squares.length; i++) {
			int x = this.tetrominoX + squares[i].x;
			int y = this.tetrominoY + squares[i].y;
			if(y == 0 || this.field.isSet(x, y-1)) {
				finishCurrentTetromino();
				break;
			}
		}

		if(this.currentTetromino != null) {
			this.tetrominoY--;
		}
	}

	private void stepLeft() {
		Tetromino tetromino = this.currentTetromino;
		Square[] squares = tetromino.squares;
		boolean isInBound = true;

		for(int i = 0; i < squares.length; i++) {
			int x = this.tetrominoX + squares[i].x;
			int y = this.tetrominoY + squares[i].y;
			if(x == 0 || this.field.isSet(x-1, y)) {
				isInBound = false;
				break;
			}
		}

		if(isInBound) {
			this.tetrominoX--;
		}
	}

	private void stepRight() {
		Tetromino tetromino = this.currentTetromino;
		Square[] squares = tetromino.squares;
		boolean isInBound = true;

		for(int i = 0; i < squares.length; i++) {
			int x = this.tetrominoX + squares[i].x;
			int y = this.tetrominoY + squares[i].y;
			if(x == XSquareNumber-1 || this.field.isSet(x+1, y)) {
				isInBound = false;
				break;
			}
		}

		if(isInBound) {
			this.tetrominoX++;
		}
	}

	private void rotate() {
		Tetromino tetromino = this.currentTetromino;
		Square[] squares = tetromino.squares;
		boolean isInBound = true;

		int x0 = this.tetrominoX + squares[0].y;
		int x1 = this.tetrominoX + squares[1].y;
		int x2 = this.tetrominoX + squares[2].y;
		int x3 = this.tetrominoX + squares[3].y;
		int y0 = this.tetrominoY + squares[0].x * -1;
		int y1 = this.tetrominoY + squares[1].x * -1;
		int y2 = this.tetrominoY + squares[2].x * -1;
		int y3 = this.tetrominoY + squares[3].x * -1;

		if(x0 < 0 || x0 >= XSquareNumber || y0 < 0 || y0 >= YSquareNumber || this.field.isSet(x0, y0)) {
			isInBound = false;
		} else if(x1 < 0 || x1 >= XSquareNumber || y1 < 0 || y1 >= YSquareNumber || this.field.isSet(x1, y1)) {
			isInBound = false;
		} else if(x2 < 0 || x2 >= XSquareNumber || y2 < 0 || y2 >= YSquareNumber || this.field.isSet(x2, y2)) {
			isInBound = false;
		} else if(x3 < 0 || x3 >= XSquareNumber || y3 < 0 || y3 >= YSquareNumber || this.field.isSet(x3, y3)) {
			isInBound = false;
		}

		if(isInBound) {
			tetromino.rotate();
		}
	}

	private int pixelOffset(int x, int y) {
		return x + y * ScreenWidth;
	}

	private void finishCurrentTetromino() {
		if(this.currentTetromino != null) {
			Tetromino tetromino = this.currentTetromino;
			Square[] squares = tetromino.squares;
			for (int i = 0; i < squares.length; i++) {
				int x = this.tetrominoX + squares[i].x;
				int y = this.tetrominoY + squares[i].y;
				this.field.set(x, y, tetromino.color());
			}
			this.currentTetromino = null;
		}
	}

	private static class Listener extends KeyboardListener {

		private Tetris tetris;

		public Listener(Tetris tetris) {
			this.tetris = tetris;
		}

		@Override
		public void onKeyDown(int value, int keyCode, boolean isChar, int flags) {
			if (!isChar) {
				switch (value) {
					case Keyboard.DOWN:
						this.tetris.stepDown();
						break;
					case Keyboard.LEFT:
						this.tetris.stepLeft();
						break;
					case Keyboard.RIGHT:
						this.tetris.stepRight();
						break;
					case Keyboard.UP:
						this.tetris.rotate();
						break;
					default:
						break;
				}
			}
		}

		@Override
		public void onKeyUp(int value, int keyCode, boolean isChar, int flags) {
		}
	}
}
