package ru.spbau.tishchenko.sd.class01.commands;

import java.io.InputStream;
import java.io.OutputStream;

import ru.spbau.tishchenko.sd.class01.Shell;
/*
 * An interface denoting a command.
 * It isn't neccessary to register the command in the shell, all commands loaded
 * by the shell itself if listed in META-INF/services/...ICommand file.
 */
public interface ICommand {
    public abstract void execute(Shell shell, String[] args, InputStream in, OutputStream out);
    public abstract String getCommand();
    public abstract String getManual();
}
