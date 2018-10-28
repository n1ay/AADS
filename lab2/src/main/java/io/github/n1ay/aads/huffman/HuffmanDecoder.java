package io.github.n1ay.aads.huffman;

import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.HashMap;

import static io.github.n1ay.aads.huffman.Config.*;

public class HuffmanDecoder {

    public static String decodeText(byte[] encodedText, HashMap<String, String> decodingTable, int symbols) {
        StringBuilder text = new StringBuilder();
        StringBuilder code = new StringBuilder();
        String symbol;
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
                text.append(symbol);
                code.setLength(0);
                symbolCount++;
            }
        }

        return text.toString();
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

    public static HashMap<String, String> decodeTable(DecodingTableInfo tableInfo) {
        HashMap<String, String> decodingTable = new HashMap<>();
        StringBuilder symbol = new StringBuilder();
        byte[] symbolBytes = new byte[tableInfo.getShortestSymbolLength()];

        int index = 0;
        for (int i = 0; i < tableInfo.getShortestSymbolLength() * 8; i++) {
            if (BitUtils.getBit(tableInfo.getDecodingTableBytes(), index))
                BitUtils.setBit(symbolBytes, i);

            index++;
        }

        symbol.append(new String(symbolBytes));

        System.out.println(symbol.toString());

        return null;
    }
}
