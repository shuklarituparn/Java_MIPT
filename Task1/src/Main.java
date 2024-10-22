package Task1.src;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static class GCD {
        public static int gcd(int a, int b) {
            if (a == 0 || b == 0)
                return a | b;

            int shift = Integer.numberOfTrailingZeros(a | b);
            a >>= Integer.numberOfTrailingZeros(a);

            while (b != 0) {
                b >>= Integer.numberOfTrailingZeros(b);
                if (a > b) {
                    int temp = a;
                    a = b;
                    b = temp;
                }
                b -= a;
            }

            return a << shift;
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            int result = GCD.gcd(a, b);
            System.out.println(result);
        } catch (InputMismatchException e) {
            System.err.println("Invalid input! Please enter integers only.");
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}

