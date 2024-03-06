package com.edteam.reservations.util;

import java.io.*;
import java.util.stream.Collectors;

public class TestUtil {

    static public String fromRequest(String fileName) throws IOException {
        return fromFile("request/".concat(fileName));
    }

    static public String fromResponse(String fileName) throws IOException {
        return fromFile("response/".concat(fileName));
    }

    static public String fromFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/" + fileName));

        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }
}
