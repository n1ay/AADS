import io.github.n1ay.aads.huffman.*;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class HuffmanTest {

    @Test
    public void test() {
        HashMap<String, String> encodingTable = new HashMap<>();
        encodingTable.put("a", "00");
        encodingTable.put("as", "11");
        encodingTable.put("sa", "01");
        encodingTable.put("aa", "10");
        int symbolCount = 11;
        byte[] encodedTable = HuffmanEncoder.encodeCodingTable(encodingTable, symbolCount);

        DecodingTableInfo info = HuffmanDecoder.extractHeaderInfo(encodedTable);
        HuffmanDecoder.decodeTable(info);
    }

    //@Test
    public void decodeEncodeTextTest() {

        List<String> texts = new LinkedList<>();

        texts.add("asdasdasdaswwqeqwdasdasdasd\r\n\r\nsdfsdfewfe w  fs    sdf sd fsd f  d fsd ");
        texts.add("jfhdsgioehfoe48f7jo 948 jrefi8ajpfi8uajef o4[3qf]jq3-wr]w3rjapfaorafj0i3q2mp3o0iqx[ 23r'qo2i3r[q2oix3r[m'ewiorfaeawepf iora;fkawmfp;oi aemf4p; oai4fmp ;a iafmswf od; ;iqdO,3WP[ ,9o [9 [ ]R]]\r\nfafwea33  we niufanefwnayio a8reugynoawe8rgunoseruzosifnzslkijedfhzkuierhfoweuiyg anor ngjh i iufuhfhif ewu fuahewfiiawu fjoserizfjaiumfaw ierf l zsd mlkfjz hk uzdfkjghdsknskkj u djfh ksehfiwu hfaw");
        texts.add(Utils.readFile("seneca.txt"));
        texts.add(Utils.readFile("vuldat.txt"));
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
