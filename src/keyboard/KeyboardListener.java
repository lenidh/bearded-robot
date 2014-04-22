package keyboard;

public abstract class KeyboardListener {
	public abstract void onKeyDown(int value, int keyCode, boolean isChar, int flags);
	public abstract void onKeyUp(int value, int keyCode, boolean isChar, int flags);
}
