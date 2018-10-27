package io.github.n1ay.aads.huffman;

import java.util.*;

public class HuffmanEncoder {

    public static HashMap<String, Integer> calculateSymbolFrequency(String text, int symbolLength) {
        HashMap<String, Integer> symbolCounter = new HashMap<>();

        String symbol;
        for (int i = 0; i < text.length(); i += symbolLength) {
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

    public static HuffmanNode buildHuffmanTree(HashMap<String, Integer> symbolCounter) {
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

    public static HashMap<String, String> generateCodingTable(HuffmanNode huffmanTree) {
        HashMap<String, String> encodingTable = new HashMap<>();
        Stack<Integer> codeStack = new Stack<>();
        huffmanTree.traverse(codeStack, encodingTable);

        return encodingTable;
    }

    public static List<String> extractSymbols(String text, int symbolLength) {
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

    public static byte[] encodeText(String text, HashMap<String, String> encodingTable, int symbolLength) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> symbolList = extractSymbols(text, symbolLength);
        for (String symbol : symbolList) {
            stringBuilder.append(encodingTable.get(symbol));
        }
        char[] encodedText = stringBuilder.toString().toCharArray();
        BitSet bitSet = new BitSet(encodedText.length);
        for (int i = 0; i < encodedText.length; i++) {
            if (encodedText[i] == '1')
                bitSet.set(i);
        }

        byte[] bytes = new byte[encodedText.length % 8 == 0 ? encodedText.length / 8 : encodedText.length / 8 + 1];
        byte[] noZeroPartingBytes = bitSet.toByteArray();
        System.arraycopy(noZeroPartingBytes, 0, bytes, 0, noZeroPartingBytes.length);

        return bytes;
    }

    public static byte[] encodeCodingTable(HashMap<String, String> encodingTable) {
        return null;
    }

    public static byte[] createBytes(BitSet header, BitSet text) {
        byte[] headerBytes = header.toByteArray();
        byte[] textBytes = text.toByteArray();

        byte[] bytes = new byte[headerBytes.length + textBytes.length];
        System.arraycopy(headerBytes, 0, bytes, 0, headerBytes.length);
        System.arraycopy(textBytes, 0, bytes, headerBytes.length, textBytes.length);

        return bytes;
    }
}
