package org.eclipse.swt.custom;
/*
 * (c) Copyright IBM Corp. 2002.
 * All Rights Reserved
 */

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.StyledText.*;
import org.eclipse.swt.graphics.*;

/**
 * Created by StyledText to handle a single invalidate (i.e., Paint event).
 */
abstract class AbstractRenderer {
	private Device device;
	private Font regularFont;
	private Font boldFont;
	private boolean isBidi;
	private int leftMargin;
	private int tabLength;
	private int tabWidth;						// width of a tab character in the current GC
	private int lineHeight;
	private int lineEndSpaceWidth;
	
AbstractRenderer(Device device, Font regularFont, boolean isBidi, int leftMargin) {
	FontData fontData = regularFont.getFontData()[0];

	fontData.setStyle(fontData.getStyle() | SWT.BOLD);
	boldFont = new Font(device, fontData);
	this.device = device;
	this.regularFont = regularFont;	
	this.isBidi = isBidi;
	this.leftMargin = leftMargin;
}
/**
 * Returns the width of the specified text. 
 * <p>
 *
 * @param text text to be measured.
 * @param startOffset offset of the character to start measuring and 
 * 	expand tabs.
 * @param length number of characters to measure. Tabs are counted 
 * 	as one character in this parameter.
 * @param startXOffset x position of "startOffset" in "text". Used for
 * 	calculating tab stops
 * @param bidi the bidi object to use for measuring text in bidi locales. 
 * @return width of the text with tabs expanded to tab stops or 0 if the 
 * 	startOffset or length is outside the specified text.
 */
int bidiTextWidth(String text, int startOffset, int length, int startXOffset, StyledTextBidi bidi) {
	int endOffset = startOffset + length;
	int textLength = text.length();
	
	if (startOffset < 0 || startOffset >= textLength || endOffset > textLength) {
		return 0;
	}
	// Use lastCaretDirection in order to get same results as during
	// caret positioning (setBidiCaretLocation). Fixes 1GKU4C5.
	return bidi.getCaretPosition(endOffset, getLastCaretDirection()) - startXOffset;
}
/**
 * Calculates the line height
 */
void calculateLineHeight() {
	GC gc = getGC();
	
	lineHeight = gc.getFontMetrics().getHeight();	
	lineEndSpaceWidth = gc.stringExtent(" ").x;
	disposeGC(gc);
}
void dispose() {
	if (boldFont != null) {
		boldFont.dispose();
		boldFont = null;
	}
}
/** 
 * Draws a line of text at the specified location.
 * <p>
 *
 * @param line the line to draw
 * @param lineIndex	index of the line to draw
 * @param paintY y location to draw at
 * @param gc GC to draw on
 * @param widgetBackground the widget background color. 
 * 	Used as the default rendering color.
 * @param widgetForeground the widget foreground color. 
 * 	Used as the default rendering color. 
 * @param currentFont the font currently set in gc. Cached for better performance.
 */
void drawLine(String line, int lineIndex, int paintY, GC gc, Color widgetBackground, Color widgetForeground, FontData currentFont, boolean clearBackground) {
	int lineOffset = getContent().getOffsetAtLine(lineIndex);
	int lineLength = line.length();
	Point selection = getSelection();
	int selectionStart = selection.x;
	int selectionEnd = selection.y;
	StyleRange[] styles = new StyleRange[0];
	Color lineBackground = null;
	StyledTextEvent event = getLineStyleData(lineOffset, line);
	StyledTextBidi bidi = null;
	
	if (event != null) {
		styles = event.styles;
	}
	if (isBidi()) {
		setLineFont(gc, currentFont, SWT.NORMAL);
		bidi = getStyledTextBidi(line, lineOffset, gc, styles);
	}
	event = getLineBackgroundData(lineOffset, line);
	if (event != null) {
		lineBackground = event.lineBackground;
	}
	if (lineBackground == null) {
		lineBackground = widgetBackground;
	}
	if (clearBackground &&
		(isFullLineSelection() == false || 
		 selectionStart > lineOffset || 
		 selectionEnd <= lineOffset + lineLength)) {
		// draw background if full selection is off or if line is not 
		// completely selected
		gc.setBackground(lineBackground);
		gc.setForeground(lineBackground);
		gc.fillRectangle(leftMargin, paintY, getClientArea().width, lineHeight);
	}
	if (selectionStart != selectionEnd) {
		drawLineSelectionBackground(line, lineOffset, styles, paintY, gc, currentFont, bidi);
	}
	if (selectionStart != selectionEnd && 
		((selectionStart >= lineOffset && selectionStart < lineOffset + lineLength) || 
		 (selectionStart < lineOffset && selectionEnd > lineOffset))) {
		styles = getSelectionLineStyles(styles);
	}
	if (isBidi()) {
		int paintX = bidiTextWidth(line, 0, 0, 0, bidi);
		drawStyledLine(line, lineOffset, 0, styles, paintX, paintY, gc, lineBackground, widgetForeground, currentFont, bidi);
	}
	else {
		drawStyledLine(line, lineOffset, 0, styles, 0, paintY, gc, lineBackground, widgetForeground, currentFont, bidi);
	}
}

/** 
 * Draws the background of the line selection.
 * <p>
 *
 * @param line the line to draw
 * @param lineOffset offset of the first character in the line.
 * 	Relative to the start of the document.
 * @param styles line styles
 * @param paintY y location to draw at
 * @param gc GC to draw on
 * @param currentFont the font currently set in gc. Cached for 
 * 	better performance.
 * @param bidi the bidi object to use for measuring and rendering 
 * 	text in bidi locales. null when not in bidi mode.
 */
protected abstract void drawLineSelectionBackground(String line, int lineOffset, StyleRange[] styles, int paintY, GC gc, FontData currentFont, StyledTextBidi bidi);

/** 
 * Draws the line at the specified location.
 * <p>
 *
 * @param line the line to draw
 * @param lineOffset offset of the first character in the line.
 * 	Relative to the start of the document.
 * @param renderOffset offset of the first character that should 
 * 	be rendered. Relative to the start of the line.
 * @param styles the styles to use for rendering line segments. 
 * 	May be empty but not null.
 * @param paintX x location to draw at, not used in bidi mode
 * @param paintY y location to draw at
 * @param gc GC to draw on
 * @param lineBackground line background color, used when no style 
 * 	is specified for a line segment.
 * @param lineForeground line foreground color, used when no style 
 * 	is specified for a line segment.
 * @param currentFont the font currently set in gc. Cached for better 
 * 	performance.
 * @param bidi the bidi object to use for measuring and rendering 
 * 	text in bidi locales. null when not in bidi mode.
 */
private void drawStyledLine(String line, int lineOffset, int renderOffset, StyleRange[] styles, int paintX, int paintY, GC gc, Color lineBackground, Color lineForeground, FontData currentFont, StyledTextBidi bidi) {
	int lineLength = line.length();
	int horizontalScrollOffset = getHorizontalPixel();
	Color background = gc.getBackground();
	Color foreground = gc.getForeground();	
	StyleRange style = null;
	StyleRange[] filteredStyles = filterLineStyles(styles);	
	int renderStopX = getClientArea().width + horizontalScrollOffset;
		
	// Always render the entire line when in a bidi locale.
	// Since we render the line in logical order we may start past the end
	// of the visual right border of the client area and work towards the
	// left.
	for (int i = 0; i < styles.length && (paintX < renderStopX || bidi != null); i++) {
		int styleLineLength;
		int styleLineStart;
		int styleLineEnd;
		style = styles[i];
		styleLineEnd = style.start + style.length - lineOffset;
		styleLineStart = Math.max(style.start - lineOffset, 0);
		// render unstyled text between the start of the current 
		// style range and the end of the previously rendered 
		// style range
		if (styleLineStart > renderOffset) {
			background = setLineBackground(gc, background, lineBackground);
			foreground = setLineForeground(gc, foreground, lineForeground);
			setLineFont(gc, currentFont, SWT.NORMAL);			
			// don't try to render more text than requested
			styleLineStart = Math.min(lineLength, styleLineStart);
			paintX = drawText(line, renderOffset, styleLineStart - renderOffset, paintX, paintY, gc, bidi);
			renderOffset = styleLineStart;
		}
		else
		if (styleLineEnd <= renderOffset) {
			// style ends before render start offset
			// skip to the next style
			continue;
		}
		if (styleLineStart >= lineLength) {
			// there are line styles but no text for those styles
			// possible when called with partial line text
			break;
		}		
		styleLineLength = Math.min(styleLineEnd, lineLength) - renderOffset;
		// set style background color if specified
		if (style.background != null) {
			background = setLineBackground(gc, background, style.background);
			foreground = setLineForeground(gc, foreground, style.background);
			if (bidi != null) {
				bidi.fillBackground(renderOffset, styleLineLength, leftMargin - horizontalScrollOffset, paintY, lineHeight);
			}
			else {
				int fillWidth = textWidth(line, lineOffset, renderOffset, styleLineLength, filteredStyles, paintX, gc, currentFont);
				gc.fillRectangle(paintX - horizontalScrollOffset + leftMargin, paintY, fillWidth, lineHeight);
			}
		}
		else {
			background = setLineBackground(gc, background, lineBackground);
		}
		// set style foreground color if specified
		if (style.foreground != null) {
			foreground = setLineForeground(gc, foreground, style.foreground);
		}
		else {
			foreground = setLineForeground(gc, foreground, lineForeground);
		}
		setLineFont(gc, currentFont, style.fontStyle);
		paintX = drawText(line, renderOffset, styleLineLength, paintX, paintY, gc, bidi);
		renderOffset += styleLineLength;
	}
	// render unstyled text at the end of the line
	if ((style == null || renderOffset < lineLength) && 
		(paintX < renderStopX || bidi != null)) {
		setLineBackground(gc, background, lineBackground);
		setLineForeground(gc, foreground, lineForeground);
		setLineFont(gc, currentFont, SWT.NORMAL);
		drawText(line, renderOffset, lineLength - renderOffset, paintX, paintY, gc, bidi);
	}	
}

/**
 * Draws the text at the specified location. Expands tabs to tab 
 * stops using the widget tab width.
 * <p>
 *
 * @param text text to draw 
 * @param startOffset offset of the first character in text to draw 
 * @param length number of characters to draw
 * @param paintX x location to start drawing at, not used in bidi mode
 * @param paintY y location to draw at. Unused when draw is false
 * @param gc GC to draw on
 * 	location where drawing would stop
 * @param bidi the bidi object to use for measuring and rendering 
 * 	text in bidi locales. null when not in bidi mode.
 * @return x location where drawing stopped or 0 if the startOffset or 
 * 	length is outside the specified text. In bidi mode this value is 
 * 	the same as the paintX input parameter.
 */
private int drawText(String text, int startOffset, int length, int paintX, int paintY, GC gc, StyledTextBidi bidi) {
	int endOffset = startOffset + length;
	int textLength = text.length();
	int horizontalScrollOffset = getHorizontalPixel();
	
	if (startOffset < 0 || startOffset >= textLength || startOffset + length > textLength) {
		return paintX;
	}
	for (int i = startOffset; i < endOffset; i++) {
		int tabIndex = text.indexOf(StyledText.TAB, i);
		// is tab not present or past the rendering range?
		if (tabIndex == -1 || tabIndex > endOffset) {
			tabIndex = endOffset;
		}
		if (tabIndex != i) {
			String tabSegment = text.substring(i, tabIndex);
			if (bidi != null) {
				bidi.drawBidiText(i, tabIndex - i, leftMargin - horizontalScrollOffset, paintY);
			}
			else {
				gc.drawString(tabSegment, paintX - horizontalScrollOffset + leftMargin, paintY, true);
				paintX += gc.stringExtent(tabSegment).x;
				if (tabIndex != endOffset && tabWidth > 0) {
					paintX = getTabStop(paintX);
				}
			}
			i = tabIndex;
		}
		else 		// is tab at current rendering offset?
		if (tabWidth > 0 && isBidi() == false) {
			paintX = getTabStop(paintX);
		}
	}
	return paintX;
}
/** 
 * Filter the given style ranges based on the font style and 
 * return the unchanged styles only if there is at least one 
 * non-regular (e.g., bold) font.
 * <p>
 * 
 * @param styles styles that may contain font styles.
 * @return null if the styles contain only regular font styles, the 
 * 	unchanged styles otherwise.
 */
StyleRange[] filterLineStyles(StyleRange[] styles) {
	if (styles != null) {
		int styleIndex = 0;
		while (styleIndex < styles.length && styles[styleIndex].fontStyle == SWT.NORMAL) {
			styleIndex++;
		}
		if (styleIndex == styles.length) {
			styles = null;
		}
	}
	return styles;
}
/**
 */
protected abstract Rectangle getClientArea();
/**
 */
protected abstract StyledTextContent getContent();

Device getDevice() {
	return device;
}
/**
 * Returns an array of text ranges that have a font style specified (e.g., SWT.BOLD).
 * <p>
 * @param styles style ranges in the line
 * @param lineOffset start index of the line, relative to the start of the document
 * @param length of the line
 * @return StyleRange[], array of ranges with a font style specified, 
 * null if styles parameter is null
 */
StyleRange[] getFontStyleRanges(StyleRange[] styles, int lineOffset, int lineLength) {
	int count = 0;
	StyleRange[] ranges = null;
	
	if (styles == null) {
		return null;
	}
	// figure out the number of ranges with font styles
	for (int i = 0; i < styles.length; i++) {
		StyleRange style = styles[i];
		if (style.start - lineOffset < lineLength) {
			if (style.fontStyle == SWT.BOLD) {
				count++;
			}
		}
	}
	// get the style information
	if (count > 0) {
		ranges = new StyleRange[count];
		count = 0;
		for (int i = 0; i < styles.length; i++) {
			StyleRange style = styles[i];
			int styleLineStart = style.start - lineOffset;
			if (styleLineStart < lineLength) {			
				if (style.fontStyle == SWT.BOLD) {
					StyleRange newStyle = new StyleRange();
					newStyle.start = Math.max(0, styleLineStart);
					newStyle.length = (Math.min(styleLineStart + style.length, lineLength)) - newStyle.start;
					ranges[count] = newStyle;
					count++;
				}
			}		
		}
	}
	return ranges;
}
protected abstract void disposeGC(GC gc);
/**
 * Returns the text segments that should be treated as if they 
 * had a different direction than the surrounding text.
 * <p>
 *
 * @param lineOffset offset of the first character in the line. 
 * 	0 based from the beginning of the document.
 * @param line text of the line to specify bidi segments for
 * @return text segments that should be treated as if they had a
 * 	different direction than the surrounding text. Only the start 
 * 	index of a segment is specified, relative to the start of the 
 * 	line. Always starts with 0 and ends with the line length. 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the segment indices returned 
 * 		by the listener do not start with 0, are not in ascending order,
 * 		exceed the line length or have duplicates</li>
 * </ul>
 */
protected abstract int[] getBidiSegments(int lineOffset, String lineText);
protected abstract GC getGC();
protected abstract int getHorizontalPixel();

int getLineEndSpaceWidth() {
	return lineEndSpaceWidth;
}
protected abstract int getLastCaretDirection();
int getLeftMargin() {
	return leftMargin;
}
/**
 * Returns the line background data for the given line or null if 
 * there is none.
 * <p>
 * @param lineOffset offset of the line start relative to the start
 * 	of the content.
 * @param line line to get line background data for
 * @return line background data for the given line.
 */
protected abstract StyledTextEvent getLineBackgroundData(int lineOffset, String line);

int getLineHeight() {
	return lineHeight;
}
/**
 * Returns the line style data for the given line or null if there is 
 * none. If there is a LineStyleListener but it does not set any styles, 
 * the StyledTextEvent.styles field will be initialized to an empty 
 * array.
 * <p>
 * 
 * @param lineOffset offset of the line start relative to the start of 
 * 	the content.
 * @param line line to get line styles for
 * @return line style data for the given line. Styles may start before 
 * 	line start and end after line end
 */
StyledTextEvent getLineStyleData(StyledTextEvent event, int lineOffset, String line) {
	int lineLength = line.length();
	
	if (event.styles != null && getWordWrap()) {
		event.styles = getVisualLineStyleData(event.styles, lineOffset, lineLength);
	}
	if (event.styles == null) {
		event.styles = new StyleRange[0];
	}
	else
	if (isBidi()) {
		GC gc = getGC();
		if (StyledTextBidi.isLigated(gc)) {
			// Check for ligatures that are partially styled, if one is found
			// automatically apply the style to the entire ligature.
			// Since ligatures can't extend over multiple lines (they aren't 
			// ligatures if they are separated by a line delimiter) we can ignore
			// style starts or ends that are not on the current line.
			// Note that there is no need to deal with segments when checking for
			// the ligatures.
			StyledTextBidi bidi = new StyledTextBidi(gc, line, new int[] {0, lineLength});
			for (int i=0; i<event.styles.length; i++) {
				StyleRange range = event.styles[i];
				StyleRange newRange = null;
				int relativeStart = range.start - lineOffset;
				if (relativeStart >= 0) {
					int startLigature = bidi.getLigatureStartOffset(relativeStart);
					if (startLigature != relativeStart) {
						newRange = (StyleRange) range.clone();
						range = event.styles[i] = newRange;
						range.start = range.start - (relativeStart - startLigature);
						range.length = range.length + (relativeStart - startLigature);
					}
				}
				int rangeEnd = range.start + range.length;
				int relativeEnd = rangeEnd - lineOffset - 1;
				if (relativeEnd < lineLength) {
					int endLigature = bidi.getLigatureEndOffset(relativeEnd);
					if (endLigature != relativeEnd) {
						if (newRange == null) {
							newRange = (StyleRange) range.clone();
							range = event.styles[i] = newRange;
						}
						range.length = range.length + (endLigature - relativeEnd);
					}
				}
	        }
	    }
	    disposeGC(gc);
	}
	return event;
}
/**
 * Returns the line style data for the given line or null if there is 
 * none. If there is a LineStyleListener but it does not set any styles, 
 * the StyledTextEvent.styles field will be initialized to an empty 
 * array.
 * <p>
 * 
 * @param lineOffset offset of the line start relative to the start of 
 * 	the content.
 * @param line line to get line styles for
 * @return line style data for the given line. Styles may start before 
 * 	line start and end after line end
 */
protected abstract StyledTextEvent getLineStyleData(int lineOffset, String line);
protected abstract Point getSelection();
/**
 * Merges the selection into the styles that are passed in.
 * The font style of existing style ranges is preserved in the selection.
 * <p>
 * @param styles the existing styles that the selection should be 
 * 	applied to.
 * @return the selection style range merged with the existing styles
 */
protected abstract StyleRange[] getSelectionLineStyles(StyleRange[] styles);
/**
 * Returns a StyledTextBidi object for the specified line.
 * <p>
 * 
 * @param lineText the line that the StyledTextBidi object should 
 * 	work on.
 * @param lineOffset offset of the beginning of the line, relative 
 * 	to the beginning of the document
 * @param gc GC to use when creating a new StyledTextBidi object.
 * @param styles StyleRanges to use when creating a new StyledTextBidi 
 * 	object.
 * @return a StyledTextBidi object for the specified line.
 */
StyledTextBidi getStyledTextBidi(String lineText, int lineOffset, GC gc, StyleRange[] styles) {
	StyleRange[] fontStyles = null;
	
	if (styles == null) {
		StyledTextEvent event = getLineStyleData(lineOffset, lineText);
		if (event != null) {
			fontStyles = getFontStyleRanges(event.styles, lineOffset, lineText.length());
		}
	}
	else {
		fontStyles = getFontStyleRanges(styles, lineOffset, lineText.length());
	}
	return new StyledTextBidi(gc, tabWidth, lineText, fontStyles, boldFont, getBidiSegments(lineOffset, lineText));
}	
/**
 * Returns the next tab stop for the specified x location.
 * <p>
 *
 * @param x the x location in front of a tab
 * @return the next tab stop for the specified x location.
 */
private int getTabStop(int x) {
	int spaceWidth = tabWidth / tabLength;

	// make sure tab stop is at least one space width apart 
	// from the last character. fixes 4844.
	if (tabWidth - x % tabWidth < spaceWidth) {
		x += tabWidth;
	}
	x += tabWidth;
	x -= x % tabWidth;
	return x;
}
/**
 * Returns styles for the specified visual (wrapped) line.
 * <p>
 * 
 * @param logicalStyles the styles for a logical (unwrapped) line
 * @param lineOffset offset of the visual line
 * @param lineLength length of the visual line
 * @return styles in the logicalStyles array that are at least 
 * 	partially on the specified visual line.
 */
StyleRange[] getVisualLineStyleData(StyleRange[] logicalStyles, int lineOffset, int lineLength) {
	int lineEnd = lineOffset + lineLength;
	int oldStyleCount = logicalStyles.length;
	int newStyleCount = 0;
	
	for (int i = 0; i < oldStyleCount; i++) {
		StyleRange style = logicalStyles[i];
		if (style.start < lineEnd && style.start + style.length > lineOffset) {
			newStyleCount++;
		}
	}
	if (newStyleCount != oldStyleCount) {
		StyleRange[] newStyles = new StyleRange[newStyleCount];
		for (int i = 0, j = 0; i < oldStyleCount; i++) {
			StyleRange style = logicalStyles[i];
			if (style.start < lineEnd && style.start + style.length > lineOffset) {
				newStyles[j++] = logicalStyles[i];						
			}
		}
		logicalStyles = newStyles;
	}
	return logicalStyles;
}
protected abstract boolean getWordWrap();
/**
 * Temporary until SWT provides this
 */
boolean isBidi() {
	return isBidi;
}
protected abstract boolean isFullLineSelection();
/** 
 * Sets the background of the specified GC for a line rendering operation,
 * if it is not already set.
 * </p>
 *
 * @param gc GC to set the background color in
 * @param currentBackground background color currently set in gc
 * @param newBackground new background color of gc
 */
private Color setLineBackground(GC gc, Color currentBackground, Color newBackground) {
	if (currentBackground.equals(newBackground) == false) {
		gc.setBackground(newBackground);
	}
	return newBackground;	
}
/** 
 * Sets the font of the specified GC if it is not already set.
 * </p>
 *
 * @param gc GC to set the font in
 * @param currentFont font data of font currently set in gc
 * @param style desired style of the font in gc. Can be one of 
 * 	SWT.NORMAL, SWT.BOLD
 */
private void setLineFont(GC gc, FontData currentFont, int style) {
	if (currentFont.getStyle() != style) {
		if (style == SWT.BOLD) {
			currentFont.setStyle(style);
			gc.setFont(boldFont);
		}
		else
		if (style == SWT.NORMAL) {
			currentFont.setStyle(style);
			gc.setFont(regularFont);
		}
	}
}
/** 
 * Sets the foreground of the specified GC for a line rendering operation,
 * if it is not already set.
 * </p>
 *
 * @param gc GC to set the foreground color in
 * @param currentForeground	foreground color currently set in gc
 * @param newForeground new foreground color of gc
 */
private Color setLineForeground(GC gc, Color currentForeground, Color newForeground) {
	if (currentForeground.equals(newForeground) == false) {
		gc.setForeground(newForeground);
	}
	return newForeground;
}
/**
 * Calculates the width in pixel of a tab character
 */
void setTabLength(int tabLength) {
	GC gc = getGC();
	StringBuffer tabBuffer = new StringBuffer(tabLength);
	
	this.tabLength = tabLength;
	for (int i = 0; i < tabLength; i++) {
		tabBuffer.append(' ');
	}
	tabWidth = gc.stringExtent(tabBuffer.toString()).x;
	disposeGC(gc);
}
/**
 * Measures the text as rendered at the specified location. Expand tabs to tab stops using
 * the widget tab width.
 * <p>
 *
 * @param text text to draw 
 * @param textStartOffset offset of the first character in text relative 
 * 	to the first character in the document
 * @param lineStyles styles of the line
 * @param paintX x location to start drawing at
 * @param gc GC to draw on
 * @param fontData the font data of the font currently set in gc
 * @return x location where drawing stopped or 0 if the startOffset or 
 * 	length is outside the specified text.
 */
private int styledTextWidth(String text, int textStartOffset, StyleRange[] lineStyles, int paintX, GC gc, FontData fontData) {
	String textSegment;
	int textLength = text.length();
	int textIndex = 0;

	for (int styleIndex = 0; styleIndex < lineStyles.length; styleIndex++) {
		StyleRange style = lineStyles[styleIndex];
		int textEnd;
		int styleSegmentStart = style.start - textStartOffset;
		if (styleSegmentStart + style.length < 0) {
			continue;
		}
		if (styleSegmentStart >= textLength) {
			break;
		}
		// is there a style for the current string position?
		if (textIndex < styleSegmentStart) {
			setLineFont(gc, fontData, SWT.NORMAL);
			textSegment = text.substring(textIndex, styleSegmentStart);
			paintX += gc.stringExtent(textSegment).x;
			textIndex = styleSegmentStart;
		}
		textEnd = Math.min(textLength, styleSegmentStart + style.length);
		setLineFont(gc, fontData, style.fontStyle);
		textSegment = text.substring(textIndex, textEnd);
		paintX += gc.stringExtent(textSegment).x;
		textIndex = textEnd;
	}
	// is there unmeasured and unstyled text?
	if (textIndex < textLength) {
		setLineFont(gc, fontData, SWT.NORMAL);
		textSegment = text.substring(textIndex, textLength);
		paintX += gc.stringExtent(textSegment).x;
	}
	return paintX;
}
/**
 * Returns the width of the specified text. Expand tabs to tab stops using
 * the widget tab width.
 * <p>
 *
 * @param text text to be measured.
 * @param lineOffset offset of the first character in the line. 
 * @param startOffset offset of the character to start measuring and 
 * 	expand tabs.
 * @param length number of characters to measure. Tabs are counted 
 * 	as one character in this parameter.
 * @param styles line styles
 * @param startXOffset x position of "startOffset" in "text". Used for
 * 	calculating tab stops
 * @param gc GC to use for measuring text
 * @param fontData the font currently set in gc. Cached for better performance.
 * @return width of the text with tabs expanded to tab stops or 0 if the 
 * 	startOffset or length is outside the specified text.
 */
int textWidth(String text, int lineOffset, int startOffset, int length, StyleRange[] lineStyles, int startXOffset, GC gc, FontData fontData) {
	int paintX = 0;
	int endOffset = startOffset + length;
	int textLength = text.length();
	
	if (startOffset < 0 || startOffset >= textLength || endOffset > textLength) {
		return paintX;
	}
	for (int i = startOffset; i < endOffset; i++) {
		int tabIndex = text.indexOf(StyledText.TAB, i);
		// is tab not present or past the rendering range?
		if (tabIndex == -1 || tabIndex > endOffset) {
			tabIndex = endOffset;
		}
		if (tabIndex != i) {
			String tabSegment = text.substring(i, tabIndex);
			if (lineStyles != null) {
				paintX = styledTextWidth(tabSegment, lineOffset + i, lineStyles, paintX, gc, fontData);
			}
			else {
				setLineFont(gc, fontData, SWT.NORMAL);
				paintX += gc.stringExtent(tabSegment).x;
			}
			if (tabIndex != endOffset && tabWidth > 0) {
				paintX = getTabStop(startXOffset + paintX) - startXOffset;
			}
			i = tabIndex;
		}
		else 		
		if (tabWidth > 0) {
			paintX = getTabStop(startXOffset + paintX) - startXOffset;
		}
	}
	return paintX;
}


}
