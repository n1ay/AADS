package io.github.n1ay.aads.huffman;

public class Config {
    public static final int CODING_TABLE_LENGTH = 32;
    public static final int SYMBOL_COUNT_LENGTH = 32;
    public static final int SYMBOL_LENGTH = 8;
    public static final int SHORTEST_SYMBOL_LENGTH = 8;
    public static final int HEADER_LENGTH = CODING_TABLE_LENGTH + SYMBOL_COUNT_LENGTH + SYMBOL_LENGTH + SHORTEST_SYMBOL_LENGTH;
}
