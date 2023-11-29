package karakters.textstuff;

/**
 * A class which provides formatting flags for use when rendering text.
 * 
 * @author	Chris Copeland
 * @version	1.0
 */
public final class TextFormat
{
	/**
	 * No additional formatting flags should be used.
	 */
	public final static int NONE = 0;
	
	/**
	 * Indicates that the renderer should not use anti-aliasing when rendering the text.
	 */
	public final static int NO_ANTI_ALIASING = 1;
	
	/**
	 * Indicates that the first line of the text should always be visible.
	 */
	public final static int FIRST_LINE_VISIBLE = 2;
	
	/**
	 * Initialize a new instance of the {@link TextFormat} class.
	 */
	private TextFormat() {
		
	}
	
	/**
	 * Gets whether the target <i>format</i> contains a specific <i>flag</i>.
	 * 
	 * @param format	The compiled format flags
	 * @param flag		The flag to find in the format flags
	 * @return true if the format flags contains the specific flag; otherwise, false 
	 */
	public static boolean isEnabled(int format, int flag) {
		return (format & flag) == flag;
	}
}
