/* Using the Stein's Algorithm */

import java.util.Scanner;

public class gcd {
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
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int result = GCD.gcd(a, b);
        System.out.println(result);
        scanner.close();
    }
}