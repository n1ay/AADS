import io.github.n1ay.aads.huffman.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HuffmanTest {

    @Test
    public void saveLoadFileTest() throws Exception {

        int equalsCounter = 0;
        List<byte[]> texts = Mock.mockTexts();
        String filename = new Date().toString() + ".tmp";
        for (byte[] bytes : texts) {
            Utils.saveBinaryFile(filename, bytes);
            byte[] loadedBytes = Utils.readBinaryFile(filename);
            if (Arrays.equals(bytes, loadedBytes))
                equalsCounter++;
        }

        File file = new File(filename);
        if (!file.delete())
            throw new Exception("File " + filename + "can't be deleted");

        assertDoesNotThrow((Executable) Exception::new);
        assertEquals(equalsCounter, texts.size());

    }

    @Test
    public void decodeEncodeTableTest() throws Exception {
        List<byte[]> texts = Mock.mockTexts();

        int okCounter = 0;
        int maxSymbolLength = 5;

        for (int symbolLength = 1; symbolLength <= maxSymbolLength; symbolLength++) {
            for (byte[] text : texts) {
                HashMap<List<Byte>, Integer> symbolCounter = HuffmanEncoder.calculateSymbolFrequency(text, symbolLength);
                HuffmanNode huffmanTree = HuffmanEncoder.buildHuffmanTree(symbolCounter);
                HashMap<List<Byte>, String> encodingTable = HuffmanEncoder.generateCodingTable(huffmanTree);
                int symbolsCount = symbolCounter.values().stream().reduce(0, Integer::sum);
                byte[] encodedTable = HuffmanEncoder.encodeCodingTable(encodingTable, symbolsCount);

                DecodingTableInfo tableInfo = HuffmanDecoder.extractHeaderInfo(encodedTable);
                HashMap<String, List<Byte>> decodingTable = HuffmanDecoder.decodeTable(tableInfo);
                HashMap<String, List<Byte>> referenceTable = Utils.invertMap(encodingTable);

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

    @Test
    public void decodeEncodeTextTest() {

        List<byte[]> texts = Mock.mockTexts();

        int equalsCounter = 0;
        int maxSymbolLength = 5;

        for (int symbolLength = 1; symbolLength <= maxSymbolLength; symbolLength++) {
            for (byte[] text : texts) {
                HashMap<List<Byte>, Integer> symbolCounter = HuffmanEncoder.calculateSymbolFrequency(text, symbolLength);
                HuffmanNode huffmanTree = HuffmanEncoder.buildHuffmanTree(symbolCounter);
                HashMap<List<Byte>, String> encodingTable = HuffmanEncoder.generateCodingTable(huffmanTree);
                int symbolsCount = symbolCounter.values().stream().reduce(0, Integer::sum);
                byte[] encodedText = HuffmanEncoder.encodeText(text, encodingTable, symbolLength);

                byte[] decodedText = HuffmanDecoder.decodeText(encodedText, Utils.invertMap(encodingTable), symbolsCount);
                if (Arrays.equals(text, decodedText))
                    equalsCounter++;
            }
        }

        assertEquals(equalsCounter, texts.size() * maxSymbolLength);
    }
}
