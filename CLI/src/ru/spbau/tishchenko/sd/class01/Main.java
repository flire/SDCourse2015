package ru.spbau.tishchenko.sd.class01;

import java.util.ServiceLoader;

import ru.spbau.tishchenko.sd.class01.commands.ICommand;
import ru.spbau.tishchenko.sd.class01.shell.ListeningShell;
import ru.spbau.tishchenko.sd.class01.shell.ShellImpl;

public class Main {

    public static void main(String[] args) {
	    ListeningShell shell = new ListeningShell(System.getProperty("user.dir"));
        registerAvailableCommands(shell);
        shell.listen();
    }

    private static void registerAvailableCommands(ShellImpl shell) {
    	ServiceLoader<ICommand> commandsLoader = ServiceLoader.load(ICommand.class);
        for(ICommand command: commandsLoader) {
        	shell.registerCommand(command);
        }
    }
}