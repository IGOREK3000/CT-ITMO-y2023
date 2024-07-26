package search;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class BinarySearch {
    // Pred: (args.length == 1 && x == args[0] && a[0] == args[0]) || (args.length > 1 && x == args[0] && a[0...n] ==
    // == args[1...n+1] && args[i] - int)
    // Post: res displays on System.out: (x <= a[0] && res = min(int k: a[k] <= x)) || (res == a.length)
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int[] a = new int[args.length - 1];
        // Pred: -||-
        // Post: a.length > 1
        for (int i = 0; i < args.length - 1; i++) {
            a[i] = Integer.parseInt(args[i + 1]);
        }
        if (args.length == 1) {
            System.out.println('0');
        } else {
            System.out.println(recursiveBS(a, x, 0, a.length - 1, a.length));
        }

     //   System.out.println(cyclicalBS(a, x));
    }


    // Pred: a.length >= 1
    // Post: (x <= a[0] && res = min(int k: a[k] <= x)) || res == a.length
    public static int cyclicalBS(int[] a, int x) {
        int l = 0;
        int r = a.length - 1;
        int res = r + 1;
        // k < l -> a[k] > x     k > r -> a[k] <= x
        // Pred: Inv: res == r + 1 && a[res] <= x && r - l >= -1
        while (l != r + 1) {
            // res == r + 1 && a[res] <= x && r - l >= -1
            // res == r + 1 && a[res] <= x &&  r - l >= -1 && r >= (l + r) / 2 >= l
            int m = (l + r) / 2;
            // res == r + 1 && a[res] <= x &&  r - l >= -1 && r >= m >= l
            if (a[m] <= x) {
                // a[m] <= x && m >= l
                res = m;
                // a[res] <= x && res - 1 == m - 1 && m >= l
                // a[res] <= x && res - 1 == m - 1 && m - 1 - l >= -1
                r = m - 1;
                // res == r + 1 && a[res] <= x && r - l >= -1
            } else {
                // a[m] > x && res == r + 1 && a[res] <= x && r - l >= -1 && m + 1 <= r + 1
                l = m + 1;
                // a[m] > x && res == r + 1 && a[res] <= x && r + 1 >= l
                // res == r + 1 && a[res] <= x && r - l >= -1
            }
            // res == r + 1 && a[res] <= x && r - l >= -1
        }
        // Post: Inv: res == r + 1 && a[res] <= x && r - l >= -1 && l == r + 1
        // (res == r + 1 && a[res] <= x) && (l == r + 1)
        // (res == r + 1 && a[res] <= x) && l == res
        // (res == a.length || a[res] <= x) && (res == 0 || a[res - 1] > x)
        // res = min(int k: a[k] <= x)

        // выход из цикла: каждую итерацию r - l уменьшается, а l - r ограничено снизу
        return res;
    }

    // Pred: a.length >= 1, l == 0, r == a.length - 1, res == a.length
    // Post: (x <= a[0] && res = min(int k: a[k] <= x)) || res == a.length
    // Inv: res == r + 1 && a[res] <= x && r - l >= -1
    public static int recursiveBS(int[] a, int x, int l, int r, int res) {
        // res == r + 1 && a[res] <= x && r - l >= -1
        if (l > r) {
            // res == r + 1 && a[res] <= x && r + 1 >= l && l > r
            // res == r + 1 && a[res] <= x && r + 1 >= l > r
            // res == r + 1 == l && a[res] <= x
            // (res == a.length || a[res] <= x) && (res == 0 || a[res - 1] > x)
            // res = min(int k: a[k] <= x)
            return res;
        } else {
            // res == r + 1 && a[res] <= x && r + 1 >= l && l <= r
            int m = (l + r) / 2;

            if (a[m] <= x) {
                // Pred:
                // a[m] <= x && m >= l
                // res = m;
                // a[res] <= x && res - 1 == m - 1 && m >= l
                // a[res] <= x && res - 1 == m - 1 && m - 1 - l >= -1
                // res == r + 1 && a[res] <= x && r - l >= -1
                // Post: -||-
                return recursiveBS(a, x, l, m - 1, m);


            } else {
                //Pred:
                // a[m] > x && res == r + 1 && a[res] <= x && r - l >= -1 && m + 1 <= r + 1
                // a[m] > x && res == r + 1 && a[res] <= x && r + 1 >= l
                // res == r + 1 && a[res] <= x && r - l >= -1
                // Post: -||-
                return recursiveBS(a, x, m + 1, r, res);
            }
        }
    }
}
