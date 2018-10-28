package io.github.n1ay.aads.huffman;

public class HuffmanData {
    private byte[] header;
    private byte[] content;

    public HuffmanData(byte[] header, byte[] content) {
        this.header = header;
        this.content = content;
    }

    public byte[] getHeader() {
        return header;
    }

    public void setHeader(byte[] header) {
        this.header = header;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
