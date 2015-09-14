package ru.spbau.tishchenko.sd.class01.commands;

import java.io.InputStream;
import java.io.OutputStream;

import ru.spbau.tishchenko.sd.class01.Shell;

public class ExitCommand implements ICommand {

	    @Override
	    public void execute(Shell shell, String[] args, InputStream in, OutputStream out) {
	        shell.stop();
	    }

	    @Override
	    public String getCommand() {
	        return "exit";
	    }

		@Override
		public String getManual() {
			return "Exits the shell. Needed no arguments.";
		}
}
