package ru.spbau.tishchenko.sd.class01.commands.grep.apachecli;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import ru.spbau.tishchenko.sd.class01.commands.ICommand;
import ru.spbau.tishchenko.sd.class01.commands.grep.GrepLogic;
import ru.spbau.tishchenko.sd.class01.shell.HighlightingOptions;
import ru.spbau.tishchenko.sd.class01.shell.IShell;

public class ACGrepCommand implements ICommand {
	
	private Options options = createOptions();
	private String pattern;
	private File file = null;

	@Override
	public void execute(IShell shell, String[] args, InputStream in, OutputStream out) {
	    CommandLineParser parser = new DefaultParser();
	    PrintStream outStream = new PrintStream(out);
	    try {
	        CommandLine line = parser.parse( options, args );
	        if (!validateParameters(shell, outStream, line.getArgs())) {
	        	return;
	        }
	        boolean asWord = line.hasOption('w');
	        boolean caseInsensitive = line.hasOption('i');
	        int linesAfter = 0;
	        if (line.hasOption('A')) {
	        	linesAfter = Integer.parseInt(line.getOptionValue('A'));
	        }
	        HighlightingOptions options = shell.getHighlightingOptions();
			GrepLogic grepExecutor = new GrepLogic(pattern, 
					new GrepLogic.Options(asWord, !caseInsensitive, linesAfter), 
					new GrepLogic.HighlightingOptions(options.startMarker, options.endMarker));
			try {
				if (file != null) {
					in = new FileInputStream(file);
				}
				grepExecutor.execute(in, out);
			} catch (FileNotFoundException e) {
				outStream.println("File not found: "+file.getAbsolutePath());
			}
	    }
	    catch( ParseException exp ) {
	        System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
	    }
	}

	@Override
	public String getCommand() {
		return "grepac";
	}

	@Override
	public String getManual() {
		HelpFormatter formatter = new HelpFormatter();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(os);
		formatter.printHelp(writer, 80, getCommand(), "", options, 0, 0, "");
		try {
			return new String(os.toByteArray(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	private static Options createOptions() {
		Options result = new Options();
		result.addOption("w", false, "Search for word occurences only");
		result.addOption("i", false, "Case insensitive search");
		result.addOption("A", true, "Number of context lines printed after every occurence");
		return result;
	}
	
	private boolean validateParameters(IShell shell, PrintStream out, String args[]) {
		if (args.length < 1) {
			out.println("Pattern isn't specified");
			return false;
		}
		if (args.length > 2) {
			out.println("Too many parameters");
		}
		pattern = args[0];
		if (args.length == 1) {
			return true;
		}
		String filename = args[1];
		file = new File(shell.getCurrentDir(), filename);
		return true;
	}

}
