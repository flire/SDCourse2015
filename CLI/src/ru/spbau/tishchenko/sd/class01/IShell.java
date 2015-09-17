package ru.spbau.tishchenko.sd.class01;

import java.io.File;

import ru.spbau.tishchenko.sd.class01.commands.ICommand;

public interface IShell {
	public String getCommandDescription(String command);
	public boolean isCommandExists(String command);
	public File getCurrentDir();
	public void stop(ICommand command);
}
