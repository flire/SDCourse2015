package ru.spbau.tishchenko.sd.class01;

import ru.spbau.tishchenko.sd.class01.commands.ICommand;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by flire on 08.09.15.
 */
public class Shell {
	private static final String PIPE_DELIMITER = "|";
	private HashMap<String, ICommand> commands = new HashMap<String, ICommand>();
	private File currentDir;
	private String SHELL_PROMPT = ">>>";
	private ExecutorService executor = Executors.newSingleThreadExecutor();

	public Shell(String currentDirPath) {
		this.currentDir = new File(currentDirPath);
	}

	public Boolean registerCommand(ICommand command) {
		String commandString = command.getCommand();
		if (commands.containsKey(commandString)) {
			return false;
		}
		commands.put(commandString, command);
		return true;
	}

	private InputStream execute(String[] args) {
		List<CommandSpecifier> cmds = parsePipe(args);
		InputStream result = null;
		for (CommandSpecifier cmd : cmds) {
			String commandString = cmd.command;
			if (!commands.containsKey(commandString)) {
				return createErrorStream("Command not found", commandString);
			}
			ICommand command = commands.get(commandString);
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

	private InputStream createErrorStream(String error, String commandString) {
		return new ByteArrayInputStream((error + ": " + commandString).getBytes());
	}

	public void listen() {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				while (!Thread.currentThread().isInterrupted()) {
					System.out.print(SHELL_PROMPT);
					String cmd = System.console().readLine();
					String[] args = parseCommand(cmd);
					InputStream result = execute(args);
					copyToOut(result);
					System.out.println();
				}
			}
		});
	}

	public boolean isCommandExists(String command) {
		return commands.containsKey(command);
	}

	public String getCommandDescription(String command) {
		ICommand cmd = commands.get(command);
		if (cmd != null) {
			return cmd.getManual();
		} else {
			return null;
		}
	}

	private String[] parseCommand(String cmd) {
		return cmd.replaceFirst(SHELL_PROMPT, "").split(" ");
	}

	public void stop() {
		executor.shutdownNow();
	}

	private void copyToOut(InputStream input) {
		byte[] buf = new byte[8192];
		try {
			while (true) {
				int length;
				if (input.available() <= 0) {
					break;
				}
				length = input.read(buf);
				if (length <= 0)
					break;
				System.out.write(buf, 0, length);
			}
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File getCurrentDir() {
		return new File(currentDir.getAbsolutePath());
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

	private class CommandSpecifier {
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
