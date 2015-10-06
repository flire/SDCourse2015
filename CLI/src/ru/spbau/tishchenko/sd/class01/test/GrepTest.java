package ru.spbau.tishchenko.sd.class01.test;

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
}
