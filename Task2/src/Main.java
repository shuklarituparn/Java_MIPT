import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

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
        Node root;

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

        public void addElement(int data) {
            root = addElement(root, data);
        }

        private void findLeaves(Node current, List<Integer> leaves) {
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

        public List<Integer> getLeaves() {
            List<Integer> leaves = new ArrayList<>();
            findLeaves(root, leaves);
            Collections.sort(leaves);
            return leaves;
        }
    }



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BinaryTree tree = new BinaryTree();

        while (true) {
            int input = scanner.nextInt();
            if (input == 0) {
                break;
            }
            tree.addElement(input);
        }

        List<Integer> leaves = tree.getLeaves();
        for (int leaf : leaves) {
            System.out.print(leaf + " ");
        }
        System.out.println();
    }
}
