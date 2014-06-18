package apps.tetris;

public abstract class Tetromino {

	protected final Square[] squares = new Square[4];

	private static int type = 0;

	public static Tetromino create() {
		Tetromino tetromino = null;
		switch (type) {
			case 0:
				tetromino = new ITetromino();
				break;
			case 1:
				tetromino = new JTetromino();
				break;
			case 2:
				tetromino = new LTetromino();
				break;
			case 3:
				tetromino = new OTetromino();
				break;
			case 4:
				tetromino = new STetromino();
				break;
			case 5:
				tetromino = new TTetromino();
				break;
			case 6:
				tetromino = new ZTetromino();
				break;
		}
		type = ++type % 7;
		return tetromino;
	}

	protected Tetromino() { }

	public void rotate() {
		for(int i = 0; i < squares.length; i++) {
			int tmp = this.squares[i].x;
			MAGIC.assign(this.squares[i].x, this.squares[i].y);
			MAGIC.assign(this.squares[i].y, tmp * -1);
		}
	}

	public void rotateLeft() {
		for(int i = 0; i < squares.length; i++) {
			int tmp = this.squares[i].x;
			MAGIC.assign(this.squares[i].x, this.squares[i].y);
			MAGIC.assign(this.squares[i].y, tmp * -1);
		}
	}

	public Square square(int index) {
		return this.squares[index % this.squares.length];
	}

	public byte color() {
		return (byte)0xbf;
	}
}
