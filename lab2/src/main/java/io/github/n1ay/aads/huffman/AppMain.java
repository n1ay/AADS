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

        BitSet encodingTableBinary = huffmanEncoder.encodeCodingTable(encodingTable);
        huffmanEncoder.setHeader(encodingTableBinary, encodingTable, symbolLength);

        Utils.printBitSet(encodedText, 0);

        byte[] bytes = huffmanEncoder.createBytes(encodingTableBinary, encodedText);
        Utils.saveBinaryFile(bytes, "xd.txt");

        HuffmanDecoder huffmanDecoder = new HuffmanDecoder();
//        String decodedText = huffmanDecoder.decodeText(encodedText, Utils.invertMap(encodingTable));
//        System.out.println("before: " + text + "\nafter:  " + decodedText);
        HuffmanData data = huffmanDecoder.unpackBytes(bytes);
        HashMap<String, String> decodingTable = huffmanDecoder.decodeCodingTable(data.getHeader());
        System.out.println(decodingTable);
    }
}
