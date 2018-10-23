package io.github.n1ay.aads.huffman;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.HashMap;

public class Utils {
    public static String readFile(String filename) {
        String text = "";
        try {
            text = new String(Files.readAllBytes(Paths.get(filename)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    public static HashMap<String, String> invertMap(HashMap<String, String> codingTable) {
        HashMap<String, String> invertedTable = new HashMap<>();
        for (String key: codingTable.keySet())
            invertedTable.put(codingTable.get(key), key);

        return invertedTable;
    }

    public static void saveBinaryFile (byte[] bytes, String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(bytes, 0, bytes.length);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
