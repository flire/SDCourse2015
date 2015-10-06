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
	private HighlightingOptions highlightingOptions;
	private final Options options;
	
	@Deprecated
	public GrepLogic(String pattern, boolean asWord, boolean caseSensitive, int linesAfter) {
		this(pattern, new Options(asWord, caseSensitive, linesAfter, linesAfter != 0), HighlightingOptions.NO_HIGHLIGHTING);
	}

	public GrepLogic(String pattern, Options options, HighlightingOptions highlighingOptions) {
		this.pattern = buildPattern(pattern, options.asWord, options.caseSensitive);
		this.options = options;
		this.highlightingOptions = highlighingOptions;
	}
	
	public void execute(InputStream in, OutputStream out) {
		PrintStream outStream = new PrintStream(out);
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			String currentLine;
			int following = -1; //works as flag: when 0, you need to insert lineSeparator
			while ((currentLine = reader.readLine()) != null) {
				Matcher m = pattern.matcher(currentLine);
				if (m.find()) {
					following = options.linesAfter;
					outStream.println(m.replaceAll(highlightingOptions.startMarker
							+ "$0"
							+ highlightingOptions.endMarker));
				} else if (following > 0) {
					following--;
					outStream.println(currentLine);
				} else if (following == 0 && options.useSkipSeparator) {
					outStream.println(options.skipSeparator);
					following = -1;
				}
			}
		} catch (IOException e) {
			outStream.println("IO exception while reading input: "+e.getMessage());
		}
	}

	private Pattern buildPattern(String pattern, boolean asWord, boolean caseSensitive) {
		if (asWord) {
			pattern = "\\b" + pattern.replaceAll("^\b", "").replaceAll("\b$", "") + "\\b";
		}
		if (caseSensitive) {
			return Pattern.compile(pattern);
		}
		return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
	}
	
	public static class Options {
		private static final String DEFAULT_LINE_SEPARATOR = "--";
		public final boolean asWord;
		public final boolean caseSensitive;
		public final int linesAfter;
		public final String skipSeparator;
		public final boolean useSkipSeparator;

		public Options(boolean asWord, boolean caseSensitive, int linesAfter, boolean useSkipSeparator) {
			this(asWord, caseSensitive, linesAfter, useSkipSeparator, DEFAULT_LINE_SEPARATOR);
		}

		public Options(boolean asWord, boolean caseSensitive, int linesAfter, boolean useSkipSeparator,
				String skipSeparator) {
			this.asWord = asWord;
			this.caseSensitive = caseSensitive;
			this.linesAfter = linesAfter;
			this.useSkipSeparator = useSkipSeparator;
			this.skipSeparator = skipSeparator;
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
