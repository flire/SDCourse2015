package ru.spbau.tishchenko.sd.class01.test;

import org.junit.Before;

import ru.spbau.tishchenko.sd.class01.commands.ICommand;

import java.util.ServiceLoader;

import org.junit.Assert;

public abstract class ShellCommandTestCase {
	
	protected TestingShell shell;
	
	@Before
	public void configure() {
		shell = new TestingShell(getShellDir());
    	ServiceLoader<ICommand> commandsLoader = ServiceLoader.load(ICommand.class);
        for(ICommand command: commandsLoader) {
        	shell.registerCommand(command);
        }
	}
	
	protected void test(String cmdLine, String expectedResult) {
		Assert.assertEquals(expectedResult, shell.execute(cmdLine));
	}
	
	abstract String getShellDir();
}
