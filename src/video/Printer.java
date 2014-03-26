package video;

public class Printer {

	private byte color = 0x0F; // Weiß auf Schwarz

	private int cursorX = 0;

	private int cursorY = 0;

	public void setColor(int fg, int bg) {
		fg &= 0x0F;
		bg &= 0x0F;
		this.color = (byte)((bg << 4) + fg);
	}

	public void setCursor(int x, int y) {
		this.cursorX = x % 80;
		this.cursorY = y % 25;
	}

	public void clear() {
		for (VideoChar vidChar : VideoMemory.std.chars) {
			vidChar.ascii = 32; // Leerzeichen
			vidChar.color = this.color;
		}
	}

	public void print(char c) {
		int index = this.cursorX + 80 * this.cursorY;

		VideoMemory.std.chars[index].ascii = (byte)c;
		VideoMemory.std.chars[index].color = this.color;

		this.cursorX = (this.cursorX + 1) % 80;
		if (this.cursorX == 0) {
			this.cursorY = (this.cursorY + 1) % 25;
		}
	}

	public void print(int i) {
		if (i == 0) {
			print('0');
		} else {
			if (i < 0) {
				print('-');
				i *= -1;
			}

			int index = this.cursorX * (this.cursorY + 1);
			int length = 0;
			int tmp = i;
			while(tmp != 0) {
				tmp /= 10;
				length++;
				this.cursorX = (this.cursorX + 1) % 80;
				if (this.cursorX == 0) {
					this.cursorY = (this.cursorY + 1) % 25;
				}
			}

			for(int j = length - 1; j >= 0; j--) {
				VideoMemory.std.chars[index + j].ascii = (byte)((i % 10) + 48);
				VideoMemory.std.chars[index + j].color = this.color;
				i /= 10;
			}
		}
	}

	public void print(long l) {
		if (l == 0) {
			print('0');
		} else {
			if (l < 0) {
				print('-');
				l *= -1;
			}

			int index = this.cursorX * (this.cursorY + 1);
			int length = 0;
			long tmp = l;
			while(tmp != 0) {
				tmp /= 10;
				length++;
				this.cursorX = (this.cursorX + 1) % 80;
				if (this.cursorX == 0) {
					this.cursorY = (this.cursorY + 1) % 25;
				}
			}

			for(int i = length - 1; i >= 0; i--) {
				VideoMemory.std.chars[index + i].ascii = (byte)((l % 10) + 48);
				VideoMemory.std.chars[index + i].color = this.color;
				l /= 10;
			}
		}
	}

	public void print(String str) {
		for (int i = 0; i < str.length(); i++) {
			print(str.charAt(i));
		}
	}

	public void printHex(byte b) {
		print('0');
		print('x');
		for(int i = 4; i >= 0; i-=4) {
			int d = ((b >> i) & 0xF);
			print((char)((d > 9) ? d + 55 : d + 48));
		}
	}

	public void printHex(short s) {
		print('0');
		print('x');
		for(int i = 12; i >= 0; i-=4) {
			int d = ((s >> i) & 0xF);
			print((char)((d > 9) ? d + 55 : d + 48));
		}
	}

	public void printHex(int i) {
		print('0');
		print('x');
		for(int j = 28; j >= 0; j-=4) {
			int d = ((i >> j) & 0xF);
			print((char)((d > 9) ? d + 55 : d + 48));
		}
	}

	public void printHex(long l) {
		print('0');
		print('x');
		for(int i = 60; i >= 0; i-=4) {
			int d = (int)((l >> i) & 0xF);
			print((char)((d > 9) ? d + 55 : d + 48));
		}
	}

	public void println() {
		this.cursorX = 0;
		this.cursorY = (this.cursorY + 1) % 25;
	}

	public void println(char c) {
		print(c);
		println();
	}

	public void println(int i) {
		print(i);
		println();
	}

	public void println(long l) {
		print(l);
		println();
	}

	public void println(String str) {
		print(str);
		println();
	}

}
