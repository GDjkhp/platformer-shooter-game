package karakters.textstuff;

/**
 * An enumerator which indicates where text should be positioned.
 * 
 * @author	Chris Copeland
 * @version	1.0
 */
public enum TextAlignment
{
	/**
	 * Text should be aligned to the top-left of the target bounds.
	 */
	TOP_LEFT,
	
	/**
	 * Text should be aligned to the top of the target bounds.
	 */
	TOP,
	
	/**
	 * Text should be aligned to the top-right of the target bounds.
	 */
	TOP_RIGHT,
	
	/**
	 * Text should be aligned to the left of the target bounds.
	 */
	MIDDLE_LEFT,
	
	/**
	 * Text should be aligned to the middle of the target bounds.
	 */
	MIDDLE,
	
	/**
	 * Text should be aligned to the right of the target bounds.
	 */
	MIDDLE_RIGHT,
	
	/**
	 * Text should be aligned to the bottom-left of the target bounds.
	 */
	BOTTOM_LEFT,
	
	/**
	 * Text should be aligned to the bottom of the target bounds.
	 */
	BOTTOM,
	
	/**
	 * Text should be aligned to the bottom-right of the target bounds.
	 */
	BOTTOM_RIGHT;
	
	private TextAlignment() {
		
	}
	
	/**
	 * Gets whether the text alignment is aligned to the bottom.
	 * 
	 * @return	true if the text alignment is aligned to the bottom; otherwise, false
	 */
	public boolean isBottom() {
		return this == BOTTOM || this == BOTTOM_LEFT || this == BOTTOM_RIGHT;
	}
	
	/**
	 * Gets whether the text alignment is centered horizontally.
	 * 
	 * @return	true if the text alignment is horizontally centered; otherwise, false
	 */
	public boolean isCenter() {
		return this == TOP || this == MIDDLE || this == BOTTOM;
	}
	
	/**
	 * Gets whether the text alignment is aligned to the left.
	 * 
	 * @return	true if the text alignment is aligned to the left; otherwise, false
	 */
	public boolean isLeft() {
		return this == TOP_LEFT || this == MIDDLE_LEFT || this == BOTTOM_LEFT;
	}
	
	/**
	 * Gets whether the text alignment is aligned to the middle.
	 * 
	 * @return	true if the text alignment is aligned to the middle; otherwise, false
	 */
	public boolean isMiddle() {
		return this == MIDDLE || this == MIDDLE_LEFT || this == MIDDLE_RIGHT;
	}
	
	/**
	 * Gets whether the text alignment is aligned to the right.
	 * 
	 * @return	true if the text alignment is aligned to the right; otherwise, false
	 */
	public boolean isRight() {
		return this == TOP_RIGHT || this == MIDDLE_RIGHT || this == BOTTOM_RIGHT;
	}
	
	/**
	 * Gets whether the text alignment is aligned to the top.
	 * 
	 * @return	true if the text alignment is aligned to the top; otherwise, false
	 */
	public boolean isTop() {
		return this == TOP || this == TOP_LEFT || this == TOP_RIGHT;
	}
}
