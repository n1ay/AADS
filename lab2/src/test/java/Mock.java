import io.github.n1ay.aads.huffman.Utils;

import java.util.ArrayList;
import java.util.List;

import static io.github.n1ay.aads.huffman.Config.TXT_DIR;

public class Mock {
    public static List<String> mockTexts() {
        List<String> texts = new ArrayList<>();
        //texts.add(Utils.readFile(TXT_DIR + "test1.txt"));
        texts.add(Utils.readFile(TXT_DIR + "test2.txt"));
        texts.add(Utils.readFile(TXT_DIR + "seneca.txt"));
//        texts.add(Utils.readFile(TXT_DIR + "vuldat.txt"));
//        texts.add(Utils.readFile(TXT_DIR + "Latin-Lipsum.txt"));

        return texts;
    }
}
