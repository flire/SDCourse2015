package ru.spbau.tishchenko.sd.class01;

import java.util.ServiceLoader;

import ru.spbau.tishchenko.sd.class01.commands.ICommand;

public class Main {

    public static void main(String[] args) {
	    Shell shell = new Shell(System.getProperty("user.dir"));
        registerAvailableCommands(shell);
        shell.listen();
    }

    private static void registerAvailableCommands(Shell shell) {
    	ServiceLoader<ICommand> commandsLoader = ServiceLoader.load(ICommand.class);
        for(ICommand command: commandsLoader) {
        	shell.registerCommand(command);
        }
    }
}