package keyboard;

class Layout {
	public boolean isCharacter(int keyCode) {
		switch (keyCode) {
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 14:
			case 15:
			case 16:
			case 17:
			case 18:
			case 19:
			case 20:
			case 21:
			case 22:
			case 23:
			case 24:
			case 25:
			case 27:
			case 30:
			case 31:
			case 32:
			case 33:
			case 34:
			case 35:
			case 36:
			case 37:
			case 38:
			case 43:
			case 44:
			case 45:
			case 46:
			case 47:
			case 48:
			case 49:
			case 50:
			case 51:
			case 52:
			case 53:
			case 57:
				return true;
		}
		return false;
	}

	public int value(int keyCode) {
		switch (keyCode) {
			case 2: // 1
				return 49;
			case 3: // 2
				return 50;
			case 4: // 3
				return 51;
			case 5: // 4
				return 52;
			case 6: // 5
				return 53;
			case 7: // 6
				return 54;
			case 8: // 7
				return 55;
			case 9: // 8
				return 56;
			case 10: // 9
				return 57;
			case 11: // 0
				return 48;
			case 14:
				return 8;
			case 15:
				return 9;
			case 16: // q
				if(Keyboard.isMod1()) {
					return 81;
				}
				return 113;
			case 17: // w
				if(Keyboard.isMod1()) {
					return 87;
				}
				return 119;
			case 18: // e
				if(Keyboard.isMod1()) {
					return 69;
				}
				return 101;
			case 19: // r
				if(Keyboard.isMod1()) {
					return 82;
				}
				return 114;
			case 20: // t
				if(Keyboard.isMod1()) {
					return 84;
				}
				return 116;
			case 21: // z
				if(Keyboard.isMod1()) {
					return 90;
				}
				return 122;
			case 22: // u
				if(Keyboard.isMod1()) {
					return 85;
				}
				return 117;
			case 23: // i
				if(Keyboard.isMod1()) {
					return 73;
				}
				return 105;
			case 24: // o
				if(Keyboard.isMod1()) {
					return 79;
				}
				return 111;
			case 25: // p
				if(Keyboard.isMod1()) {
					return 80;
				}
				return 112;
			case 27:
				if(Keyboard.isMod1()) {
					return 42;
				}
				return 43;
			case 28:
				return Keyboard.RETURN;
			case 29:
				return Keyboard.CTRL;
			case 30: // a
				if(Keyboard.isMod1()) {
					return 65;
				}
				return 97;
			case 31: // s
				if(Keyboard.isMod1()) {
					return 83;
				}
				return 115;
			case 32: // d
				if(Keyboard.isMod1()) {
					return 68;
				}
				return 100;
			case 33: // f
				if(Keyboard.isMod1()) {
					return 70;
				}
				return 102;
			case 34: // g
				if(Keyboard.isMod1()) {
					return 71;
				}
				return 103;
			case 35: // h
				if(Keyboard.isMod1()) {
					return 72;
				}
				return 104;
			case 36: // j
				if(Keyboard.isMod1()) {
					return 74;
				}
				return 106;
			case 37: // k
				if(Keyboard.isMod1()) {
					return 75;
				}
				return 107;
			case 38: // l
				if(Keyboard.isMod1()) {
					return 76;
				}
				return 108;
			case 42:
				return Keyboard.SHIFT_LEFT;
			case 43: // #
				if(Keyboard.isMod1()) {
					return 35;
				}
				return 39;
			case 44: // y
				if(Keyboard.isMod1()) {
					return 89;
				}
				return 121;
			case 45: // x
				if(Keyboard.isMod1()) {
					return 88;
				}
				return 120;
			case 46: // c
				if(Keyboard.isMod1()) {
					return 67;
				}
				return 99;
			case 47: // v
				if(Keyboard.isMod1()) {
					return 86;
				}
				return 118;
			case 48: // b
				if(Keyboard.isMod1()) {
					return 66;
				}
				return 98;
			case 49: // n
				if(Keyboard.isMod1()) {
					return 78;
				}
				return 110;
			case 50: // m
				if(Keyboard.isMod1()) {
					return 77;
				}
				return 109;
			case 51: // ,
				if(Keyboard.isMod1()) {
					return 59;
				}
				return 44;
			case 52: // .
				if(Keyboard.isMod1()) {
					return 58;
				}
				return 46;
			case 53: // -
				if(Keyboard.isMod1()) {
					return 95;
				}
				return 45;
			case 54:
				return Keyboard.SHIFT_RIGHT;
			case 56:
				return Keyboard.ALT;
			case 57:
				return 32;
			case 58:
				return Keyboard.CAPS_LOCK;
		}

		return Keyboard.UNSPECIFIED;
	}
}