package keyboard;

/**
 * Basistyp für Listener, die auf Tastatur-Events reagieren können.
 */
public abstract class KeyboardListener {

	/**
	 * Wird aufgerufen, wenn eine Taste gedrückt wird.
	 *
	 * @param value Der, vom Layout abhängigen, Tastenwert.
	 * @param keyCode Der Tasten-Code.
	 * @param isChar true falls der value-Parameter einem ASCII-Wert entspricht,
	 *               sonst false.
	 * @param flags Flags die den Zustand der Modifizierungstasten angeben.
	 */
	public abstract void onKeyDown(int value, int keyCode, boolean isChar, int flags);

	/**
	 * Wird aufgerufen, wenn eine Taste losgelassen wird.
	 *
	 * @param value Der, vom Layout abhängigen, Tastenwert.
	 * @param keyCode Der Tasten-Code.
	 * @param isChar true falls der value-Parameter einem ASCII-Wert entspricht,
	 *               sonst false.
	 * @param flags Flags die den Zustand der Modifizierungstasten angeben.
	 */
	public abstract void onKeyUp(int value, int keyCode, boolean isChar, int flags);
}
