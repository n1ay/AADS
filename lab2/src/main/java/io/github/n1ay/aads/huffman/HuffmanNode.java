package io.github.n1ay.aads.huffman;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class HuffmanNode {
    private HuffmanNode parent;
    private HuffmanNode left;
    private HuffmanNode right;
    private List<Byte> symbol;
    private int symbolCount;
    private int childrenSymbolCount;

    HuffmanNode(List<Byte> symbol, int symbolCount) {
        this.symbol = symbol;
        this.symbolCount = symbolCount;
        this.childrenSymbolCount = 0;
    }

    HuffmanNode() {
        this.symbol = null;
        this.symbolCount = 0;
        this.childrenSymbolCount = 0;
    }

    public void setChildren(HuffmanNode left, HuffmanNode right) {
        this.left = left;
        this.right = right;
        childrenSymbolCount = left.childrenSymbolCount + left.symbolCount
                + right.childrenSymbolCount + right.symbolCount;
        left.parent = this;
        right.parent = this;
    }

    public void traverse(Stack<Integer> codeStack, HashMap<List<Byte>, String> encodingTable) {
        if (symbol != null) {
            StringBuilder code = new StringBuilder();
            for(Integer i: codeStack) {
                code.append(i);
            }
            encodingTable.put(symbol, code.toString());
        }
        if (left != null) {
            codeStack.push(0);
            left.traverse(codeStack, encodingTable);
            codeStack.pop();
        }
        if (right != null) {
            codeStack.push(1);
            right.traverse(codeStack, encodingTable);
            codeStack.pop();
        }
    }

    @Override
    public String toString() {
        return "symbol: " + symbol + ", symbolCount: " + symbolCount + ", childrenSymbolCount: " + childrenSymbolCount
                + ", left: {" + (left == null ? "null" : left.toString())
                + "}, right: {" + (right == null ? "null" : right.toString()) + "}";
    }

    public HuffmanNode getParent() {
        return parent;
    }

    public void setParent(HuffmanNode parent) {
        this.parent = parent;
    }

    public HuffmanNode getLeft() {
        return left;
    }

    public void setLeft(HuffmanNode left) {
        this.left = left;
    }

    public HuffmanNode getRight() {
        return right;
    }

    public void setRight(HuffmanNode right) {
        this.right = right;
    }

    public List<Byte> getSymbol() {
        return symbol;
    }

    public void setSymbol(List<Byte> symbol) {
        this.symbol = symbol;
    }

    public int getSymbolCount() {
        return symbolCount;
    }

    public void setSymbolCount(int symbolCount) {
        this.symbolCount = symbolCount;
    }

    public int getChildrenSymbolCount() {
        return childrenSymbolCount;
    }

    public void setChildrenSymbolCount(int childrenSymbolCount) {
        this.childrenSymbolCount = childrenSymbolCount;
    }
}
