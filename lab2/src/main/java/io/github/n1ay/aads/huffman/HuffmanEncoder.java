package io.github.n1ay.aads.huffman;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

class HuffmanEncoder {

    public String readFile(String filename) {
        String text = "";
        try {
            text = new String(Files.readAllBytes(Paths.get(filename)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    HashMap<String, Integer> calculateSymbolFrequency(String text, int symbolLength) {
        HashMap<String, Integer> symbolCounter = new HashMap<>();

        String symbol;
        for (int i = 0; i < text.length(); i+=symbolLength) {
            if (i + symbolLength < text.length()) {
                symbol = text.substring(i, i + symbolLength);
            } else {
                symbol = text.substring(i);
            }
            Integer currentSymbolCount = symbolCounter.get(symbol);
            if (currentSymbolCount != null) {
                symbolCounter.put(symbol, currentSymbolCount + 1);
            } else
                symbolCounter.put(symbol, 1);
        }
        return symbolCounter;
    }

    HuffmanNode buildHuffmanTree(HashMap<String, Integer> symbolCounter) {
        PriorityQueue<HuffmanNode> huffmanNodeList = new PriorityQueue<>((node1, node2) ->
                node1.getChildrenSymbolCount() + node1.getSymbolCount()
                        - node2.getChildrenSymbolCount() - node2.getSymbolCount());

        for (String symbol : symbolCounter.keySet()) {
            huffmanNodeList.add(new HuffmanNode(symbol, symbolCounter.get(symbol)));
        }

        while (huffmanNodeList.size() > 1) {
            HuffmanNode first = huffmanNodeList.poll();
            huffmanNodeList.remove(first);
            HuffmanNode second = huffmanNodeList.poll();
            huffmanNodeList.remove(second);

            HuffmanNode huffmanNode = new HuffmanNode();
            huffmanNode.setChildren(first, second);

            huffmanNodeList.add(huffmanNode);
        }
        return huffmanNodeList.peek();
    }

    HashMap<String, String> generateCodingTable(HuffmanNode huffmanTree) {
        HashMap<String, String> encodingTable = new HashMap<>();
        Stack<Integer> codeStack = new Stack<>();
        huffmanTree.traverse(codeStack, encodingTable);

        return encodingTable;
    }

    public List<String> extractSymbols(String text, int symbolLength) {
        List<String> symbolList = new LinkedList<>();
        String symbol;
        for (int i = 0; i < text.length(); i += symbolLength) {
            if (i + symbolLength < text.length()) {
                symbol = text.substring(i, i + symbolLength);
            } else {
                symbol = text.substring(i);
            }
            symbolList.add(symbol);
        }
        return symbolList;
    }

    public BitSet encodeText(String text, HashMap<String, String> encodingTable, int symbolLength) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> symbolList = extractSymbols(text, symbolLength);
        for(String symbol:symbolList) {
            stringBuilder.append(encodingTable.get(symbol));
        }
        char[] encodedText = stringBuilder.toString().toCharArray();
        BitSet bitSet = new BitSet(encodedText.length);
        for (int i = 0; i < encodedText.length; i++) {
            if (encodedText[i] == '1')
                bitSet.set(i);
        }

        return bitSet;
    }

    public String encodeCodingTable(HashMap<String, String> encodingTable, int symbolLength) {
        StringBuilder encodedTable = new StringBuilder();
        List<String> symbols = new LinkedList<>(encodingTable.keySet());
        symbols.sort(Comparator.comparingInt(String::length));

        for (String symbol: symbols) {
            String value = encodingTable.get(symbol);
            if (value.length() < 10)
                encodedTable.append(symbol).append('0').append(value.length()).append(value);
            else
                encodedTable.append(symbol).append(value.length()).append(value);
        }

        return encodedTable.toString();
    }
}
