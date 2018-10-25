package io.github.n1ay.aads.huffman;

import java.util.BitSet;
import java.util.HashMap;
import java.util.stream.Stream;

public class AppMain {
    public static void main(String[] args) {
        final int symbolLength = 2;

        //encode phase
        HuffmanEncoder huffmanEncoder = new HuffmanEncoder();
//        String text = Utils.readFile("seneca.txt");
        String text = "abababcacaweweweweweweweddfghythhgj";
        HashMap<String, Integer> symbolCounter = huffmanEncoder.calculateSymbolFrequency(text, symbolLength);
        HuffmanNode huffmanTree = huffmanEncoder.buildHuffmanTree(symbolCounter);
        HashMap<String, String> encodingTable = huffmanEncoder.generateCodingTable(huffmanTree);

        BitSet encodedText = huffmanEncoder.encodeText(text, encodingTable, symbolLength);
//        System.out.println("before: " + text.length() + ", after:  " + encodedText.length());

        BitSet encodingTableBinary = huffmanEncoder.encodeCodingTable(encodingTable);
        huffmanEncoder.setHeader(encodingTableBinary, encodingTable, symbolLength);

//        Utils.printBitSet(encodedText, 0);

        byte[] bytes = huffmanEncoder.createBytes(encodingTableBinary, encodedText);
        Utils.saveBinaryFile(bytes, "seneca-huff.txt");

//        String decodedText = huffmanDecoder.decodeText(encodedText, Utils.invertMap(encodingTable));
//        System.out.println("before: " + text + "\nafter:  " + decodedText);


        //decode phase
        HuffmanDecoder huffmanDecoder = new HuffmanDecoder();
        byte[] encodedBytes = Utils.readBinaryFile("seneca-huff.txt");
        HuffmanData huffmanData = huffmanDecoder.unpackBytes(encodedBytes);
        HashMap<String, String> decodingTable = huffmanDecoder.decodeCodingTable(huffmanData.getHeader());
        String decodedText = huffmanDecoder.decodeText(huffmanData.getContent(), decodingTable);
        Utils.saveFile("seneca2.txt", decodedText);

    }
}
