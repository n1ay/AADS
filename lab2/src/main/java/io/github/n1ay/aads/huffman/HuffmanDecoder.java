package io.github.n1ay.aads.huffman;

import java.util.BitSet;
import java.util.HashMap;

import static io.github.n1ay.aads.huffman.Config.*;

public class HuffmanDecoder {

    public String decodeText(BitSet encodedBits, HashMap<String, String> decodingTable) {
        StringBuilder text = new StringBuilder();
        StringBuilder code = new StringBuilder();
        String symbol;
        for (int i = 0; i < encodedBits.length(); i++) {
            if (encodedBits.get(i)) {
                code.append(1);
            } else {
                code.append(0);
            }

            symbol = decodingTable.get(code.toString());
            if (symbol != null) {
                text.append(symbol);
                code.setLength(0);
            }
        }

        return text.toString();
    }

    public HuffmanData unpackBytes(byte[] bytes) {
        BitSet bits = BitSet.valueOf(bytes);

        StringBuilder codingTableLength = new StringBuilder();
        for (int i = 0; i < CODING_TABLE_LENGTH; i++) {
            if (bits.get(i))
                codingTableLength.append(1);
            else
                codingTableLength.append(0);
        }
        int headerLength = Integer.parseInt(codingTableLength.toString(), 2);
        BitSet header = bits.get(0, headerLength);

        int skipBits = 8 - headerLength % 8;
        BitSet encodedText = bits.get(headerLength + skipBits, bits.length());

        return new HuffmanData(header, encodedText);
    }

    public HashMap<String, String> decodeCodingTable(BitSet header) {
        StringBuilder symbolLengthBinary = new StringBuilder();
        StringBuilder shortestSymbolBinary = new StringBuilder();

        for (int i = CODING_TABLE_LENGTH; i < CODING_TABLE_LENGTH + SYMBOL_LENGTH; i++) {
            if (header.get(i))
                symbolLengthBinary.append(1);
            else
                symbolLengthBinary.append(0);
        }

        for (int i = CODING_TABLE_LENGTH + SYMBOL_LENGTH;
             i < CODING_TABLE_LENGTH + SYMBOL_LENGTH + SHORTEST_SYMBOL_LENGTH; i++) {
            if (header.get(i))
                shortestSymbolBinary.append(1);
            else
                shortestSymbolBinary.append(0);
        }

        HashMap<String, String> decodingTable = new HashMap<>();

        int symbolLength = Integer.parseInt(symbolLengthBinary.toString(), 2);
        int shortestSymbolLength = Integer.parseInt(shortestSymbolBinary.toString(), 2);

        StringBuilder symbol = new StringBuilder();
        StringBuilder prefix = new StringBuilder();
        StringBuilder nextPrefixLengthBuilder = new StringBuilder();
        int nextPrefixLength;

        int index = HEADER_LENGTH;
        for (int i = 0; i < shortestSymbolLength; i++) {
            symbol.append(Utils.readByteFromBitStream(header, index));
            index += 8;
        }

        for (int i = 0; i < MAX_SYMBOL_LENGTH_BITS; i++) {
                if (header.get(index))
                    nextPrefixLengthBuilder.append(1);
                else
                    nextPrefixLengthBuilder.append(0);

                index++;
        }

        nextPrefixLength = Integer.parseInt(nextPrefixLengthBuilder.toString(), 2);

        for (int i = 0; i < nextPrefixLength; i++) {
            if (header.get(index))
                prefix.append(1);
            else
                prefix.append(0);

            index++;
        }

        decodingTable.put(prefix.toString(), symbol.toString());

        while (index < header.length()) {
            symbol.setLength(0);
            nextPrefixLengthBuilder.setLength(0);
            prefix.setLength(0);

            for (int i = 0; i < symbolLength; i++) {
                symbol.append(Utils.readByteFromBitStream(header, index));
                index += 8;
            }

            for (int i = 0; i < MAX_SYMBOL_LENGTH_BITS; i++) {
                if (header.get(index))
                    nextPrefixLengthBuilder.append(1);
                else
                    nextPrefixLengthBuilder.append(0);

                index++;
            }

            nextPrefixLength = Integer.parseInt(nextPrefixLengthBuilder.toString(), 2);

            for (int i = 0; i < nextPrefixLength; i++) {
                if (header.get(index))
                    prefix.append(1);
                else
                    prefix.append(0);

                index++;
            }

            decodingTable.put(prefix.toString(), symbol.toString());
        }

        return decodingTable;
    }
}
