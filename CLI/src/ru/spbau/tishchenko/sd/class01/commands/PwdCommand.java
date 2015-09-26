package ru.spbau.tishchenko.sd.class01.commands;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import ru.spbau.tishchenko.sd.class01.shell.IShell;

/**
 * Created by flire on 08.09.15.
 */
public class PwdCommand implements ICommand {
    @Override
    public void execute(IShell shell, String[] args, InputStream in, OutputStream out) {
        new PrintStream(out).println(shell.getCurrentDir().getAbsolutePath());
    }

    @Override
    public String getCommand() {
        return "pwd";
    }

	@Override
	public String getManual() {
		return "Prints current directory. Need no arguments.";
	}
}
