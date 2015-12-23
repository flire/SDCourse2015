package ru.spbau.tishchenko.sd.class01.tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import ru.spbau.tishchenko.sd.class01.commands.ICommand;
import ru.spbau.tishchenko.sd.class01.commands.grep.GrepLogic;
import ru.spbau.tishchenko.sd.class01.commands.grep.jc.JCGrepCommand;
import ru.spbau.tishchenko.sd.class01.shell.IShell;
import ru.spbau.tishchenko.sd.class01.test.TestingShell;

public class TestJCGrep {

	private static final String ENCODING = "UTF-8";

	private static ICommand command;
	private static IShell testShell;

	@BeforeClass
	public static void setup() {
		command = new JCGrepCommand();
		testShell = new TestingShell("test_data");
	}

	@Test
	public void testSimpleMatch() throws UnsupportedEncodingException {
		String in = "pattern \nPattern \nempty\n and pattern here";
		String expected = "<start>pattern<end> \n and <start>pattern<end> here\n";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		command.execute(testShell, 
				new String[] { "pattern" }, 
				new ByteArrayInputStream(in.getBytes(ENCODING)), 
				out);
		assertEquals(expected, out.toString(ENCODING));
	}

	@Test
	public void testCaseInsensitiveSearch() throws UnsupportedEncodingException {
		String in = "pattern \nPattern \nempty\n and pattern here";
		String expected = "<start>pattern<end> \n<start>Pattern<end> \n and <start>pattern<end> here\n";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		command.execute(testShell, 
				new String[] { "-i", "pattern" }, 
				new ByteArrayInputStream(in.getBytes(ENCODING)), 
				out);
		assertEquals(expected, out.toString(ENCODING));
	}

	@Test
	public void testLinesAfter() throws UnsupportedEncodingException {
		String in = "junk here \n pattern \n line 1 \n line 2 \n line 3";
		String expected = " <start>pattern<end> \n line 1 \n line 2 \n" + GrepLogic.Options.DEFAULT_LINE_SEPARATOR + "\n";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		command.execute(testShell, 
				new String[] { "-A", "2", "pattern" }, 
				new ByteArrayInputStream(in.getBytes(ENCODING)), 
				out);
		assertEquals(expected, out.toString(ENCODING));
	}

	@Test
	public void testAsWord() throws UnsupportedEncodingException {
		String in = "no patterns allowed \n pattern! \n pattern... sigh...";
		String expected = " <start>pattern<end>! \n <start>pattern<end>... sigh...\n";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		command.execute(testShell, 
				new String[] { "-w", "pattern" }, 
				new ByteArrayInputStream(in.getBytes(ENCODING)), 
				out);
		assertEquals(expected, out.toString(ENCODING));
	}

	@Test
	public void testFile() throws UnsupportedEncodingException {
		String expected = "<start>pattern<end>\nand <start>pattern<end> here\n";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		command.execute(testShell, 
				new String[] { "pattern", "simpleMatch.txt" }, 
				new ByteArrayInputStream(new byte[] {}), 
				out);
		assertEquals(expected, out.toString(ENCODING));
	}

	@Test
	public void testEmptyFile() throws UnsupportedEncodingException {
		String expected = "";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		command.execute(testShell, 
				new String[] { "pattern", "emptyFile.txt" }, 
				new ByteArrayInputStream(new byte[] {}), 
				out);
		assertEquals(expected, out.toString(ENCODING));
	}
	
	@Test
	public void testEmptyInput() throws UnsupportedEncodingException {
		String expected = "";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		command.execute(testShell, 
				new String[] { "pattern" }, 
				new ByteArrayInputStream(new byte[] {}), 
				out);
		assertEquals(expected, out.toString(ENCODING));
	}
	
	@Test
	public void testNonExistentFile() throws UnsupportedEncodingException {
		String expected = "File not found";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		command.execute(testShell, 
				new String[] { "pattern", "nonExistent.txt" }, 
				new ByteArrayInputStream(new byte[] {}), 
				out);
		assertTrue(out.toString(ENCODING).startsWith(expected));
	}
}
