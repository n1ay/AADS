import io.github.n1ay.aads.huffman.*;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class HuffmanTest {

    @Test
    public void decodeEncodeTextTest() {

        List<String> texts = Mock.mockTexts();

        int equalsCounter = 0;
        int maxSymbolLength = 5;

        for (int symbolLength = 1; symbolLength <= maxSymbolLength; symbolLength++) {
            for (String text : texts) {
                HashMap<String, Integer> symbolCounter = HuffmanEncoder.calculateSymbolFrequency(text, symbolLength);
                HuffmanNode huffmanTree = HuffmanEncoder.buildHuffmanTree(symbolCounter);
                HashMap<String, String> encodingTable = HuffmanEncoder.generateCodingTable(huffmanTree);
                int symbolsCount = symbolCounter.values().stream().reduce(0, Integer::sum);
                byte[] encodedText = HuffmanEncoder.encodeText(text, encodingTable, symbolLength);

                String decodedText = HuffmanDecoder.decodeText(encodedText, Utils.invertMap(encodingTable), symbolsCount);
                if (text.equals(decodedText))
                    equalsCounter++;
            }
        }

        assertEquals(equalsCounter, texts.size() * maxSymbolLength);
    }
}
