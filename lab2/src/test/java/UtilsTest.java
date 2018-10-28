import io.github.n1ay.aads.huffman.HuffmanData;
import io.github.n1ay.aads.huffman.HuffmanEncoder;
import io.github.n1ay.aads.huffman.Utils;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {
    @Test
    public void packUnpackTest() {
        HashMap<String, String> encodingTable = new HashMap<>();
        encodingTable.put("a", "00");
        encodingTable.put("as", "11");
        encodingTable.put("sa", "01");
        encodingTable.put("aa", "10");
        int symbolLength = 2;


        String text = "aasasassaaaaasaaaasaa";
        int symbolCount = 11;
        byte[] encodedText = HuffmanEncoder.encodeText(text, encodingTable, symbolLength);
        byte[] encodedTable = HuffmanEncoder.encodeCodingTable(encodingTable, symbolCount);
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

        assertEquals(equalsCount, encodedTable.length + encodedText.length);
    }
}
