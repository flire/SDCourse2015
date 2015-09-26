package ru.spbau.tishchenko.sd.class01.commands;

import java.io.*;

import ru.spbau.tishchenko.sd.class01.shell.IShell;
import ru.spbau.tishchenko.sd.class01.utils.StreamUtils;

/**
 * Created by flire on 08.09.15.
 */
public class CatCommand implements ICommand {
    @Override
    public void execute(IShell shell, String[] args, InputStream in, OutputStream out) {
    	PrintStream printStream = new PrintStream(out);
        if (args.length == 0) {
            StreamUtils.copy(in, out);
            return;
        }
        File currentDir = shell.getCurrentDir();
        File fileToOpen = new File(currentDir, args[0]);
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

