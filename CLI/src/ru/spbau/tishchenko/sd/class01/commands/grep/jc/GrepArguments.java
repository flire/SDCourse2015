package ru.spbau.tishchenko.sd.class01.commands.grep.jc;

import java.util.List;

import com.beust.jcommander.Parameter;

public class GrepArguments {
	@Parameter
	public List<String> parameters;
	
	@Parameter(names = "-w", description = "Find word occurences only")
	public boolean asWord;
	
	@Parameter(names = "-A", description = "A number of context lines printed after every occurence")
	public int linesAfter;
	
	@Parameter(names = "-i", description = "Case insensitive search")
	public boolean caseInsensitive;
}
