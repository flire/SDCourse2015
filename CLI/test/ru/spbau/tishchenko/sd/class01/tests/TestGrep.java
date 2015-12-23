package ru.spbau.tishchenko.sd.class01.tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import ru.spbau.tishchenko.sd.class01.commands.GrepCommand;
import ru.spbau.tishchenko.sd.class01.shell.IShell;
import ru.spbau.tishchenko.sd.class01.test.TestingShell;

public class TestGrep {

	private static final String ENCODING = "UTF-8";

	private static GrepCommand command;
	private static IShell testShell;

	@BeforeClass
	public static void setup() {
		command = new GrepCommand();
		testShell = new TestingShell("test_data");
	}

	@Test
	public void testSimpleMatch() throws UnsupportedEncodingException {
		String in = "pattern \nPattern \nempty\n and pattern here";
		String expected = "pattern \n and pattern here\n";
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
		String expected = "pattern \nPattern \n and pattern here\n";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		command.execute(testShell, 
				new String[] { "pattern", "-i" }, 
				new ByteArrayInputStream(in.getBytes(ENCODING)), 
				out);
		assertEquals(expected, out.toString(ENCODING));
	}

	@Test
	public void testLinesAfter() throws UnsupportedEncodingException {
		String in = "junk here \n pattern \n line 1 \n line 2 \n line 3";
		String expected = " pattern \n line 1 \n" + GrepCommand.LINE_SEPARATOR + "\n";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		command.execute(testShell, 
				new String[] { "pattern", "-A", "2" }, 
				new ByteArrayInputStream(in.getBytes(ENCODING)), 
				out);
		assertEquals(expected, out.toString(ENCODING));
	}

	@Test
	public void testAsWord() throws UnsupportedEncodingException {
		String in = "no patterns allowed \n pattern! \n pattern... sigh...";
		String expected = " pattern! \n pattern... sigh...\n";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		command.execute(testShell, 
				new String[] { "pattern", "-w" }, 
				new ByteArrayInputStream(in.getBytes(ENCODING)), 
				out);
		assertEquals(expected, out.toString(ENCODING));
	}

	@Test
	public void testFile() throws UnsupportedEncodingException {
		String expected = "pattern\nand pattern here\n";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		command.execute(testShell, 
				new String[] { "pattern", "test_data/simpleMatch.txt" }, 
				new ByteArrayInputStream(new byte[] {}), 
				out);
		assertEquals(expected, out.toString(ENCODING));
	}

	@Test
	public void testEmptyFile() throws UnsupportedEncodingException {
		String expected = "";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		command.execute(testShell, 
				new String[] { "pattern", "test_data/emptyFile.txt" }, 
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
		String expected = "File nonExistent.txt wasn't found\n";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		command.execute(testShell, 
				new String[] { "pattern", "nonExistent.txt" }, 
				new ByteArrayInputStream(new byte[] {}), 
				out);
		assertEquals(expected, out.toString(ENCODING));
	}
}
