package io.github.n1ay.aads.huffman;

import java.util.HashMap;

public class AppMain {
    public static void main(String[] args) {
        final int symbolLength = 3;
        HuffmanEncoder huffmanEncoder = new HuffmanEncoder();
        HashMap<String, Integer> symbolCount = huffmanEncoder.calculateSymbolFrequency("seneca.txt", symbolLength);
        HuffmanNode huffmanTree = huffmanEncoder.buildHuffmanTree(symbolCount);
        HashMap<String, String> codingTable = huffmanEncoder.generateCodingTable(huffmanTree);
        System.out.println(codingTable);
    }
}
