package ru.spbau.tishchenko.sd.class01.shell;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ru.spbau.tishchenko.sd.class01.commands.ICommand;

public class ShellImpl implements IShell {

	private static final String PIPE_DELIMITER = "|";
	private HashMap<String, ICommand> commands = new HashMap<String, ICommand>();
	protected File currentDir;

	public ShellImpl() {
		super();
	}

	public boolean isCommandExists(String command) {
		return commands.containsKey(command);
	}

	public String getCommandDescription(String command) {
		ICommand cmd = getCommand(command);
		if (cmd != null) {
			return cmd.getManual();
		} else {
			return null;
		}
	}

	public File getCurrentDir() {
		return new File(currentDir.getAbsolutePath());
	}

	public Boolean registerCommand(ICommand command) {
		String commandString = command.getCommand();
		if (isCommandExists(commandString)) {
			return false;
		}
		commands.put(commandString, command);
		return true;
	}

	protected InputStream execute(String[] args) {
		List<CommandSpecifier> cmds = parsePipe(args);
		InputStream result = null;
		for (CommandSpecifier cmd : cmds) {
			String commandString = cmd.command;
			if (!isCommandExists(commandString)) {
				return createErrorStream("Command not found", commandString);
			}
			ICommand command = getCommand(commandString);
			String[] effectiveArguments = cmd.args;
			try (PipedOutputStream out = new PipedOutputStream()) {
				if (result == null) {
					result = new PipedInputStream(out);
					command.execute(this, effectiveArguments, System.in, out);
				} else {
					PipedInputStream newIn = new PipedInputStream(out);
					command.execute(this, effectiveArguments, result, out);
					result.close();
					result = newIn;
				}
			} catch (IOException e) {
				return createErrorStream("Error while creating pipe streams for: ", commandString);
			}
		}
		return result;
	}
	
	protected ICommand getCommand(String cmdLine) {
		return commands.get(cmdLine);
	}
	
	protected void putCommand(ICommand command) {
		commands.put(command.getCommand(), command);
	}

	private InputStream createErrorStream(String error, String commandString) {
		return new ByteArrayInputStream((error + ": " + commandString).getBytes());
	}

	protected String[] parseCommand(String cmd) {
		return cmd.split("\\s+");
	}

	private List<CommandSpecifier> parsePipe(String[] args) {
		ArrayList<CommandSpecifier> result = new ArrayList<>();
		int prevRange = 0;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals(PIPE_DELIMITER)) {
				result.add(new CommandSpecifier(Arrays.copyOfRange(args, prevRange, i)));
				prevRange = i + 1;
			}
		}
		result.add(new CommandSpecifier(Arrays.copyOfRange(args, prevRange, args.length)));
		return result;
	}


	class CommandSpecifier {
		public final String command;
		public final String[] args;

		public CommandSpecifier(String[] args) {
			command = args[0];
			int length = args.length;
			if (length > 1) {
				this.args = Arrays.copyOfRange(args, 1, length);
			} else {
				this.args = new String[0];
			}
		}
	}
}