package ru.spbau.tishchenko.sd.class01.test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ru.spbau.tishchenko.sd.class01.shell.HighlightingOptions;
import ru.spbau.tishchenko.sd.class01.shell.ShellImpl;

public class TestingShell extends ShellImpl {
	public TestingShell(String currentDirPath) {
		this.currentDir = new File(currentDirPath);
	}
	
	public String execute(String cmdLine) {
		return execute(cmdLine, "");
	}
	
	public String execute(String cmdLine, String input) {
		String args[] = parseCommand(cmdLine);
		InputStream result = execute(args, new ByteArrayInputStream(input.getBytes()));
		return outputAsString(result);
	}
	
	private String outputAsString(InputStream result) {
		StringBuilder out = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(result))) {
	        int ch;
	        while ((ch = reader.read()) != -1) {
	            out.append((char)ch);
	        }
	        System.out.println(out.toString());   //Prints the string content read from input stream
        } catch (IOException e) {
        	System.err.println(e.getMessage());
		}
        return out.toString();
	}

	@Override
	public HighlightingOptions getHighlightingOptions() {
		return new HighlightingOptions("<start>", "<end>");
	}
}
