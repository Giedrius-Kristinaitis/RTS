package com.gasis.rts.filehandling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads a small text file with titled lines. The entire file is read into memory at once
 */
public class FileLineReader {

    // the lines of the file
    private List<String> lines = new ArrayList<String>();

    // the thing used to split line title from the content
    private String separator;

    /**
     * Class constructor. Initializes the buffered reader
     *
     * @param input     stream to read data from
     * @param separator the thing used to split line title from the content
     */
    public FileLineReader(InputStream input, String separator) {
        this.separator = separator;

        readLines(input);
    }

    /**
     * Reads all file lines into the line list
     *
     * @param input stream to read file data from
     */
    private void readLines(InputStream input) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            String line = null;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Reads the line with the given title
     *
     * @param title title of the line
     * @return read line, null if the line was not found
     */
    public String readLine(String title) {
        for (String line : lines) {
            String[] data = line.split(separator);

            if (data[0].trim().equalsIgnoreCase(title)) {
                return data[1].trim();
            }
        }

        return null;
    }

    /**
     * Reads all lines with the given title
     *
     * @param title title of the line
     * @return list of all lines with the given title, null if no lines were found
     */
    public List<String> readLines(String title) {
        List<String> titledLines = new ArrayList<String>();

        for (String line : lines) {
            String[] data = line.split(separator);

            if (data[0].trim().equalsIgnoreCase(title)) {
                titledLines.add(data[1].trim());
            }
        }

        return titledLines.size() == 0 ? null : titledLines;
    }
}
