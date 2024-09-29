import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    public static void LongestDecreasingSubsequence(int[] nums) {
        int len = nums.length;
        int[] tails = new int[len];
        int[] positions = new int[len];
        int maxlen = 0;
        for (int index = 0; index< len; index++) {
            int left = 0, right = maxlen;
            while (left < right) {
                int mid = (left + right) / 2;
                if (nums[tails[mid]] >= nums[index]) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            tails[left] = index;
            positions[index] = (left > 0) ? tails[left- 1] : -1;

            if (left == maxlen) {
                maxlen++;
            }
        }

        List<Integer> sequence = new ArrayList<>();
        for (int i = tails[maxlen - 1]; i >= 0; i = positions[i]) {
            sequence.add(i + 1);
        }

        System.out.println(sequence.size());
        Collections.reverse(sequence);
        for (int idx : sequence) {
            System.out.print(idx + " ");
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int num = Integer.parseInt(br.readLine());
        StringTokenizer tokenizer = new StringTokenizer(br.readLine());
        int[] arr = new int[num];

        for (int index = 0; index < num; index++) {
            arr[index] = Integer.parseInt(tokenizer.nextToken());
        }
        br.close();
        LongestDecreasingSubsequence(arr);
    }
}
