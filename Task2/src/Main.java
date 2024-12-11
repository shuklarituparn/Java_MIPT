package Task2.src;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        BinaryTree tree = new BinaryTree();
        BinaryTree.readInput(tree);
        BinaryTree.printTree(tree);
    }

    static class Node {
        int data;
        Node leftChild;
        Node rightChild;

        public Node(int input) {
            this.data = input;
            leftChild = null;
            rightChild = null;
        }
    }

    static class BinaryTree {
        static HashSet<Integer> elementDict = new HashSet<>();
        Node root;

        private static void findLeaves(Node current, List<Integer> leaves) {
            if (current == null) {
                return;
            }
            if (current.leftChild == null && current.rightChild == null) {
                leaves.add(current.data);
            } else {
                findLeaves(current.leftChild, leaves);
                findLeaves(current.rightChild, leaves);
            }
        }

        public static void readInput(BinaryTree tree) {
            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    int input = scanner.nextInt();
                    if (input == 0) {
                        break;
                    }
                    if (tree.addElement(input)) {
                        BinaryTree.elementDict.add(input);
                    }
                }
            } catch (Exception e) {
                System.out.println("Following error happened " + e.getMessage());
            }

        }

        public static void printTree(BinaryTree tree) {
            List<Integer> leaves = tree.getLeaves();
            for (int leaf : leaves) {
                System.out.print(leaf + " ");
            }
            System.out.println();
        }

        private Node addElement(Node current, int data) {
            if (current == null) {
                return new Node(data);
            }
            if (data < current.data) {
                current.leftChild = addElement(current.leftChild, data);
            } else if (data > current.data) {
                current.rightChild = addElement(current.rightChild, data);
            }
            return current;
        }

        public boolean addElement(int data) {
            if (elementDict.contains(data)) {
                return false;
            }
            root = addElement(root, data);
            return true;

        }

        public List<Integer> getLeaves() {
            List<Integer> leaves = new ArrayList<>();
            findLeaves(root, leaves);
            Collections.sort(leaves);
            return leaves;
        }
    }
}
