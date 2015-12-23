package ru.spbau.tishchenko.sd.class01.commands.grep.jc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import com.beust.jcommander.JCommander;

import ru.spbau.tishchenko.sd.class01.commands.ICommand;
import ru.spbau.tishchenko.sd.class01.commands.grep.GrepLogic;
import ru.spbau.tishchenko.sd.class01.shell.HighlightingOptions;
import ru.spbau.tishchenko.sd.class01.shell.IShell;

public class JCGrepCommand implements ICommand {
	
	private GrepArguments grepArgs;
	private JCommander commander;
	private String pattern;
	private File file = null;

	@Override
	public void execute(IShell shell, String[] args, InputStream in, OutputStream out) {
		grepArgs = new GrepArguments();
		commander = new JCommander(grepArgs);
		commander.parse(args);
		PrintStream outStream = new PrintStream(out);
		boolean areParametersValid = validateParameters(shell, outStream);
		if (!areParametersValid) {
			return;
		}
        HighlightingOptions options = shell.getHighlightingOptions();
		GrepLogic grepExecutor = new GrepLogic(pattern, 
				new GrepLogic.Options(grepArgs.asWord,
						!grepArgs.caseInsensitive,
						grepArgs.linesAfter,
						Arrays.asList(args).indexOf("-A") != -1), 
				new GrepLogic.HighlightingOptions(options.startMarker, options.endMarker));
		try {
			if (file != null) {
				in = new FileInputStream(file);
			}
			grepExecutor.execute(in, out);
		} catch (FileNotFoundException e) {
			outStream.println("File not found: "+file.getAbsolutePath());
		} finally {
			file = null;
		}
	}

	private boolean validateParameters(IShell shell, PrintStream out) {
		if (grepArgs.parameters.size() < 1) {
			out.println("Pattern isn't specified");
			return false;
		}
		if (grepArgs.parameters.size() > 2) {
			out.println("Too many parameters");
		}
		pattern = grepArgs.parameters.get(0);
		if (grepArgs.parameters.size() == 1) {
			return true;
		}
		String filename = grepArgs.parameters.get(1);
		file = new File(shell.getCurrentDir(), filename);
		return true;
		
	}

	@Override
	public String getCommand() {
		return "grepjc";
	}

	@Override
	public String getManual() {
		StringBuilder out = new StringBuilder();
		new JCommander(new GrepArguments()).usage(out);
		return out.toString();
	}

}
