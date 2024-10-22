package HW1_TASK4.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    private static int[] findTailsAndPositions(int[] nums, int[] tails, int[] positions) {
        int len = nums.length;
        int maxlen = 0;

        for (int index = 0; index < len; index++) {
            int pos = binarySearch(nums, tails, maxlen, index);
            tails[pos] = index;
            positions[index] = (pos > 0) ? tails[pos - 1] : -1;

            if (pos == maxlen) {
                maxlen++;
            }
        }
        return new int[]{maxlen, tails[maxlen - 1]};
    }

    private static int binarySearch(int[] nums, int[] tails, int maxlen, int index) {
        int left = 0;
        int right = maxlen;

        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[tails[mid]] >= nums[index]) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    private static List<Integer> buildSequence(int[] positions, int startIndex) {
        List<Integer> sequence = new ArrayList<>();
        for (int i = startIndex; i >= 0; i = positions[i]) {
            sequence.add(i + 1);
        }
        Collections.reverse(sequence);
        return sequence;
    }

    public static void longestDecreasingSubsequence(int[] nums) {
        int len = nums.length;
        int[] tails = new int[len];
        int[] positions = new int[len];

        int[] result = findTailsAndPositions(nums, tails, positions);
        int maxlen = result[0];
        int startIndex = result[1];

        List<Integer> sequence = buildSequence(positions, startIndex);

        System.out.println(sequence.size());
        for (int idx : sequence) {
            System.out.print(idx + " ");
        }
    }

    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String numStr = br.readLine();
            int num = Integer.parseInt(numStr);

            String[] inputs = br.readLine().split(" ");
            int[] arr = Arrays.stream(inputs)
                    .mapToInt(Integer::parseInt)
                    .limit(num)
                    .toArray();

            longestDecreasingSubsequence(arr);

        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + e.getMessage());
        }
    }
}