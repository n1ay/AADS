package io.github.n1ay.aads.huffman;

public class AppMain {

    public static void compress(String inputFilename, String outputFilename) {
        byte[] inputContent = Utils.readBinaryFile(inputFilename);
        byte[] inputSample;
        int sampleSize = 10000;
        if (inputContent.length > sampleSize)
            inputSample = Utils.getBytes(inputContent, 0, sampleSize);
        else
            inputSample = inputContent;

        int symbolLength = 1;
        int bestSymbolLength = 1;
        byte[] compressedData = HuffmanEncoder.compress(inputSample, symbolLength);
        int bestFileSize = compressedData.length;
        int stopCounter = 0;
        System.out.println("[INFO] Symbol length=" + symbolLength + ", compression factor=" +
                ((double) (inputContent.length - compressedData.length)) / inputContent.length * 100);

        while (stopCounter < 5) {
            symbolLength++;
            compressedData = HuffmanEncoder.compress(inputSample, symbolLength);
            if (compressedData.length < bestFileSize) {
                bestFileSize = compressedData.length;
                bestSymbolLength = symbolLength;
                stopCounter = 0;
            } else {
                stopCounter++;
            }

            System.out.println("[INFO] Symbol length=" + symbolLength + ", compression factor=" +
                    ((double) (inputContent.length - compressedData.length)) / inputContent.length * 100);
        }

        compressedData = HuffmanEncoder.compress(inputContent, bestSymbolLength);

        Utils.saveBinaryFile(outputFilename, compressedData);
        System.out.println("Best symbol length=" + bestSymbolLength + ", compression factor=" +
                ((double) (inputContent.length - compressedData.length)) / inputContent.length * 100);
    }

    public static void decompress(String compressedFilename, String outputFilename) {
        byte[] compressedData = Utils.readBinaryFile(compressedFilename);
        byte[] decodedText = HuffmanDecoder.decompress(compressedData);

        Utils.saveBinaryFile(outputFilename, decodedText);
    }

    public static void main(String[] args) {

        String errorMsg = "Usage: [java AppMain] c/x inputFile outputFile";

        if (args.length != 3) {
            System.out.println(errorMsg);
            return;
        }
        if (args[0].equals("x")) {
            decompress(args[1], args[2]);
            System.out.println("Successfully extracted an archive. Output saved to " + args[2]);
        } else if (args[0].equals("c")) {
            compress(args[1], args[2]);
            System.out.println("Successfully compressed a file. Output saved to " + args[2]);
        } else {
            System.out.println(errorMsg);
        }
    }
}
