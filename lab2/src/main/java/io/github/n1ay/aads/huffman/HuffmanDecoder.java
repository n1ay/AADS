package io.github.n1ay.aads.huffman;

import java.util.BitSet;
import java.util.HashMap;

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

    public String decodeCodingTable(BitSet codingTableBits, BitSet header) {
        return "EMPTY BODY";
    }
}
