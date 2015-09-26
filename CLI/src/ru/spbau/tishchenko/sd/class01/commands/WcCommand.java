package ru.spbau.tishchenko.sd.class01.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import ru.spbau.tishchenko.sd.class01.shell.IShell;

public class WcCommand implements ICommand {

	@Override
	public void execute(IShell shell, String[] args, InputStream in, OutputStream out) {
		PrintStream outStream = new PrintStream(out);
		WcOptions options = new WcOptions(args);
		if (options.filename != null) {
			File fileToRead = new File(shell.getCurrentDir(), options.filename);
			try {
				in = new FileInputStream(fileToRead);
			} catch (FileNotFoundException e) {
				outStream.println("File not found: "+options.filename);
				return;
			}
		}
		int words = 0;
		int lines = 0;
		int chars = 0;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while (in.available() > 0) {
				line = reader.readLine();
				lines++;
				words += line.split("\\s+").length;
				chars += line.length();
			}
		} catch (IOException e) {
			outStream.println("IO exception while reading a file: "+options.filename);
			return;
		}
		StringBuilder resultBuilder = new StringBuilder();
		if (options.countChars) {
			resultBuilder.append(chars);
			resultBuilder.append("\t");
		}
		if (options.countWords) {
			resultBuilder.append(words);
			resultBuilder.append("\t");
		}
		if (options.countLines) {
			resultBuilder.append(lines);
			resultBuilder.append("\t");
		}
		outStream.println(resultBuilder.toString());
	}

	@Override
	public String getCommand() {
		return "wc";
	}
	
	private class WcOptions {
		private static final String OPTION_PREFIX = "-";
		private static final String CHARS_OPTION = "-m";
		private static final String WORDS_OPTION = "-w";
		private static final String LINES_OPTION = "-l";

		public boolean countChars = false;
		public boolean countWords = false;
		public boolean countLines = false;
		public String filename = null;
		
		public WcOptions(String[] args) {
			if (args.length != 0 && !args[args.length-1].startsWith(OPTION_PREFIX)) {
				filename = args[args.length-1];
			}
			for (String option: args) {
				switch (option) {
				case CHARS_OPTION:
					countChars = true;
					break;
				case WORDS_OPTION:
					countWords = true;
					break;
				case LINES_OPTION:
					countLines = true;
				default:
					break;
				}
			}
			if (!(countChars || countWords || countLines)) {
				countChars = true;
				countWords = true;
				countLines = true;
			}
		}
	}

	@Override
	public String getManual() {
		return "Counts chars, words and lines in text. If file not specified, stdin is used.\n"
				+ "Format: wc [-m | -w | -l | file]\n"
				+ "\t-m: count chars\n"
				+ "\t-w: count words\n"
				+ "\t-l: count lines";
	}
}
