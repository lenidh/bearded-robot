package java.lang;

/**
 * Gibt an, dass die genannten Warnungen im annotierten Element unterdrückt
 * werden sollen.
 *
 * <p>
 * Dieses Interface wird aus Kompatibilitätsgründen bereitgestellt. Zum Beispiel
 * um in einer IDE die Nutzung von Codeanalysefunktionen zu vereinfachen.
 * </p>
 */
@SJC.IgnoreUnit
public @interface SuppressWarnings {
	public String[] value();
}
