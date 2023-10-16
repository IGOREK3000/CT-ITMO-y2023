import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] elements = new int[n];
        int[] depths = new int[n];
        int[][] inputs = new int[n][];
        int[][] values = new int[n][];
        StringBuilder answers = new StringBuilder();
        int argsCount = 0;
        for (int i = 0; i < n; i++) {
            int argsQuantity = sc.nextInt();
            if (argsQuantity == 0) {
                depths[i] = 0;
                inputs[i] = new int[0];
                values[i] = new int[0];
                argsCount++;
            //    answers[i] = -1;
            } else {
                inputs[i] = new int[argsQuantity];
                for (int j = 0; j < argsQuantity; j++) {
                    inputs[i][j] = sc.nextInt() - 1;
                }
                values[i] = new int[(int) Math.pow(2, argsQuantity)];
                for (int j = 0; j < values[i].length; j++) {
                    values[i][j] = sc.nextInt();
                }
                int maxInputDepth = depths[inputs[i][0]];
                for (int j = 1; j < inputs[i].length; j++) {
                    if (depths[inputs[i][j]] > maxInputDepth) {
                        maxInputDepth = depths[inputs[i][j]];
                    }
                }
                depths[i] = maxInputDepth + 1;
            }
        }
        System.out.println(depths[n - 1]);
        StringBuilder ansTemp = new StringBuilder();

        for (int i = 0; i < (int) Math.pow(2, argsCount); i++) {
            String tempArg = Integer.toBinaryString(i);
            char[] zeros = new char[argsCount - tempArg.length()];
            Arrays.fill(zeros, '0');
            String arg = (new String(zeros)) + tempArg;
            int argNum = 0;
            for (int k = 0; k < n; k++) {
                if (inputs[k].length == 0) {
                    answers.append(arg.charAt(argNum));;
                    argNum++;
                } else {
                    for (int j = 0; j < inputs[k].length; j++) {
                        ansTemp.append(answers.charAt(inputs[k][j]));
                    }
                    answers.append(values[k][Integer.parseInt(ansTemp.toString(), 2)]);
                    ansTemp.setLength(0);
                }
            }
            System.out.print(answers.charAt(n - 1));
            answers.setLength(0);
        }
    }
}