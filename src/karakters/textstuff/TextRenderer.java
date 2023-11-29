package karakters.textstuff;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

/**
 * A class which provides static methods for rendering text using alignment.
 * 
 * @author	Chris Copeland
 * @version	1.0
 */
public final class TextRenderer
{	
	/**
	 * Initialize a new instance of the {@link TextRenderer} class.
	 */
	private TextRenderer() {
		
	}

	/**
	 * Draws a string onto a <code>Graphics</code> handle, using a <code>Font</code>, <code>Color</code> and target bounds to calculate
	 * the location and automatic wrapping of text. The <i>align</i> property determines where the text will be positioned.
	 * 
	 * @param g			A <code>Graphics</code> handle which is the target of the draw operation
	 * @param text		A <code>String</code> containing the text to draw
	 * @param font		The <code>Font</code> to use when drawing the text
	 * @param color		The <code>Color</code> to use when drawing the text
	 * @param bounds	A <code>Rectangle</code> representing the bounds of the text
	 * @return			A <code>Rectangle</code> representing the bounds consumed by the text
	 */
	public static Rectangle drawString(Graphics g, String text, Font font, Color color, Rectangle bounds) {
		return drawString(g, text, font, color, bounds, TextAlignment.TOP_LEFT, TextFormat.NONE);
	}

	/**
	 * Draws a string onto a <code>Graphics</code> handle, using a <code>Font</code>, <code>Color</code> and target bounds to calculate
	 * the location and automatic wrapping of text. The <i>align</i> property determines where the text will be positioned.
	 * 
	 * @param g			A <code>Graphics</code> handle which is the target of the draw operation
	 * @param text		A <code>String</code> containing the text to draw
	 * @param font		The <code>Font</code> to use when drawing the text
	 * @param color		The <code>Color</code> to use when drawing the text
	 * @param bounds	A <code>Rectangle</code> representing the bounds of the text
	 * @param align		A <code>TextAlignment</code> value representing the location to draw the text, relative to the <i>bounds</i>
	 * @return			A <code>Rectangle</code> representing the bounds consumed by the text
	 */
	public static Rectangle drawString(Graphics g, String text, Font font, Color color, Rectangle bounds, TextAlignment align) {
		return drawString(g, text, font, color, bounds, align, TextFormat.NONE);
	}
	
	/**
	 * Draws a string onto a <code>Graphics</code> handle, using a <code>Font</code>, <code>Color</code> and target bounds to calculate
	 * the location and automatic wrapping of text. The <i>align</i> property determines where the text will be positioned.
	 * 
	 * @param g			A <code>Graphics</code> handle which is the target of the draw operation
	 * @param text		A <code>String</code> containing the text to draw
	 * @param font		The <code>Font</code> to use when drawing the text
	 * @param color		The <code>Color</code> to use when drawing the text
	 * @param bounds	A <code>Rectangle</code> representing the bounds of the text
	 * @param align		A <code>TextAlignment</code> value representing the location to draw the text, relative to the <i>bounds</i>
	 * @param format	Additional formatting flags to use when drawing (see <code>TextFormat</code> class)
	 * @return			A <code>Rectangle</code> representing the bounds consumed by the text
	 */
	public static Rectangle drawString(Graphics g, String text, Font font, Color color, Rectangle bounds, TextAlignment align, int format)
	{
		if (g == null)
			throw new NullPointerException("The graphics handle cannot be null.");
		if (text == null)
			throw new NullPointerException("The text cannot be null.");
		if (font == null)
			throw new NullPointerException("The font cannot be null.");
		if (color == null)
			throw new NullPointerException("The text color cannot be null.");
		if (bounds == null)
			throw new NullPointerException("The text bounds cannot be null.");
		if (align == null)
			throw new NullPointerException("The text alignment cannot be null.");
		if (text.length() == 0)
			return new Rectangle(bounds.x, bounds.y, 0, 0);
		
		Graphics2D g2D = (Graphics2D)g;
		
		AttributedString attributedString = new AttributedString(text);
		attributedString.addAttribute(TextAttribute.FOREGROUND, color);
		attributedString.addAttribute(TextAttribute.FONT, font);
		
		AttributedCharacterIterator attributedCharIterator = attributedString.getIterator();		
		
		FontRenderContext fontContext = new FontRenderContext(null, !TextFormat.isEnabled(format, TextFormat.NO_ANTI_ALIASING), false);
		LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(attributedCharIterator, fontContext);
		
		Point targetLocation = new Point(bounds.x, bounds.y);
		int nextOffset = 0;
		
		if (align.isMiddle() || align.isBottom())
		{
			if (align.isMiddle())
				targetLocation.y = bounds.y + (bounds.height / 2);
			if (align.isBottom())
				targetLocation.y = bounds.y + bounds.height;
			
			while (lineMeasurer.getPosition() < text.length())
			{
				nextOffset = lineMeasurer.nextOffset(bounds.width);
				nextOffset = nextTextIndex(nextOffset, lineMeasurer.getPosition(), text);
				
				TextLayout textLayout = lineMeasurer.nextLayout(bounds.width, nextOffset, false);
				
				if (align.isMiddle())
					targetLocation.y -= (textLayout.getAscent() + textLayout.getLeading() + textLayout.getDescent()) / 2;
				if (align.isBottom())
					targetLocation.y -= (textLayout.getAscent() + textLayout.getLeading() + textLayout.getDescent());
			}
			
			if (TextFormat.isEnabled(format, TextFormat.FIRST_LINE_VISIBLE))
				targetLocation.y = Math.max(0, targetLocation.y);
			
			lineMeasurer.setPosition(0);
		}
		
		if (align.isRight() || align.isCenter())
			targetLocation.x = bounds.x + bounds.width;
		
		Rectangle consumedBounds = new Rectangle(targetLocation.x, targetLocation.y, 0, 0);
					
		while (lineMeasurer.getPosition() < text.length())
		{				
			nextOffset = lineMeasurer.nextOffset(bounds.width);				
			nextOffset = nextTextIndex(nextOffset, lineMeasurer.getPosition(), text);
			
			TextLayout textLayout = lineMeasurer.nextLayout(bounds.width, nextOffset, false);			
			Rectangle2D textBounds = textLayout.getBounds();
			
			targetLocation.y += textLayout.getAscent();
			consumedBounds.width = Math.max(consumedBounds.width, (int)textBounds.getWidth());
			
			switch (align)
			{
				case TOP_LEFT:
				case MIDDLE_LEFT:
				case BOTTOM_LEFT:
					textLayout.draw(g2D, targetLocation.x, targetLocation.y);
					break;
					
				case TOP:
				case MIDDLE:
				case BOTTOM:
					targetLocation.x = bounds.x + (bounds.width / 2) - (int)(textBounds.getWidth() / 2);
					consumedBounds.x = Math.min(consumedBounds.x, targetLocation.x);
					textLayout.draw(g2D, targetLocation.x, targetLocation.y);
					break;
					
				case TOP_RIGHT:
				case MIDDLE_RIGHT:
				case BOTTOM_RIGHT:
					targetLocation.x = bounds.x + bounds.width - (int)textBounds.getWidth();
					textLayout.draw(g2D, targetLocation.x, targetLocation.y);
					consumedBounds.x = Math.min(consumedBounds.x, targetLocation.x);
					break;
			}
						
			targetLocation.y += textLayout.getLeading() + textLayout.getDescent();
		}
		
		consumedBounds.height = targetLocation.y - consumedBounds.y;
		
		return consumedBounds;
	}
	
	/**
	 * Calculates the next maximum index of the string that will be displayed.
	 * 
	 * @param nextOffset		The index calculated using a <code>LineBreakMeasurer</code> <i>nextOffset</i> method
	 * @param measurerPosition	The position within a <code>LineBreakMeasurer</code>
	 * @param text				The text being rendered
	 * @return					The next maximum index within the string
	 */
	private static int nextTextIndex(int nextOffset, int measurerPosition, String text)
	{
		for (int i = measurerPosition + 1; i < nextOffset; ++i)
		{
			if (text.charAt(i) == '\n')
				return i;
		}
		
		return nextOffset;
	}
}
