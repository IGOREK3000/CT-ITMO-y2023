import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = 1 << n;
        int[] truthTable = new int[m];
        for (int i = 0; i < m; i++) {
            scanner.next();
            truthTable[i] = scanner.nextInt();
        }
        int[] coeffs = new int[m];
        for (int i = 0; i < m; i++) {
            coeffs[i] = truthTable[i];
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if ((j & (1 << i)) != 0) {
                    coeffs[j] ^= coeffs[j ^ (1 << i)];
                }
            }
        }
        for (int i = 0; i < m; i++) {
            String s = Integer.toBinaryString(i);
            int k = n - s.length();
            char[] str = new char[k];
            Arrays.fill(str, '0');
            System.out.print(new String(str) + Integer.toBinaryString(i));
            System.out.print(" ");
            System.out.println(coeffs[i]);
        }
    }
}