package io.github.n1ay.aads.huffman;

import java.nio.ByteBuffer;
import java.util.*;

import static io.github.n1ay.aads.huffman.Config.*;

public class HuffmanEncoder {

    public static HashMap<List<Byte>, Integer> calculateSymbolFrequency(byte[] text, int symbolLength) {
        HashMap<List<Byte>, Integer> symbolCounter = new HashMap<>();

        List<Byte> symbol;
        for (int i = 0; i < text.length; i += symbolLength) {
            if (i + symbolLength < text.length) {
                symbol = Arrays.asList(Utils.toObject(Utils.getBytes(text, i, symbolLength)));
            } else {
                symbol = Arrays.asList(Utils.toObject(Utils.getBytes(text, i)));
            }
            Integer currentSymbolCount = symbolCounter.get(symbol);
            if (currentSymbolCount != null) {
                symbolCounter.put(symbol, currentSymbolCount + 1);
            } else
                symbolCounter.put(symbol, 1);
        }
        return symbolCounter;
    }

    public static HuffmanNode buildHuffmanTree(HashMap<List<Byte>, Integer> symbolCounter) {
        PriorityQueue<HuffmanNode> huffmanNodeList = new PriorityQueue<>((node1, node2) ->
                node1.getChildrenSymbolCount() + node1.getSymbolCount()
                        - node2.getChildrenSymbolCount() - node2.getSymbolCount());

        for (List<Byte> symbol : symbolCounter.keySet()) {
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

    public static HashMap<List<Byte>, String> generateCodingTable(HuffmanNode huffmanTree) {
        HashMap<List<Byte>, String> encodingTable = new HashMap<>();
        Stack<Integer> codeStack = new Stack<>();
        huffmanTree.traverse(codeStack, encodingTable);

        return encodingTable;
    }

    public static List<List<Byte>> extractSymbols(byte[] text, int symbolLength) {
        List<List<Byte>> symbolList = new LinkedList<>();
        List<Byte> symbol;
        for (int i = 0; i < text.length; i += symbolLength) {
            if (i + symbolLength < text.length) {
                symbol = Arrays.asList(Utils.toObject(Utils.getBytes(text, i, symbolLength)));
            } else {
                symbol = Arrays.asList(Utils.toObject(Utils.getBytes(text, i)));
            }
            symbolList.add(symbol);
        }
        return symbolList;
    }

    public static byte[] encodeText(byte[] text, HashMap<List<Byte>, String> encodingTable, int symbolLength) {
        StringBuilder stringBuilder = new StringBuilder();
        List<List<Byte>> symbolList = extractSymbols(text, symbolLength);
        for (List<Byte> symbol : symbolList) {
            stringBuilder.append(encodingTable.get(symbol));
        }
        char[] encodedText = stringBuilder.toString().toCharArray();
        BitSet bitSet = new BitSet(encodedText.length);
        for (int i = 0; i < encodedText.length; i++) {
            if (encodedText[i] == '1')
                bitSet.set(i);
        }

        byte[] bytes = new byte[((encodedText.length % 8) == 0) ? (encodedText.length / 8) : (encodedText.length / 8 + 1)];
        byte[] noZeroPartingBytes = bitSet.toByteArray();
        System.arraycopy(noZeroPartingBytes, 0, bytes, 0, noZeroPartingBytes.length);

        return bytes;
    }

    public static byte[] encodeCodingTable(HashMap<List<Byte>, String> encodingTable, int symbolCount) {
        List<List<Byte>> symbols = new ArrayList<>(encodingTable.keySet());
        symbols.sort(Comparator.comparingInt(List::size));

        int codingTableLengthBits = HEADER_LENGTH;
        for (List<Byte> s : encodingTable.keySet()) {
            codingTableLengthBits += s.size() * 8;
            codingTableLengthBits += 8;
            codingTableLengthBits += encodingTable.get(s).length();
        }
        int sizeBytes = codingTableLengthBits / 8 + (((codingTableLengthBits % 8) == 0) ? 0 : 1);
        byte[] codingTableBytes = new byte [sizeBytes];

        ByteBuffer byteBuffer = ByteBuffer.allocate(CODING_TABLE_LENGTH / 8);
        byteBuffer.putInt(codingTableLengthBits);
        byte[] bytes = byteBuffer.array();
        int index = 0;

        for (int i = 0; i < CODING_TABLE_LENGTH; i++, index++) {
            if (BitUtils.getBit(bytes, i))
                BitUtils.setBit(codingTableBytes, index);
        }

        byteBuffer.clear();
        byteBuffer.putInt(symbolCount);
        bytes = byteBuffer.array();
        for (int i = 0; i < SYMBOL_COUNT_LENGTH; i++, index++) {
            if (BitUtils.getBit(bytes, i)) {
                BitUtils.setBit(codingTableBytes, index);
            }
        }

        bytes = new byte[]{(byte) symbols.get(symbols.size() - 1).size()};
        for (int i = 0; i < SYMBOL_LENGTH; i++, index++) {
            if (BitUtils.getBit(bytes, i)) {
                BitUtils.setBit(codingTableBytes, index);
            }
        }

        bytes = new byte[]{(byte) symbols.get(0).size()};
        for (int i = 0; i < SHORTEST_SYMBOL_LENGTH; i++, index++) {
            if (BitUtils.getBit(bytes, i)) {
                BitUtils.setBit(codingTableBytes, index);
            }
        }


        for (List<Byte> currentSymbol : symbols) {
            for (byte ignored : currentSymbol) {
                for (int k = 0; k < 8; k++, index++) {
                    if (BitUtils.getBit(new byte[]{ignored}, k))
                        BitUtils.setBit(codingTableBytes, index);
                }
            }

            String currentCode = encodingTable.get(currentSymbol);
            int currentCodeLength = currentCode.length();
            byte[] currentCodeLengthBytes = {(byte) currentCodeLength};
            for (int k = 0; k < 8; k++, index++) {
                if (BitUtils.getBit(currentCodeLengthBytes, k))
                    BitUtils.setBit(codingTableBytes, index);
            }

            for (int k = 0; k < currentCodeLength; k++, index++) {
                if (currentCode.charAt(k) == '1')
                    BitUtils.setBit(codingTableBytes, index);
            }
        }
        return codingTableBytes;
    }

    public static byte[] compress(byte[] text, int symbolLength) {
        HashMap<List<Byte>, Integer> symbolCounter = calculateSymbolFrequency(text, symbolLength);
        HuffmanNode huffmanTree = buildHuffmanTree(symbolCounter);
        HashMap<List<Byte>, String> encodingTable = generateCodingTable(huffmanTree);
        byte[] encodedText = encodeText(text, encodingTable, symbolLength);
        int symbolCount = symbolCounter.values().stream().reduce(0,Integer::sum);
        byte[] encodedTable = encodeCodingTable(encodingTable, symbolCount);
        return Utils.packBytes(encodedTable, encodedText);
    }
}
