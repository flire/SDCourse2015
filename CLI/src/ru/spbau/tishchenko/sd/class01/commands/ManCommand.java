package ru.spbau.tishchenko.sd.class01.commands;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import ru.spbau.tishchenko.sd.class01.Shell;

public class ManCommand implements ICommand {

	@Override
	public void execute(Shell shell, String[] args, InputStream in, OutputStream out) {
		PrintStream outStream = new PrintStream(out);
		if (args.length != 1) {
			outStream.println("Invalid number of arguments.");
			return;
		}
		String cmd = args[0];
		if (!shell.isCommandExists(cmd)) {
			outStream.println("Command doesn't exist: "+ cmd);
			return;
		}
		outStream.println(shell.getCommandDescription(cmd));
	}

	@Override
	public String getCommand() {
		return "man";
	}

	@Override
	public String getManual() {
		return "Shows the description for the command.\n"
				+ "Format: man <command>";
	}

}
