package ru.spbau.tishchenko.sd.class01.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

    public static List<String> readLines(FileInputStream fileInputStream) {
        List<String> result = new ArrayList<>();
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream))) {
            result.add(bufferedReader.readLine());
        } catch (IOException e) {
            System.err.println("Cannot open file stream: " + e.getMessage());
        }
        return result;
    }
}
