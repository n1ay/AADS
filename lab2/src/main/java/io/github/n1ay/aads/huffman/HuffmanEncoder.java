package io.github.n1ay.aads.huffman;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static io.github.n1ay.aads.huffman.Config.*;

class HuffmanEncoder {

    HashMap<String, Integer> calculateSymbolFrequency(String text, int symbolLength) {
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
        for (String symbol : symbolList) {
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

    public BitSet encodeCodingTable(HashMap<String, String> encodingTable) {

        List<String> symbols = new LinkedList<>(encodingTable.keySet());
        symbols.sort(Comparator.comparingInt(String::length));
        List<Integer> lengthsList = new ArrayList<>();
        List<BitSet> bitSetList = new LinkedList<>();
        for (String symbol : symbols) {
            String value = encodingTable.get(symbol);
            bitSetList.add(BitSet.valueOf(symbol.getBytes()));

            lengthsList.add(value.length());
            char[] binaryValueLength = Integer.toBinaryString(value.length()).toCharArray();
            BitSet valueLengthBits = new BitSet(MAX_SYMBOL_LENGTH_BITS);
            for (int i = 0; i < MAX_SYMBOL_LENGTH_BITS; i++) {
                if (MAX_SYMBOL_LENGTH_BITS - i <= binaryValueLength.length &&
                        binaryValueLength[i - MAX_SYMBOL_LENGTH_BITS + binaryValueLength.length] == '1')
                    valueLengthBits.set(i);
            }

            bitSetList.add(valueLengthBits);

            BitSet valueBits = new BitSet(value.length());
            for (int i = 0; i < value.length(); i++) {
                if (value.toCharArray()[i] == '1')
                    valueBits.set(i);
            }

            bitSetList.add(valueBits);
        }
        BitSet codingTableBits = new BitSet(HEADER_LENGTH);

        int index = HEADER_LENGTH;
        for (int i = 0; i < bitSetList.size(); i++) {
            int bitSetLength;
            if (i % 3 == 0) {
                int currentLength = bitSetList.get(i).length();
                bitSetLength = currentLength % 8 == 0 ? currentLength : (currentLength / 8 + 1) * 8;
            }
            else if (i % 3 == 1) {
                bitSetLength = 8;
            } else {
                bitSetLength = lengthsList.get(i/3);
            }
            for (int j = 0; j < bitSetLength; j++, index++) {
                if (bitSetList.get(i).get(j))
                    codingTableBits.set(index);
            }
        }

        return codingTableBits;
    }

    public void setHeader(BitSet codingTableBits, HashMap<String, String> codingTable, int symbolLength) {
        int shortestSymbolLength = symbolLength;
        for (String symbol : codingTable.keySet()) {
            if (symbol.length() < symbolLength) {
                shortestSymbolLength = symbol.length();
                break;
            }
        }

        char[] codingTableLength = Integer.toBinaryString(codingTableBits.length()).toCharArray();
        char[] symbolLengthBinary = Integer.toBinaryString(symbolLength).toCharArray();
        char[] shortestSymbolLengthBinary = Integer.toBinaryString(shortestSymbolLength).toCharArray();

        for (int i = CODING_TABLE_LENGTH - codingTableLength.length; i < CODING_TABLE_LENGTH; i++) {
            if (codingTableLength[i - (CODING_TABLE_LENGTH - codingTableLength.length)] == '1')
                codingTableBits.set(i);
        }

        for (int i = SYMBOL_LENGTH - symbolLengthBinary.length + CODING_TABLE_LENGTH; i < SYMBOL_LENGTH
                + CODING_TABLE_LENGTH; i++) {
            if (symbolLengthBinary[i - (SYMBOL_LENGTH - symbolLengthBinary.length + CODING_TABLE_LENGTH)] == '1')
                codingTableBits.set(i);
        }

        for (int i = SHORTEST_SYMBOL_LENGTH - shortestSymbolLengthBinary.length
                + CODING_TABLE_LENGTH + SYMBOL_LENGTH; i < SHORTEST_SYMBOL_LENGTH
                + SYMBOL_LENGTH + CODING_TABLE_LENGTH; i++) {
            if (codingTableLength[i - (SHORTEST_SYMBOL_LENGTH - shortestSymbolLengthBinary.length
                    + CODING_TABLE_LENGTH + SYMBOL_LENGTH)] == '1')
                codingTableBits.set(i);
        }
    }

    public byte[] createBytes(BitSet header, BitSet text) {
        byte[] headerBytes = header.toByteArray();
        byte[] textBytes = text.toByteArray();

        byte[] bytes = new byte[headerBytes.length + textBytes.length];
        System.arraycopy(headerBytes, 0, bytes, 0, headerBytes.length);
        System.arraycopy(textBytes, 0, bytes, headerBytes.length, textBytes.length);

        return bytes;
    }
}
