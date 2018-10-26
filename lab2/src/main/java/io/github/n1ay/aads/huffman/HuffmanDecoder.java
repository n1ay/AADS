package io.github.n1ay.aads.huffman;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.BitSet;
import java.util.HashMap;

import static io.github.n1ay.aads.huffman.Config.*;

public class HuffmanDecoder {

    public String decodeText(String encodedText, HashMap<String, String> decodingTable, int symbols) {
        StringBuilder text = new StringBuilder();
        StringBuilder code = new StringBuilder();
        String symbol;
        for (int i = 0, j = 0; i < encodedText.length() && j < symbols; i++) {
            if (encodedText.toCharArray()[i] == '1') {
                code.append(1);
            } else {
                code.append(0);
            }

            symbol = decodingTable.get(code.toString());
            if (symbol != null) {
                text.append(symbol);
                j++;
                code.setLength(0);
            }
        }

        return text.toString();
    }

    public HashMap<String, String> deserializeDecodingTable (String serializedDecodingTable) {

        Type type = new TypeToken<HashMap<String, String>>(){}.getType();
        return new Gson().fromJson(serializedDecodingTable, type);
    }
}
