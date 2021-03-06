package io.github.n1ay.aads.huffman;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static io.github.n1ay.aads.huffman.Config.CODING_TABLE_LENGTH;

public class Utils {

    public static <V, K> HashMap<V, K> invertMap(HashMap<K, V> codingTable) {
        HashMap<V, K> invertedTable = new HashMap<>();
        for (K key : codingTable.keySet())
            invertedTable.put(codingTable.get(key), key);

        return invertedTable;
    }

    public static void saveBinaryFile(String filename, byte[] bytes) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(bytes, 0, bytes.length);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] readBinaryFile(String filename) {
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

    public static Byte[] toObject(byte[] bytes) {
        Byte[] result = new Byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            Byte b = bytes[i];
            result[i] = b;
        }

        return result;
    }

    public static byte[] toPrimitive(Byte[] bytes) {
        byte[] result = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            result[i] = b;
        }

        return result;
    }

    public static byte[] packBytes(Byte[] headerBytes, Byte[] textBytes) {

        byte[] bytes = new byte[headerBytes.length + textBytes.length];
        System.arraycopy(headerBytes, 0, bytes, 0, headerBytes.length);
        System.arraycopy(textBytes, 0, bytes, headerBytes.length, textBytes.length);

        return bytes;
    }

    public static byte[] packBytes(byte[] headerBytes, byte[] textBytes) {

        byte[] bytes = new byte[headerBytes.length + textBytes.length];
        System.arraycopy(headerBytes, 0, bytes, 0, headerBytes.length);
        System.arraycopy(textBytes, 0, bytes, headerBytes.length, textBytes.length);

        return bytes;
    }

    public static HuffmanData unpackBytes(byte[] bytes) {
        byte[] headerLengthBytes = new byte[CODING_TABLE_LENGTH / 8];
        System.arraycopy(bytes, 0, headerLengthBytes, 0, 4);

        ByteBuffer byteBuffer = ByteBuffer.wrap(headerLengthBytes);
        int headerLength = byteBuffer.getInt();

        byte[] header = new byte[headerLength / 8 + ((headerLength % 8 == 0) ? 0 : 1)];
        byte[] data = new byte[bytes.length - header.length];
        System.arraycopy(bytes, 0, header, 0, header.length);
        System.arraycopy(bytes, header.length, data, 0, data.length);

        return new HuffmanData(header, data);
    }

    public static void printBitSet(BitSet bitSet) {
        printBitSet(bitSet, 0);
    }

    public static void printByteArray(byte[] byteArray) {
        for (int i = 0; i < byteArray.length * 8; i++) {
            if (BitUtils.getBit(byteArray, i)) {
                System.out.print("1");
            } else
                System.out.print("0");

            if (i % 8 == 7)
                System.out.println("");
        }
        System.out.println("");
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

    public static byte[] getBytes(byte[] byteArray, int index) {
        byte[] result = new byte[byteArray.length - index];
        System.arraycopy(byteArray, index, result, 0, result.length);
        return result;
    }

    public static byte[] getBytes(byte[] byteArray, int index, int count) {
        byte[] result = new byte[count];
        System.arraycopy(byteArray, index, result, 0, count);
        return result;
    }

    public static void appendBytes(List<Byte> byteList, byte[] byteArray) {
        for (byte aByteArray : byteArray) {
            byteList.add(aByteArray);
        }
    }

    public static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static List<String> fileToLines(String filename) {
        List<String> lines = new LinkedList<String>();
        String line = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
