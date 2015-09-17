package ru.spbau.tishchenko.sd.class01.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtils {
	public static void copy(InputStream input, OutputStream output) {
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
				output.write(buf, 0, length);
			}
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
