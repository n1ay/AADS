package io.github.n1ay.aads.huffman;

import java.util.BitSet;
import java.util.HashMap;

public class AppMain {
    public static void main(String[] args) {
        final int symbolLength = 2;
        HuffmanEncoder huffmanEncoder = new HuffmanEncoder();
//        String text = huffmanEncoder.readFile("seneca.txt");
        String text = "abababcacaweweweweweweweddfghythhgj";
        HashMap<String, Integer> symbolCounter = huffmanEncoder.calculateSymbolFrequency(text, symbolLength);
        HuffmanNode huffmanTree = huffmanEncoder.buildHuffmanTree(symbolCounter);
        HashMap<String, String> encodingTable = huffmanEncoder.generateCodingTable(huffmanTree);

        BitSet encodedText = huffmanEncoder.encodeText(text, encodingTable, symbolLength);
        System.out.println("before: " + text.length() + ", after:  " + encodedText.length());
//        System.out.println(encodingTable + "\n" + encodedText);

//        String encodedTable = huffmanEncoder.encodeCodingTable(encodingTable, symbolLength);
//        System.out.println(encodedTable);

        HuffmanDecoder huffmanDecoder = new HuffmanDecoder();
        String decodedText = huffmanDecoder.decodeText(encodedText, huffmanDecoder.invertTable(encodingTable));
        System.out.println("before: " + text + "\nafter:  " + decodedText);
    }
}
