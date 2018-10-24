package io.github.n1ay.aads.huffman;

import java.util.BitSet;

public class HuffmanData {
    private BitSet header;
    private BitSet content;

    public HuffmanData(BitSet header, BitSet content) {
        this.header = header;
        this.content = content;
    }

    public BitSet getHeader() {
        return header;
    }

    public void setHeader(BitSet header) {
        this.header = header;
    }

    public BitSet getContent() {
        return content;
    }

    public void setContent(BitSet content) {
        this.content = content;
    }
}
