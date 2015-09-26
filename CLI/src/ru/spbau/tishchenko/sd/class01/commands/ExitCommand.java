package ru.spbau.tishchenko.sd.class01.commands;

import java.io.InputStream;
import java.io.OutputStream;

import ru.spbau.tishchenko.sd.class01.shell.IShell;
import ru.spbau.tishchenko.sd.class01.shell.ListeningShell;
import ru.spbau.tishchenko.sd.class01.shell.ShellImpl;

/**
 * It's a command for exiting from a listening shell and it needs no registration.
 * @author flire
 */
public class ExitCommand implements ICommand {
    @Override
    public void execute(IShell shell, String[] args, InputStream in, OutputStream out) {
    	if (shell instanceof ListeningShell) {
    		((ListeningShell) shell).stopListen();
    	}
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
