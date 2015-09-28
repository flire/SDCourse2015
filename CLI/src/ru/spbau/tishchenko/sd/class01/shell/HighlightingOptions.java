package ru.spbau.tishchenko.sd.class01.shell;

public class HighlightingOptions {
	public final String startMarker;
	public final String endMarker;
	public final boolean isEnabled;
	
	public HighlightingOptions(String start, String end) {
		this(start, end, true);
	}
	
	private HighlightingOptions(String start, String end, boolean isEnabled) {
		startMarker = start;
		endMarker = end;
		this.isEnabled = isEnabled;
	}
	
	public static final HighlightingOptions DISABLED = new HighlightingOptions("", "", false);
}
