package ru.spbau.tishchenko.sd.class01.commands.grep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrepLogic {
	private Pattern pattern;
	private int linesAfter;
	private HighlightingOptions highlightingOptions;
	
	public GrepLogic(String pattern, boolean asWord, boolean caseSensitive, int linesAfter) {
		this(pattern, new Options(asWord, caseSensitive, linesAfter), HighlightingOptions.NO_HIGHLIGHTING);
	}

	public GrepLogic(String pattern, Options options, HighlightingOptions highlighingOptions) {
		this.pattern = buildPattern(pattern, options.asWord, options.caseSensitive);
		this.linesAfter = options.linesAfter;
		this.highlightingOptions = highlighingOptions;
	}
	
	public void execute(InputStream in, OutputStream out) {
		PrintStream outStream = new PrintStream(out);
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			String currentLine;
			int following = 0;
			while ((currentLine = reader.readLine()) != null) {
				Matcher m = pattern.matcher(currentLine);
				if (m.find()) {
					following = linesAfter;
					outStream.println(m.replaceAll(highlightingOptions.startMarker
							+ "$0"
							+ highlightingOptions.endMarker));
				} else if (following > 0) {
					following--;
					outStream.println(currentLine);
				}
			}
		} catch (IOException e) {
			outStream.println("IO exception while reading input: "+e.getMessage());
		}
	}

	private Pattern buildPattern(String pattern, boolean asWord, boolean caseSensitive) {
		if (asWord) {
			pattern = "\b" + pattern.replaceAll("^\b", "").replaceAll("\b$", "") + "\b";
		}
		if (caseSensitive) {
			return Pattern.compile(pattern);
		}
		return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
	}
	
	public static class Options {
		public final boolean asWord;
		public final boolean caseSensitive;
		public final int linesAfter;

		public Options(boolean asWord, boolean caseSensitive, int linesAfter) {
			this.asWord = asWord;
			this.caseSensitive = caseSensitive;
			this.linesAfter = linesAfter;
		}
	}
	
	public static class HighlightingOptions {
		public final String startMarker;
		public final String endMarker;
		
		public HighlightingOptions(String startMarker, String endMarker) {
			this.startMarker = startMarker;
			this.endMarker = endMarker;
		}
		
		public static final HighlightingOptions NO_HIGHLIGHTING = new HighlightingOptions("", "");
	}

}
