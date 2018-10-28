import io.github.n1ay.aads.huffman.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {

    //@Test
    public void packUnpackTest() {

        List<String> texts = Mock.mockTexts();

        int okCounter = 0;
        int maxSymbolLength = 5;

        for (int symbolLength = 1; symbolLength <= maxSymbolLength; symbolLength++) {
            for (String text : texts) {
                HashMap<String, Integer> symbolCounter = HuffmanEncoder.calculateSymbolFrequency(text, symbolLength);
                HuffmanNode huffmanTree = HuffmanEncoder.buildHuffmanTree(symbolCounter);
                HashMap<String, String> encodingTable = HuffmanEncoder.generateCodingTable(huffmanTree);
                int symbolsCount = symbolCounter.values().stream().reduce(0, Integer::sum);
                byte[] encodedText = HuffmanEncoder.encodeText(text, encodingTable, symbolLength);
                byte[] encodedTable = HuffmanEncoder.encodeCodingTable(encodingTable, symbolsCount);

                byte[] packedData = Utils.packBytes(encodedTable, encodedText);
                HuffmanData unpackedData = Utils.unpackBytes(packedData);


                int equalsCount = 0;

                for (int i = 0; i < encodedTable.length; i++) {
                    if (encodedTable[i] == unpackedData.getHeader()[i])
                        equalsCount++;
                }

                for (int i = 0; i < encodedText.length; i++) {
                    if (encodedText[i] == unpackedData.getContent()[i])
                        equalsCount++;
                }

                if (encodedTable.length + encodedText.length == equalsCount) ;
                okCounter++;

            }
        }

        assertEquals(okCounter, maxSymbolLength * texts.size());
    }
}
