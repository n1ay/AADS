import io.github.n1ay.aads.huffman.Utils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static io.github.n1ay.aads.huffman.Config.TXT_DIR;

public class Mock {

    static List<String> testFilenames;

    static {
        testFilenames = new LinkedList<>();
        testFilenames.add("test1.txt");
        testFilenames.add("test2.txt");
        testFilenames.add("seneca.txt");
        testFilenames.add("vuldat.txt");
        testFilenames.add("Latin-Lipsum.txt");
    }

    public static List<byte[]> mockTexts() {
        List<byte[]> texts = new ArrayList<>();
        for (String s: testFilenames) {
            texts.add(Utils.readFile(TXT_DIR + s).getBytes(StandardCharsets.US_ASCII));
        }

        return texts;
    }
}
