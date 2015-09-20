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
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";

	public GrepLogic(String pattern, boolean asWord, boolean caseSensitive, int linesAfter) {
		this.pattern = buildPattern(pattern, asWord, caseSensitive);
		this.linesAfter = linesAfter;
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
					outStream.println(m.replaceAll(ANSI_RED + "$0" + ANSI_RESET));
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

}
