import java.util.*;
import java.io.*;

public class Main {
    public static void kmpSearch(String text, String pattern) {
        int textLen = text.length();
        int patternLen = pattern.length();
        int[] pi = new int[patternLen];
        int j = 0;
        for (int l = 1; l < patternLen; l++) {
            while (j > 0 && pattern.charAt(j) != pattern.charAt(l)) {
                j = pi[j - 1];
            }
            if (pattern.charAt(j) == pattern.charAt(l)) {
                j++;
            }
            pi[l] = j;
        }

        int k = 0;
        for (int i = 0; i < textLen; i++) {
            while (k > 0 && pattern.charAt(k) != text.charAt(i)) {
                k = pi[k - 1];
            }
            if (pattern.charAt(k) == text.charAt(i)) {
                k++;
            }
            if (k == patternLen) {
                System.out.print((i - patternLen + 1) + " ");
                k = pi[k - 1];
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String pattern = br.readLine();
        String text = br.readLine();
        br.close();
        kmpSearch(text, pattern);
    }
}
