package io.github.n1ay.aads.huffman;

import java.nio.ByteBuffer;
import java.util.*;

import static io.github.n1ay.aads.huffman.Config.*;

public class HuffmanDecoder {

    public static byte[] decodeText(byte[] encodedText, HashMap<String, List<Byte>> decodingTable, int symbols) {
        List<Byte> text = new ArrayList<>();
        StringBuilder code = new StringBuilder();
        List<Byte> symbol;
        int symbolCount = 0;
        BitSet encodedBits = BitSet.valueOf(encodedText);

        for (int i = 0; i < encodedText.length * 8 && symbolCount < symbols; i++) {
            if (encodedBits.get(i)) {
                code.append(1);
            } else {
                code.append(0);
            }

            symbol = decodingTable.get(code.toString());
            if (symbol != null) {
                Utils.appendBytes(text, Utils.toPrimitive(symbol.toArray(new Byte[]{})));
                code.setLength(0);
                symbolCount++;
            }
        }

        return Utils.toPrimitive(text.toArray(new Byte[]{}));
    }

    public static DecodingTableInfo extractHeaderInfo(byte[] header) {
        int headerLength;
        int symbolCount;
        int symbolLength;
        int shortestSymbolLength;

        byte[] buffer = new byte[CODING_TABLE_LENGTH / 8];
        System.arraycopy(header, 0, buffer, 0, CODING_TABLE_LENGTH / 8);
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        headerLength = byteBuffer.getInt();

        System.arraycopy(header, CODING_TABLE_LENGTH / 8, buffer, 0, SYMBOL_COUNT_LENGTH / 8);
        byteBuffer = ByteBuffer.wrap(buffer);
        symbolCount = byteBuffer.getInt();

        buffer = new byte[]{0};
        for (int i = 0; i < SYMBOL_LENGTH; i++) {
            if (BitUtils.getBit(header, CODING_TABLE_LENGTH + SYMBOL_COUNT_LENGTH + i))
                BitUtils.setBit(buffer, i);
        }

        symbolLength = buffer[0];

        buffer = new byte[]{0};
        for (int i = 0; i < SHORTEST_SYMBOL_LENGTH; i++) {
            if (BitUtils.getBit(header, CODING_TABLE_LENGTH + SYMBOL_COUNT_LENGTH + SYMBOL_LENGTH + i))
                BitUtils.setBit(buffer, i);
        }

        shortestSymbolLength = buffer[0];

        byte[] content = new byte[header.length - HEADER_LENGTH / 8];
        System.arraycopy(header, HEADER_LENGTH / 8, content, 0, content.length);

        return new DecodingTableInfo(content, headerLength, symbolCount, symbolLength, shortestSymbolLength);
    }

    public static HashMap<String, List<Byte>> decodeTable(DecodingTableInfo tableInfo) {
        HashMap<String, List<Byte>> decodingTable = new HashMap<>();
        byte[] tableBytes = tableInfo.getDecodingTableBytes();
        byte[] symbolBytes = new byte[1];
        byte[] prefixLengthBytes = new byte[1];
        StringBuilder prefixBuilder = new StringBuilder();

        byte[] symbol = new byte[tableInfo.getShortestSymbolLength()];

        int index = 0;
        for (int i = 0; i < tableInfo.getShortestSymbolLength(); i++) {
            for (int j = 0; j < 8; j++, index++) {
                if (BitUtils.getBit(tableBytes, index))
                    BitUtils.setBit(symbolBytes, j);
            }
            symbol[i] = symbolBytes[0];
            symbolBytes[0] = 0;
        }

        for (int i = 0; i < 8; i++, index++) {
            if (BitUtils.getBit(tableBytes, index))
                BitUtils.setBit(prefixLengthBytes, i);
        }

        int prefixLength = prefixLengthBytes[0];

        for (int i = 0; i < prefixLength; i++, index++) {
            if (BitUtils.getBit(tableBytes, index))
                prefixBuilder.append(1);
            else
                prefixBuilder.append(0);
        }

        decodingTable.put(prefixBuilder.toString(), Arrays.asList(Utils.toObject(symbol)));
        prefixBuilder.setLength(0);
        symbol = new byte[tableInfo.getSymbolLength()];
        prefixLengthBytes[0] = 0;
        symbolBytes[0] = 0;

        while (index < tableInfo.getHeaderLength() - HEADER_LENGTH) {

            for (int i = 0; i < tableInfo.getSymbolLength(); i++) {
                for (int j = 0; j < 8; j++, index++) {
                    if (BitUtils.getBit(tableBytes, index))
                        BitUtils.setBit(symbolBytes, j);
                }
                symbol[i] = symbolBytes[0];
                symbolBytes[0] = 0;
            }

            for (int i = 0; i < 8; i++, index++) {
                if (BitUtils.getBit(tableBytes, index))
                    BitUtils.setBit(prefixLengthBytes, i);
            }

            prefixLength = prefixLengthBytes[0];

            for (int i = 0; i < prefixLength; i++, index++) {
                if (BitUtils.getBit(tableBytes, index))
                    prefixBuilder.append(1);
                else
                    prefixBuilder.append(0);
            }

            decodingTable.put(prefixBuilder.toString(), Arrays.asList(Utils.toObject(symbol)));
            prefixBuilder.setLength(0);
            symbol = new byte[tableInfo.getSymbolLength()];
            prefixLengthBytes[0] = 0;
            symbolBytes[0] = 0;
        }

        return decodingTable;
    }

    public static byte[] decompress(byte[] compressed) {
        HuffmanData huffmanData = Utils.unpackBytes(compressed);
        DecodingTableInfo tableInfo = extractHeaderInfo(huffmanData.getHeader());
        HashMap<String, List<Byte>> decodingTable = decodeTable(tableInfo);
        return decodeText(huffmanData.getContent(), decodingTable, tableInfo.getSymbolCount());
    }
}
