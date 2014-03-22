package video;

/**
 * Stellt einen Speicherbereich dar, der zum speichern von
 * {@link video.VideoChar}s verwendet werden kann.
 *
 * @see video.VideoChar
 */
public class VideoMemory extends STRUCT {

	/**
	 * Ein vordefinierter {@link video.VideoMemory}, der auf den
	 * Bildschirmspeicher verweist.
	 */
	public static final VideoMemory std = (VideoMemory)MAGIC.cast2Struct(0xB8000);

	/**
	 * Die {@link video.VideoChar}s des Speicherbereichs.
	 */
	@SJC(count = 2000)
	public VideoChar[] chars;
}
