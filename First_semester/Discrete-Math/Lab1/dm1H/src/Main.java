import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        generateExpression(n - 1);
    }

    public static void generateExpression(int n) {
        if (n == 0) {
            System.out.print("((A0|B0)|(A0|B0))");
        } else {
            System.out.print("((");
            generateExpression(n - 1);
            String str = Integer.toString(n);
            System.out.print("|((A" + str + "|A" + str + ")|(B" + str + "|B" + str + ")))|(A" + str + "|B" + str + "))");
        }
    }
}