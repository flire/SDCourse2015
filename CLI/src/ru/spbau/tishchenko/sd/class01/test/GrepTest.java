package ru.spbau.tishchenko.sd.class01.test;

import java.io.FileNotFoundException;

import org.junit.Test;

public class GrepTest extends GrepCommonTest {
	
	private final String lineSeparator = "----------------------------------------------";

	@Override
	protected String getGrepCommand() {
		return "grep";
	}

	@Override
	protected String resolveTags(String taggedResult) {
		return taggedResult.replaceAll(GrepCommonTest.OCCURENCE_START_TAG, "")
				.replaceAll(GrepCommonTest.OCCURENCE_END_TAG, "")
				.replaceAll(LINE_SEPARATOR_TAG, lineSeparator);
	}
	
	@Test
	@Override
	public void testLinesAfter() throws FileNotFoundException {
		testWithFile("<grepcmd> -A 3 pattern <file>", "2linesAfter.txt");
		testWithFile("<grepcmd> -A 1 pattern <file>", "0linesAfter.txt");
	}
	
	@Test
	@Override
	public void testAllParameters() throws FileNotFoundException {
		testWithFile("<grepcmd> -w -i -A 3 pattern <file>", "allParameters.txt");
	}

}
