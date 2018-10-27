package io.github.n1ay.aads.huffman;

import java.util.List;

public class BitUtils {
    public static void setBit(byte[] byteArray, int index) {
        int bitNumber = index % 8;
        byteArray[index / 8] |= 1 << bitNumber;
    }

    public static void unsetBit(byte[] byteArray, int index) {
        int bitNumber = index % 8;
        byte setByte = 0;
        for (int i = 0; i < 8; i++) {
            if (i != bitNumber) {
                setByte |= byteArray[index / 8] & (1 << i);
            }
        }
        byteArray[index / 8] = setByte;
    }

    public static boolean getBit(byte[] byteArray, int index) {
        int bitNumber = index % 8;
        return (byteArray[index / 8] & (1 << bitNumber)) > 0;
    }

    public static void setBit(List<Byte> byteList, int index) {
        int bitNumber = index % 8;
        byteList.set(index / 8, (byte) (byteList.get(index / 8) | (1 << bitNumber)));
    }

    public static void unsetBit(List<Byte> byteList, int index) {
        int bitNumber = index % 8;
        byte setByte = 0;
        for (int i = 0; i < 8; i++) {
            if (i != bitNumber) {
                setByte |= byteList.get(index / 8) & (1 << i);
            }
        }
        byteList.set(index / 8, setByte);
    }

    public static boolean getBit(List<Byte> byteList, int index) {
        int bitNumber = index % 8;
        return (byteList.get(index / 8) & (1 << bitNumber)) > 0;
    }
}
