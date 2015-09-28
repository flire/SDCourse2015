package ru.spbau.tishchenko.sd.class01.shell;

import ru.spbau.tishchenko.sd.class01.commands.ExitCommand;
import ru.spbau.tishchenko.sd.class01.commands.ICommand;
import ru.spbau.tishchenko.sd.class01.utils.StreamUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created by flire on 08.09.15.
 */
public class ListeningShell extends ShellImpl {
	String SHELL_PROMPT = ">>>";
	private boolean isInterrupted;
	private static final String ANSI_RESET = "\u001B[0m";
	private static final String ANSI_RED = "\u001B[31m";

	public ListeningShell(String currentDirPath) {
		this.currentDir = new File(currentDirPath);
		isInterrupted = false;
		this.putCommand(new ExitCommand());
	}
	
	public void listen() {
		try (BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in))) {
			while (!isInterrupted) {
				System.out.print(SHELL_PROMPT);
				String cmd = buffer.readLine();
				String[] args = parseCommand(cmd);
				InputStream result = execute(args, System.in);
				StreamUtils.copy(result, System.out);
				result.close();
			}
		} catch (IOException e) {
			System.err.println("Stream reading error: " + e.getMessage());
		}
	}
	
	public void stopListen() {
		isInterrupted = true;
	}
	
	@Override
	protected String[] parseCommand(String cmd) {
		return super.parseCommand(cmd.replaceFirst(SHELL_PROMPT, ""));
	}

	@Override
	public HighlightingOptions getHighlightingOptions() {
		return new HighlightingOptions(ANSI_RED, ANSI_RESET);
	}
}
