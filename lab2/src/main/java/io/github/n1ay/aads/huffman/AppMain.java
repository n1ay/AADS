package io.github.n1ay.aads.huffman;

import java.util.BitSet;
import java.util.HashMap;
import java.util.stream.Stream;

public class AppMain {
    public static void main(String[] args) {
        final int symbolLength = 2;
        HuffmanEncoder huffmanEncoder = new HuffmanEncoder();
//        String text = Utils.readFile("seneca.txt");
        String text = "abababcacaweweweweweweweddfghythhgj";
        HashMap<String, Integer> symbolCounter = huffmanEncoder.calculateSymbolFrequency(text, symbolLength);
        HuffmanNode huffmanTree = huffmanEncoder.buildHuffmanTree(symbolCounter);
        HashMap<String, String> encodingTable = huffmanEncoder.generateCodingTable(huffmanTree);

        BitSet encodedText = huffmanEncoder.encodeText(text, encodingTable, symbolLength);
        System.out.println("before: " + text.length() + ", after:  " + encodedText.length());
//        System.out.println(encodingTable + "\n" + encodedText);

        BitSet encodingTableBinary = huffmanEncoder.encodeCodingTable(encodingTable);
        huffmanEncoder.setHeader(encodingTableBinary, encodingTable, symbolLength);
        for (int i = 0; i < encodingTableBinary.length(); i++) {
            if (encodingTableBinary.get(i))
                System.out.print("1");
            else
                System.out.print("0");
            if (i % 8 == 7)
                System.out.println("");
        }
        Utils.saveBinaryFile(huffmanEncoder.createBytes(encodingTableBinary, encodedText), "xd.txt");

//        String encodedTable = huffmanEncoder.encodeCodingTable(encodingTable, symbolLength);
//        System.out.println(encodedTable);

//        HuffmanDecoder huffmanDecoder = new HuffmanDecoder();
//        String decodedText = huffmanDecoder.decodeText(encodedText, Utils.invertMap(encodingTable));
//        System.out.println("before: " + text + "\nafter:  " + decodedText);
    }
}
