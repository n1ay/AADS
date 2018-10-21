package io.github.n1ay.aads.huffman;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

class HuffmanEncoder {

    HashMap<String, Integer> calculateSymbolFrequency(String filename, int symbolLength) {
        String text = "";
        try {
            text = new String(Files.readAllBytes(Paths.get(filename)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        HashMap<String, Integer> symbolCount = new HashMap<>();

        String symbol;
        for (int i = 0; i < text.length(); i+=symbolLength) {
            if (i + symbolLength < text.length()) {
                symbol = text.substring(i, i + symbolLength);
            } else {
                symbol = text.substring(i);
            }

            Integer currentSymbolCount = symbolCount.get(symbol);
            if (currentSymbolCount != null) {
                symbolCount.put(symbol, currentSymbolCount + 1);
            } else
                symbolCount.put(symbol, 1);
        }

        return symbolCount;
    }

    HuffmanNode buildHuffmanTree(HashMap<String, Integer> symbolCount) {
        TreeSet<HuffmanNode> huffmanNodeList = new TreeSet<>((node1, node2) -> node1.getSymbolCount()
                + node1.getChildrenSymbolCount() - node2.getSymbolCount() - node2.getChildrenSymbolCount());

        for (String symbol : symbolCount.keySet()) {
            huffmanNodeList.add(new HuffmanNode(symbol, symbolCount.get(symbol)));
        }

        while (huffmanNodeList.size() > 1) {
            HuffmanNode first = huffmanNodeList.first();
            huffmanNodeList.remove(first);
            HuffmanNode second = huffmanNodeList.first();
            huffmanNodeList.remove(second);

            HuffmanNode huffmanNode = new HuffmanNode();
            huffmanNode.setChildren(first, second);

            huffmanNodeList.add(huffmanNode);
        }
        return huffmanNodeList.first();
    }

    HashMap<String, String> generateCodingTable(HuffmanNode huffmanTree) {
        HashMap<String, String> codingTable = new HashMap<>();
        Stack<Integer> codeStack = new Stack<>();
        huffmanTree.traverse(huffmanTree, codeStack, codingTable);

        return codingTable;
    }
}
