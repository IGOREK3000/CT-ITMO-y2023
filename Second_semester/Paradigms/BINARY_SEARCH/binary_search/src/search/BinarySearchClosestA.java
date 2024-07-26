package search;

public class BinarySearchClosestA {
    // Pre: (args.length == 1 && x == args[0] && a[0] == args[0]) || (args.length > 1 && x == args[0] && a[0...n] ==
    // == args[1...n+1] && args[i] - int)
    // Post: res displays on System.out: (x <= a[0] && res = min(int k: a[k] <= x)) || (res == a.length)
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int[] a = new int[args.length - 1];
        int sum = 0;


        for (int i = 0; i < args.length - 1; i++) {
            a[i] = Integer.parseInt(args[i + 1]);
            sum += a[i];
        }


        if (sum % 2 == 0) {
            // Pre: a != null && int l = 0 && int r = a.length - 1 && int res = r;
            // Post: r = {k from 0...n : |a[k] - x| - min{1..n}} || r == a.length
            System.out.println(recursiveBS(a, x, 0, a.length - 1, a.length - 1));
        } else {
            // Pre: a != null
            // Post: r = {k from 0...n : |a[k] - x| - min{1..n}} || r == a.length
            System.out.println(cyclicalBS(a, x));
        }
    }


    // Pred: a.length >= 1
    // Post: Post: r = {k from 0...n : |a[k] - x| - min{1..n}} || r == a.length
    public static int cyclicalBS(int[] a, int x) {
        // ( ... || r == a.length)
        if (a.length == 0) {
            return 0;
        }
        int l = 0;
        int r = a.length - 1;
        int res = r + 1;
        // Самый левый который >= x
        // k < l -> a[k] > x     k > r -> a[k] <= x
        // Pred: Inv: res == r + 1 && a[res] >= x && r - l >= -1
        while (l != r + 1) {
            // res == r + 1 && a[res] >= x && r - l >= -1
            // res == r + 1 && a[res] >= x &&  r - l >= -1 && r >= (l + r) / 2 >= l
            int m = (l + r) / 2;
            // res == r + 1 && a[res] >= x &&  r - l >= -1 && r >= m >= l
            if (a[m] >= x) {
                // a[m] >= x && m >= l
                res = m;
                // a[res] >= x && res - 1 == m - 1 && m >= l
                // a[res] >= x && res - 1 == m - 1 && m - 1 - l >= -1
                r = m - 1;
                // res == r + 1 && a[res] >= x && r - l >= -1
            } else {
                // a[m] > x && res == r + 1 && a[res] >= x && r - l >= -1 && m + 1 <= r + 1
                l = m + 1;
                // a[m] > x && res == r + 1 && a[res] >= x && r + 1 >= l
                // res == r + 1 && a[res] >= x && r - l >= -1
            }
            // res == r + 1 && a[res] >= x && r - l >= -1
        }
        // Post: Inv: res == r + 1 && a[res] >= x && r - l >= -1 && l == r + 1
        // (res == r + 1 && a[res] >= x) && (l == r + 1)
        // (res == r + 1 && a[res] >= x) && l == res (Inv: что a[l] < x)
        // res = min(int k: a[k] >= x)

        // l > r: i -> i + 1:  r - l ↓, а r - l >= -1


        // Pre: res = min(int k: a[k] >= x)
        // Post: a[res]: a[res] - x >= x - a[res - 1] && res = min(int k: a[k] >= x)
        if (res == a.length || (res > 0 && a[res] - x >= x - a[res - 1]
                && !checkOverflow(x, a[res - 1]) && !checkOverflow(a[res], x))) {
            // res == a.length -> a[a.length - 1]: res = min(int k: a[k] >= x)
            // ....a[res-1].....x.....a[res]..... :
            // (1) res > 0 && a[res] - x >= x - a[res - 1]
            // (2) !checkOverflow(x, a[res - 1]) && !checkOverflow(a[res], x)
            // (1) && (2) -> a[res]: a[res] - x >= x - a[res - 1]
            res--;
        }
        return a[res];
    }

    // Pre: int x, int y
    // Post: (x - y) overflowed ?
    public static boolean checkOverflow(int x, int y) {
        // Pre: true
        // Post: (x - y) overflowed
        if ((y > 0 && x < Integer.MIN_VALUE + y)
                || (y < 0 && x > Integer.MAX_VALUE + y)
                || (y == Integer.MIN_VALUE && x >= 0)) {
            // y == Integer.MIN_VALUE && x >= 0 -> (x - y) overflowed (negative)
            // y > 0 && x < Integer.MIN_VALUE + y -> (x - y) overflowed (negative)
            // y < 0 && x > Integer.MAX_VALUE + y -> (x - y) overflowed (positive)

            // Integer.MIN_VALUE > x - y || x - y > Integer.MAX_VALUE
            return true;
        }
        // Integer.MIN_VALUE <= x - y <= Integer.MAX_VALUE
        return false;
    }


    // Pre: a.length >= 1, l == 0, r == a.length - 1, res == a.length
    // Inv: res == r + 1 && a[res] <= x && r - l >= -1 && a[l] <= x <= a[r]
    // Post: (x <= a[0] && res = min(int k: a[k] <= x)) || res == a.length

    public static int recursiveBS(int[] a, int x, int l, int r, int res) {
        // res == r + 1 && a[res] >= x && r - l >= -1
        if (l > r) {
            // res == r + 1 && a[res] >= x && r + 1 >= l && l > r
            // res == r + 1 && a[res] >= x && r + 1 >= l > r
            // res == r + 1 == l && a[res] >= x
            // (res == a.length || a[res] >= x) && (res == 0 || a[res - 1] > x)
            // res = min(int k: a[k] >= x)

            // Pre: res = min(int k: a[k] >= x)
            // Post: a[res]: a[res] - x >= x - a[res - 1] && res = min(int k: a[k] >= x)
            if (res > 0 && a[res] - x >= x - a[res - 1] && !checkOverflow(x, a[res - 1]) ) {
                // res == a.length -> a[a.length - 1]: res = min(int k: a[k] >= x)
                // ....a[res-1].....x.....a[res]..... :
                // (1) res > 0 && a[res] - x >= x - a[res - 1]
                // (2) !checkOverflow(x, a[res - 1]) && !checkOverflow(a[res], x)
                // (1) && (2) -> a[res]: a[res] - x >= x - a[res - 1]
                res--;
            }
            return a[res];
        } else {
            // res == r + 1 && a[res] >= x && r + 1 >= l && l <= r
            int m = (l + r) / 2;
            if (a[m] >= x) {
                // Pre:
                // a[m] >= x && m >= l
                // res = m;
                // a[res] >= x && res - 1 == m - 1 && m >= l
                // a[res] >= x && res - 1 == m - 1 && m - 1 - l >= -1
                // res == r + 1 && a[res] >= x && r - l >= -1
                // Post: -||-
                return recursiveBS(a, x, l, m - 1, m);
            } else {
                //Pre:
                // a[m] > x && res == r + 1 && a[res] >= x && r - l >= -1 && m + 1 <= r + 1
                // a[m] > x && res == r + 1 && a[res] >= x && r + 1 >= l
                // res == r + 1 && a[res] >= x && r - l >= -1
                // Post: -||-
                return recursiveBS(a, x, m + 1, r, res);
            }
        }
        // l > r: i -> i + 1:  r - l ↓, а r - l >= -1
    }
}
