package io.github.n1ay.aads.huffman;

import java.util.BitSet;
import java.util.HashMap;

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
}
