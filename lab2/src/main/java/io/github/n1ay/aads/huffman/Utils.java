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

    public static String runCommand(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder out = new StringBuilder();
            String s;
            while ((s = stdInput.readLine()) != null) {
                out.append(s);
            }

            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static int getFileLength(String filename) {
        return Integer.parseInt(Utils.runCommand("./binop.sh ls " + filename));
    }

    public static void saveStringInBinaryForm() {
        runCommand("./binop.sh save ");
    }

    public static String readBinaryFileToString() {
        runCommand("./binop.sh read");
        return readFile("bin.txt");
    }
}
