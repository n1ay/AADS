package io.github.n1ay.aads.huffman;

public class DecodingTableInfo {
    private byte[] decodingTableBytes;
    private int headerLength;
    private int symbolCount;
    private int symbolLength;
    private int shortestSymbolLength;

    public DecodingTableInfo(byte[] decodingTableBytes, int headerLength, int symbolCount, int symbolLength, int shortestSymbolLength) {
        this.decodingTableBytes = decodingTableBytes;
        this.headerLength = headerLength;
        this.symbolCount = symbolCount;
        this.symbolLength = symbolLength;
        this.shortestSymbolLength = shortestSymbolLength;
    }


    public byte[] getDecodingTableBytes() {
        return decodingTableBytes;
    }

    public void setDecodingTableBytes(byte[] decodingTableBytes) {
        this.decodingTableBytes = decodingTableBytes;
    }

    public int getHeaderLength() {
        return headerLength;
    }

    public void setHeaderLength(int headerLength) {
        this.headerLength = headerLength;
    }

    public int getSymbolCount() {
        return symbolCount;
    }

    public void setSymbolCount(int symbolCount) {
        this.symbolCount = symbolCount;
    }

    public int getSymbolLength() {
        return symbolLength;
    }

    public void setSymbolLength(int symbolLength) {
        this.symbolLength = symbolLength;
    }

    public int getShortestSymbolLength() {
        return shortestSymbolLength;
    }

    public void setShortestSymbolLength(int shortestSymbolLength) {
        this.shortestSymbolLength = shortestSymbolLength;
    }
}
