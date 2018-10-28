import io.github.n1ay.aads.huffman.BitUtils;
import io.github.n1ay.aads.huffman.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class BitUtilsTest {
    @Test
    public void getSetBitArrayTest() throws Exception {
        String textLower = "abcabc";
        String textUpper = "ABCABC";
        byte[] textLowerBytes = textLower.getBytes(StandardCharsets.US_ASCII);
        byte[] textUpperBytes = textUpper.getBytes(StandardCharsets.US_ASCII);

        if (!BitUtils.getBit(textUpperBytes, 13))
            BitUtils.setBit(textUpperBytes, 13);
        else
            throw new Exception();

        if (BitUtils.getBit(textLowerBytes, 5))
            BitUtils.unsetBit(textLowerBytes, 5);
        else
            throw new Exception();

        if (BitUtils.getBit(textLowerBytes, 5) || !BitUtils.getBit(textUpperBytes, 13)) {
            throw new Exception();
        }
        assertDoesNotThrow((ThrowingSupplier<Exception>) Exception::new);
    }

    @Test
    public void getSetBitListTest() throws Exception {
        String textLower = "abcabc";
        String textUpper = "ABCABC";
        List<Byte> textLowerBytes = new ArrayList<>(Arrays.asList(Utils.toObject(textLower.getBytes(StandardCharsets.US_ASCII))));
        List<Byte> textUpperBytes = new ArrayList<>(Arrays.asList(Utils.toObject(textUpper.getBytes(StandardCharsets.US_ASCII))));

        if (!BitUtils.getBit(textUpperBytes, 13))
            BitUtils.setBit(textUpperBytes, 13);
        else
            throw new Exception();

        if (BitUtils.getBit(textLowerBytes, 5))
            BitUtils.unsetBit(textLowerBytes, 5);
        else
            throw new Exception();

        if (BitUtils.getBit(textLowerBytes, 5) || !BitUtils.getBit(textUpperBytes, 13)) {
            throw new Exception();
        }
        assertDoesNotThrow((ThrowingSupplier<Exception>) Exception::new);
    }
}
