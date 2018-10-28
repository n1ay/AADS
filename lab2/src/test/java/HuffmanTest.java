import io.github.n1ay.aads.huffman.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.nio.ByteBuffer;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class HuffmanTest {

    @Test
    public void decodeEncodeTableTest() throws Exception {
        List<String> texts = Mock.mockTexts();

        int okCounter = 0;
        int maxSymbolLength = 5;

        for (int symbolLength = 1; symbolLength <= maxSymbolLength; symbolLength++) {
            for (String text : texts) {
                HashMap<String, Integer> symbolCounter = HuffmanEncoder.calculateSymbolFrequency(text, symbolLength);
                HuffmanNode huffmanTree = HuffmanEncoder.buildHuffmanTree(symbolCounter);
                HashMap<String, String> encodingTable = HuffmanEncoder.generateCodingTable(huffmanTree);
                int symbolsCount = symbolCounter.values().stream().reduce(0, Integer::sum);
                byte[] encodedTable = HuffmanEncoder.encodeCodingTable(encodingTable, symbolsCount);

                DecodingTableInfo tableInfo = HuffmanDecoder.extractHeaderInfo(encodedTable);
                HashMap<String, String> decodingTable = HuffmanDecoder.decodeTable(tableInfo);
                HashMap<String, String> referenceTable = Utils.invertMap(encodingTable);

                Set<String> refKeySet = referenceTable.keySet();
                Set<String> decodedKeySet = decodingTable.keySet();

                if (!refKeySet.containsAll(decodedKeySet))
                    throw new Exception("New values in decoded table");

                if (!decodedKeySet.containsAll(refKeySet))
                    throw new Exception("Decoded table does not contain all values");

                int equalsCounter = 0;

                for (String s : refKeySet) {
                    if (referenceTable.get(s).equals(decodingTable.get(s)))
                        equalsCounter++;
                }

                if (equalsCounter == refKeySet.size()) {
                    okCounter++;
                }
            }
        }

        assertDoesNotThrow((Executable) Exception::new);
        assertEquals(okCounter, maxSymbolLength * texts.size());
    }

    //@Test
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
