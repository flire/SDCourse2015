package ru.spbau.tishchenko.sd.class01.test;

import ru.spbau.tishchenko.sd.class01.shell.HighlightingOptions;

public class GrepACTest extends GrepCommonTest {
	
	private final String lineSeparator = "--";

	@Override
	protected String getGrepCommand() {
		return "grepac";
	}

	@Override
	protected String resolveTags(String taggedResult) {
		HighlightingOptions options = shell.getHighlightingOptions();
		return taggedResult.replaceAll(GrepCommonTest.OCCURENCE_START_TAG, options.startMarker)
				.replaceAll(GrepCommonTest.OCCURENCE_END_TAG, options.endMarker)
				.replaceAll(LINE_SEPARATOR_TAG, lineSeparator);
	}

}
