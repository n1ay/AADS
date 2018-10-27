package io.github.n1ay.aads.huffman;

import java.nio.ByteBuffer;
import java.util.*;

import static io.github.n1ay.aads.huffman.Config.*;

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

    public static byte[] encodeCodingTable(HashMap<String, String> encodingTable, int symbolCount) {
        List<String> symbols = new ArrayList<>(encodingTable.keySet());
        symbols.sort(Comparator.comparingInt(String::length));

        int codingTableLengthBits = HEADER_LENGTH;
        for (String s : encodingTable.keySet()) {
            codingTableLengthBits += s.length() * 8;
            codingTableLengthBits += 8;
            codingTableLengthBits += encodingTable.get(s).length();
        }
        int sizeBytes = codingTableLengthBits / 8 + (((codingTableLengthBits % 8) == 0) ? 0 : 1);
        List<Byte> codingTableBytes = new ArrayList<>(sizeBytes);
        for (int i = 0; i < sizeBytes; i++) {
            codingTableBytes.add((byte) 0);
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.putInt(codingTableLengthBits - HEADER_LENGTH);
        byte[] bytes = byteBuffer.array();
        int index = 0;

        for (int i = 0; i < CODING_TABLE_LENGTH; i++) {
            if (BitUtils.getBit(bytes, i))
                BitUtils.setBit(codingTableBytes, index);

            index++;
        }

        index = CODING_TABLE_LENGTH;

        byteBuffer.clear();
        byteBuffer.putInt(symbolCount);
        bytes = byteBuffer.array();
        for (int i = 0; i < SYMBOL_COUNT_LENGTH; i++) {
            if (BitUtils.getBit(bytes, i)) {
                BitUtils.setBit(codingTableBytes, index);
            }

            index++;
        }

        index = CODING_TABLE_LENGTH + SYMBOL_COUNT_LENGTH;

        bytes = new byte[]{(byte) symbols.get(symbols.size()-1).length()};
        for (int i = 0; i < SYMBOL_LENGTH; i++) {
            if (BitUtils.getBit(bytes, i)) {
                BitUtils.setBit(codingTableBytes, index);
            }

            index++;
        }

        index = CODING_TABLE_LENGTH + SYMBOL_COUNT_LENGTH + SYMBOL_LENGTH;

        bytes = new byte[]{(byte) symbols.get(0).length()};
        for (int i = 0; i < SHORTEST_SYMBOL_LENGTH; i++) {
            if (BitUtils.getBit(bytes, i)) {
                BitUtils.setBit(codingTableBytes, index);
            }

            index++;
        }


        for (String symbol : symbols) {
            byte[] currentSymbol = symbol.getBytes();
            for (byte ignored : currentSymbol) {
                for (int k = 0; k < 8; k++) {
                    if (BitUtils.getBit(currentSymbol, k))
                        BitUtils.setBit(codingTableBytes, index);

                    index++;
                }
            }

            String currentCode = encodingTable.get(symbol);
            int currentCodeLength = currentCode.length();
            byte[] currentCodeLengthBytes = {(byte) currentCodeLength};
            for (int k = 0; k < 8; k++) {
                if (BitUtils.getBit(currentCodeLengthBytes, k))
                    BitUtils.setBit(codingTableBytes, index);

                index++;
            }

            for (int k = 0; k < currentCodeLength; k++) {
                if (currentCode.charAt(k) == '1')
                    BitUtils.setBit(codingTableBytes, index);

                index++;
            }
        }

        return Utils.toPrimitive(codingTableBytes.toArray(new Byte[0]));
    }
}
