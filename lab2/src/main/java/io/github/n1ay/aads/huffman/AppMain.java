package io.github.n1ay.aads.huffman;

import java.util.HashMap;

public class AppMain {
    public static void main(String[] args) {
        final int symbolLength = 2;
        HuffmanEncoder huffmanEncoder = new HuffmanEncoder();
        String text = huffmanEncoder.readFile("seneca.txt");
        HashMap<String, Integer> symbolCounter = huffmanEncoder.calculateSymbolFrequency(text, symbolLength);
        HuffmanNode huffmanTree = huffmanEncoder.buildHuffmanTree(symbolCounter);
        HashMap<String, String> codingTable = huffmanEncoder.generateCodingTable(huffmanTree);

        String encodedText = huffmanEncoder.encodeText(text, codingTable, symbolLength);
        System.out.println("before: " + text.length() + ", after:" + encodedText.length()/8);
        System.out.println(codingTable + "\n" + encodedText);
    }
}
