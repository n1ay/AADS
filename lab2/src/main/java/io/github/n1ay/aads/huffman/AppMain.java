package io.github.n1ay.aads.huffman;

import java.nio.charset.StandardCharsets;

public class AppMain {

    public static void compress(String inputFilename, String outputFilename) {
        int symbolLength = 2;
        byte[] inputContent = Utils.readFile(inputFilename).getBytes(StandardCharsets.US_ASCII);
        byte[] compressedData = HuffmanEncoder.compress(inputContent, symbolLength);

        Utils.saveBinaryFile(outputFilename, compressedData);
    }

    public static void decompress(String compressedFilename, String outputFilename) {
        byte[] compressedData = Utils.readBinaryFile(compressedFilename);
        byte[] decodedText = HuffmanDecoder.decompress(compressedData);

        Utils.saveFile(outputFilename, new String(decodedText, StandardCharsets.US_ASCII));
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: [java AppMain] c/x input output");
            return;
        }
        if (args[0].equals("x")) {
            decompress(args[1], args[2]);
        } else if (args[0].equals("c")) {
            compress(args[1], args[2]);
        } else {
            System.out.println("Usage: [java AppMain] c/x input output");
        }
    }
}
