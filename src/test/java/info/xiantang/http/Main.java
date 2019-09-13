package info.xiantang.http;

/**
 * @Author: xiantang
 * @Date: 2019/9/10 19:05
 */
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
//        Scanner in = new Scanner(System.in);
//        int N = in.nextInt();
//        int count = 0;
//        for (int i = 1; i <= N; i++) {
//            int sum = 0;
//            for (int j = i; j <= N; j++) {
//                sum += j;
//                if (sum == N) {
//                    count += 1;
//                    break;
//                }
//            }
//
//        }
//        System.out.println(count);
        Scanner in = new Scanner(System.in);
        String str1 = in.nextLine();
        String str2 = in.nextLine();

        int[][] dp = new int[str1.length()][str2.length()];
        for (int i = 0; i < str1.length(); i++) {
            dp[i][0] = i;
        }
        for (int i = 0; i < str2.length(); i++) {
            dp[0][i] = i;
        }
        for (int i = 1; i < str1.length(); i++) {
            for (int j = 1; j < str2.length(); j++) {
                if (str1.charAt(i) == str2.charAt(j)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1],
                            Math.min(dp[i][j - 1], dp[i - 1][j])) + 1;
                }
            }
        }
        System.out.println(dp[str1.length() - 1][str2.length() - 1]);



    }
}



