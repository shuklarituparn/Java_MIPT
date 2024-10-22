package HW1_Task3.src;

import java.util.*;
import java.io.*;

public class Main {
    public static int [] prefixFunction(String pattern){
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
        return pi;

    }
    public static class KMP{
        public static void kmpSearch(String text, String pattern) {
            int textLen = text.length();
            int patternLen = pattern.length();
            int [] prefixString= prefixFunction(pattern);
            int k = 0;
            for (int i = 0; i < textLen; i++) {
                while (k > 0 && pattern.charAt(k) != text.charAt(i)) {
                    k = prefixString[k - 1];
                }
                if (pattern.charAt(k) == text.charAt(i)) {
                    k++;
                }
                if (k == patternLen) {
                    System.out.print((i - patternLen + 1) + " ");
                    k = prefixString[k - 1];
                }
            }
        }


    }

    public static void main(String[] args)  {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                String pattern = br.readLine();
                String text = br.readLine();
                KMP.kmpSearch(text, pattern);
            }catch (Exception e) {
                System.out.println("following problem occurred " + e.getMessage());
            }
    }
}
