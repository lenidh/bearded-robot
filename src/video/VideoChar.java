package video;

/**
 * Stellt ein Zeichen des Bildschirms dar.
 *
 * @see video.VideoMemory
 */
public class VideoChar extends STRUCT {

	/**
	 * Der ASCII-Code des Zeichens.
	 */
	public byte ascii;

	/**
	 * Der Farbcode des Zeichens.
	 *
	 * <p>
	 * <table cellpadding=3 border=1 width="100%">
	 * <tr align="center">
	 * <th colspan=8>Byte</th>
	 * </tr>
	 * <tr align="center">
	 * <td>Bit</td>
	 * <td>7</td>
	 * <td>6</td>
	 * <td>5</td>
	 * <td>4</td>
	 * <td>3</td>
	 * <td>2</td>
	 * <td>1</td>
	 * <td>0</td>
	 * </tr>
	 * <tr align="center">
	 * <td>Funktion</td>
	 * <td>Blink</td>
	 * <td colspan="3">Hintergrund</td>
	 * <td>Hell</td>
	 * <td colspan="3">Vordergrund</td>
	 * </tr>
	 * </table>
	 * </p>
	 * <p>
	 * <table cellpadding=3 border=1 width="100%">
	 * <tr align="center">
	 * <th colspan=8>Farbe</th>
	 * </tr>
	 * <tr align="center">
	 * <td>Nr.</td>
	 * <td>0</td>
	 * <td>1</td>
	 * <td>2</td>
	 * <td>3</td>
	 * <td>4</td>
	 * <td>5</td>
	 * <td>6</td>
	 * <td>7</td>
	 * </tr>
	 * <tr align="center">
	 * <td>Farbe</td>
	 * <td>Schwarz</td>
	 * <td>Blau</td>
	 * <td>Grün</td>
	 * <td>Türkis</td>
	 * <td>Rot</td>
	 * <td>Violett</td>
	 * <td>Braun</td>
	 * <td>Grau</td>
	 * </tr>
	 * </table>
	 * </p>
	 */
	public byte color;
}
