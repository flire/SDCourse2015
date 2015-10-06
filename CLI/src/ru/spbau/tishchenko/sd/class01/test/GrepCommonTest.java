package ru.spbau.tishchenko.sd.class01.test;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public abstract class GrepCommonTest extends ShellCommandTestCase {

	private static final String CMD_PLACEHOLDER = "<grepcmd>";
	private static final String FILE_PLACEHOLDER = "<file>";
	private final File TEST_DATA_DIR = new File("test_data");
	private final static String RESULT_FILE_EXTENSION = ".result";
	protected final static String OCCURENCE_START_TAG = "<occurence>";
	protected final static String OCCURENCE_END_TAG = "</occurence>";
	protected final static String LINE_SEPARATOR_TAG = "<lineSeparator>";
	
	abstract protected String getGrepCommand();
	
	abstract protected String resolveTags(String taggedResult);
	
	protected String createCommand(String cmdWithPlaceholder, String filename) {
		return cmdWithPlaceholder.replaceAll(CMD_PLACEHOLDER, getGrepCommand())
				.replaceAll(FILE_PLACEHOLDER, TEST_DATA_DIR + File.separator + filename);
	}
	
	@Override
	String getShellDir() {
		return System.getProperty("user.dir");
	}
	
	protected void testWithFile(String cmd, String filename) throws FileNotFoundException {
		String result = readFile(new File(TEST_DATA_DIR, filename + RESULT_FILE_EXTENSION));
		test(createCommand(cmd, filename), resolveTags(result));
	}

	private String readFile(File file) throws FileNotFoundException {
	    StringBuilder fileContents = new StringBuilder((int)file.length());
	    String lineSeparator = System.getProperty("line.separator");
	    
	    try(Scanner scanner = new Scanner(file)) {
	        while(scanner.hasNextLine()) {        
	            fileContents.append(scanner.nextLine() + lineSeparator);
	        }
	        return fileContents.toString();
	    }
	}

	@Test
	public void testSimpleMatch() throws FileNotFoundException {
		testWithFile("<grepcmd> pattern <file>", "simpleMatch.txt");
	}
	
	@Test
	public void testStreaming() throws FileNotFoundException {
		testWithFile("cat <file> | <grepcmd> pattern", "simpleMatch.txt");
	}
	
	@Test
	public void testCaseInsensitiveSearch() throws FileNotFoundException {
		testWithFile("<grepcmd> -i pattern <file>", "caseInsensitiveSearch.txt");
	}
	
	@Test
	public void testLinesAfter() throws FileNotFoundException {
		testWithFile("<grepcmd> -A 2 pattern <file>", "2linesAfter.txt");
		testWithFile("<grepcmd> -A 0 pattern <file>", "0linesAfter.txt");
	}
	
	@Test
	public void testAsWord() throws FileNotFoundException {
		testWithFile("<grepcmd> -w pattern <file>", "asWord.txt");
	}
	
	@Test
	public void testMultipleMatchInString() throws FileNotFoundException {
		testWithFile("<grepcmd> abadaaba <file>", "multipleMatch.txt");
	}
	
	@Test
	public void testAllParameters() throws FileNotFoundException {
		testWithFile("<grepcmd> -w -i -A 2 pattern <file>", "allParameters.txt");
	}
}
