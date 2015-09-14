package ru.spbau.tishchenko.sd.class01.commands;

import ru.spbau.tishchenko.sd.class01.Shell;

import java.io.*;

/**
 * Created by flire on 08.09.15.
 */
public class CatCommand implements ICommand {
    @Override
    public void execute(Shell shell, String[] args, InputStream in, OutputStream out) {
    	PrintStream printStream = new PrintStream(out);
        if (args.length == 0) {
            printStream.println("No argument supplied.");
        }
        File currentDir = shell.getCurrentDir();
        File fileToOpen = new File(currentDir, args[0]);
        if (!fileToOpen.exists()) {
            printStream.println("File doesn't exist: " + args[0]);
        }
        try(FileInputStream stream = new FileInputStream(fileToOpen)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            reader.lines().forEach((line) -> {printStream.println(line);});
        } catch (FileNotFoundException e) {
        	printStream.println("File doesn't exist: " + args[0]);
        } catch (IOException e) {
        	printStream.println("IO exception while reading the file: " + e.getMessage());
        }
    }

    @Override
    public String getCommand() {
        return "cat";
    }

	@Override
	public String getManual() {
		return "Prints file contents. \nFormat: cat <file>";
	}
}

