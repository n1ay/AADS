package io.github.n1ay.aads.huffman;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.HashMap;

import static io.github.n1ay.aads.huffman.Config.HEADER_LENGTH;

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

    public static void printBitSet(BitSet bitSet) {
        printBitSet(bitSet, 0);
    }

    public static void printBitSet(BitSet bitSet, int cols) {
        for (int i = 0; i < bitSet.length(); i++) {
            if (bitSet.get(i))
                System.out.print(1);
            else
                System.out.print(0);
            if ((cols > 0) && (i % cols == (cols - 1)))
                System.out.println("");
        }
        System.out.println("");
    }

    public static String readByteFromBitStream(BitSet bitStream, int position) {
        byte[] characters;
        BitSet characterBits = bitStream.get(position, position + 8);
        characters = characterBits.toByteArray();
        return new String(characters, StandardCharsets.UTF_8);
    }

    public static byte[] readBinaryFile (String filename) {
        byte[] bytes = null;
        try {
            FileInputStream fis = new FileInputStream(filename);
            long fileSize = (new File(filename)).length();
            bytes = new byte[(int) fileSize];
            fis.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    public static void saveFile(String filename, String text) {
        try {
            PrintWriter out = new PrintWriter(filename);
            out.print(text);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
