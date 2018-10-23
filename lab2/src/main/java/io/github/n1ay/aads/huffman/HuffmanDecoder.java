package io.github.n1ay.aads.huffman;

import java.util.BitSet;
import java.util.HashMap;

public class HuffmanDecoder {

    public HashMap<String, String> invertTable (HashMap<String, String> codingTable) {
        HashMap<String, String> invertedTable = new HashMap<>();
        for (String key: codingTable.keySet())
            invertedTable.put(codingTable.get(key), key);

        return invertedTable;
    }

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
}
