package dev.http418.sgmmssimulator.structure;


import java.util.ArrayList;
import java.util.List;

public class BST<T extends Comparable<T>> {
    private Node<T> root;

    public BST() {
        this.root = null;
    }

    public void insert(T value) {
        root = insertRec(root, value);
    }

    private Node<T> insertRec(Node<T> root, T value) {
        if (root == null) {
            return new Node<>(value);
        }
        if (value.compareTo(root.getValue()) < 0) {
            root.setLeft(insertRec(root.getLeft(), value));
        } else if (value.compareTo(root.getValue()) > 0) {
            root.setRight(insertRec(root.getRight(), value));
        }
        return root;
    }
    public void delete(T value) {
        root = deleteRec(root, value);
    }

    private Node<T> deleteRec(Node<T> root, T value) {
        if (root == null) {
            return null;
        }
        if (value.compareTo(root.getValue()) < 0) {
            root.setLeft(deleteRec(root.getLeft(), value));
        } else if (value.compareTo(root.getValue()) > 0) {
            root.setRight(deleteRec(root.getRight(), value));
        } else {
            // Node with only one child or no child
            if (root.getLeft() == null) {
                return root.getRight();
            } else if (root.getRight() == null) {
                return root.getLeft();
            }
            // Node with two children: Get the inorder successor (smallest in the right subtree)
            root.setValue(minValue(root.getRight()));
            // Delete the inorder successor
            root.setRight(deleteRec(root.getRight(), root.getValue()));
        }
        return root;
    }
    private T minValue(Node<T> root) {
        T minValue = root.getValue();
        while (root.getLeft() != null) {
            minValue = root.getLeft().getValue();
            root = root.getLeft();
        }
        return minValue;
    }
    public List<T> inOrder() {
        List<T> list = new ArrayList<>();
        inOrderRec(root, list);
        return list;
    }

    private void inOrderRec(Node<T> node, List<T> list) {
        if (node != null) {
            inOrderRec(node.getLeft(), list);
            list.add(node.getValue());
            inOrderRec(node.getRight(), list);
        }
    }
    public int size() {
        return sizeRec(root);
    }

    private int sizeRec(Node<T> node) {
        if (node == null) return 0;
        return 1 + sizeRec(node.getLeft()) + sizeRec(node.getRight());
    }

}
