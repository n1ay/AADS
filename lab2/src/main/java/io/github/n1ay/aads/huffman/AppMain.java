package io.github.n1ay.aads.huffman;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class AppMain {
    public static void compressDecompress() {
        final int symbolLength = 2;
        final String TABLE_FILE = "dtable.tmp";
        final String BIN_RAW_FILE = "bin.txt";

        //encode phase
        HuffmanEncoder huffmanEncoder = new HuffmanEncoder();
        String text = Utils.readFile("seneca.txt");
//        String text = "abababcacaweweweweweweweddfghythhgj";
        HashMap<String, Integer> symbolCounter = huffmanEncoder.calculateSymbolFrequency(text, symbolLength);
        HuffmanNode huffmanTree = huffmanEncoder.buildHuffmanTree(symbolCounter);
        HashMap<String, String> encodingTable = huffmanEncoder.generateCodingTable(huffmanTree);
        String encodedText = huffmanEncoder.encodeText(text, encodingTable, symbolLength);
        HashMap<String, String> decodingTable = Utils.invertMap(encodingTable);
        String serializedDecodingTable = huffmanEncoder.serializeDecodingTable(decodingTable);
        int symbols = symbolCounter.values().stream().reduce(0, Integer::sum);
        Utils.saveFile(TABLE_FILE, symbols + "\n" + serializedDecodingTable);
        Utils.saveFile(BIN_RAW_FILE, encodedText);
        Utils.saveStringInBinaryForm();
        HuffmanDecoder huffmanDecoder = new HuffmanDecoder();

        String de = huffmanDecoder.decodeText(encodedText, decodingTable, symbols);
        Utils.saveFile("test", de);

//        String encodedText2 = Utils.readBinaryFileToString();
//        String[] tableFileContent = Utils.readFile(TABLE_FILE).split("\\n", 2);
//        int symbols2 = Integer.parseInt(tableFileContent[0]);
//        HashMap<String, String> decodingTable2 = huffmanDecoder.deserializeDecodingTable(tableFileContent[1]);
//
//        String decodedText = huffmanDecoder.decodeText(encodedText2, decodingTable2, symbols2);
//        Utils.saveFile("seneca2.txt", decodedText);
//
//        decode phase

//        String decodedText = huffmanDecoder.decodeText(huffmanData.getContent(), decodingTable);
//        Utils.saveFile("seneca2.txt", decodedText);
        System.out.println("");
    }

    public static void findOptimal() {
        int symbolLength = 1;
        int bestSymbol = 1;
        double compressionFactor = 0;
        double bestCompressionFactor = 0;
        int worseScenarioCounter = 0;
        int fileSize = Utils.getFileLength("seneca.txt");
        System.out.println("original file size: " + fileSize);
        while (true) {
            final String TABLE_FILE = "dtable.tmp";
            final String BIN_RAW_FILE = "bin.txt";

            //encode phase
            HuffmanEncoder huffmanEncoder = new HuffmanEncoder();
            String text = Utils.readFile("seneca.txt");
            HashMap<String, Integer> symbolCounter = huffmanEncoder.calculateSymbolFrequency(text, symbolLength);
            HuffmanNode huffmanTree = huffmanEncoder.buildHuffmanTree(symbolCounter);
            HashMap<String, String> encodingTable = huffmanEncoder.generateCodingTable(huffmanTree);
            String encodedText = huffmanEncoder.encodeText(text, encodingTable, symbolLength);
            HashMap<String, String> decodingTable = Utils.invertMap(encodingTable);
            String serializedDecodingTable = huffmanEncoder.serializeDecodingTable(decodingTable);
            int symbols = symbolCounter.values().stream().reduce(0, Integer::sum);
            Utils.saveFile(TABLE_FILE, symbols + "\n" + serializedDecodingTable);
            Utils.saveFile(BIN_RAW_FILE, encodedText);
            Utils.saveStringInBinaryForm();
            int tableSize = Utils.getFileLength(TABLE_FILE);
            int compressedFileSize = Utils.getFileLength("enc.bin");

            int fullSize = tableSize + compressedFileSize;

            compressionFactor = 100 * (fileSize - fullSize) / compressedFileSize;
            System.out.println("compression: " + compressionFactor + " symbolLength: " + symbolLength + " fileSize: " + fullSize);
            if (compressionFactor > bestCompressionFactor) {
                bestCompressionFactor = compressionFactor;
                bestSymbol = symbolLength;
                worseScenarioCounter = 0;
            } else
                worseScenarioCounter++;

            if (worseScenarioCounter >= 3)
                break;
            symbolLength++;
        }

        System.out.println("best symbol length = " + bestSymbol + " compression: " + bestCompressionFactor);
    }

    public static void main(String[] args) {
        compressDecompress();
//        findOptimal();

    }
}
