package dev.http418.sgmmssimulator.structure;

public class Node<T> {
    private T value;
    private Node<T> left, right;

    Node(T item) {
        value = item;
        left = right = null;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Node<T> getLeft() {
        return left;
    }

    public void setLeft(Node<T> left) {
        this.left = left;
    }

    public Node<T> getRight() {
        return right;
    }

    public void setRight(Node<T> right) {
        this.right = right;
    }
}
