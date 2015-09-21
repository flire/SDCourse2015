package ru.spbau.tishchenko.sd.class01.commands;

import ru.spbau.tishchenko.sd.class01.IShell;
import ru.spbau.tishchenko.sd.class01.utils.StreamUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrepCommand implements ICommand {
    private boolean isAfterContext;
    private int linesAfter = 1;
    private PrintStream output;

    @Override
    public void execute(IShell shell, String[] args, InputStream in, OutputStream out) {
        GrepOptions options = new GrepOptions(args);
        isAfterContext = options.isAfterContext;
        linesAfter = options.linesNumber;
        output = new PrintStream(out);

        String pattern = options.pattern;
        int flags = 0;
        if (options.isCaseIgnoring) {
            flags |= Pattern.CASE_INSENSITIVE;
        }
        if (options.isFullWordMatch) {
            pattern = "\\b" + pattern + "\\b";
        }

        Pattern p = Pattern.compile(pattern, flags);
        if (options.files.size() == 0) {
            try {
                grepStream(p, in);
            } catch (IOException e) {
                output.println("IO Exceptions occurred during grep: " + e.getMessage());
            }
        } else {
            for (String file : options.files) {
                try {
                    grepFile(p, file);
                } catch (FileNotFoundException e) {
                    output.println("Cannot find file: " + e.getMessage());
                }
            }
        }
    }

    private void grepFile(Pattern p, String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        if (!file.exists()) {
            output.println("File " + fileName + " wasn't found");
            return;
        }
        List<String> data = StreamUtils.readLines(new FileInputStream(file));
        grepData(p, data);
    }

    private void grepData(Pattern p, List<String> data) {
        for (int i = 0; i < data.size(); ++i) {
            String line = data.get(i);
            Matcher m = p.matcher(line);
            if (m.find()) {
                for (int j = i; j < Math.min(data.size() - 1, i + linesAfter); ++j) {
                    String outLine = data.get(j);
                    output.println(outLine);
                }
                if (isAfterContext) {
                    output.println("----------------------------------------------");
                }
            }
        }
    }

    private void grepStream(Pattern p, InputStream in) throws IOException {
        List<String> data = StreamUtils.readLines(in);
        grepData(p, data);
    }

    @Override
    public String getCommand() {
        return "grep";
    }

    @Override
    public String getManual() {
        return "Search pattern in the text.\nUsage: grep -iw <pattern> <filename>";
    }

    private static class GrepOptions {
        private static final String OPTION_PREFIX = "-";
        private static final String IGNORE_CASE_OPTION = "-i";
        private static final String WORD_BOUND_OPTION = "-w";
        private static final String AFTER_CONTEXT_OPTION = "-A";

        private boolean isCaseIgnoring = false;
        private boolean isFullWordMatch = false;
        private boolean isAfterContext = false;
        private int linesNumber = 1;
        private boolean wasPatternRead = false;

        private String pattern;
        private final List<String> files = new ArrayList<>();

        public GrepOptions(String[] args) {
            for (int i = 0; i < args.length; ++i) {
                String arg = args[i];
                if (!arg.startsWith(OPTION_PREFIX) && (i - 1 < 0 || !args[i - 1].equals(AFTER_CONTEXT_OPTION))) {
                    if (!wasPatternRead) {
                        pattern = arg;
                        wasPatternRead = true;
                    } else {
                        files.add(arg);
                    }
                } else {
                    switch (arg) {
                        case AFTER_CONTEXT_OPTION:
                            isAfterContext = true;
                            if (i < args.length - 1) {
                                linesNumber = Integer.parseInt(args[i + 1]);
                            }
                            break;
                        case IGNORE_CASE_OPTION:
                            isCaseIgnoring = true;
                            break;
                        case WORD_BOUND_OPTION:
                            isFullWordMatch = true;
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }
}
