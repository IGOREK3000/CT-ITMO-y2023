import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int numVariables = scanner.nextInt();
        int numDisjunctions = scanner.nextInt();

        int[][] formula = new int[numDisjunctions][numVariables];
        for (int i = 0; i < numDisjunctions; ++i) {
            for (int j = 0; j < numVariables; ++j) {
                formula[i][j] = scanner.nextInt();
            }
        }

        boolean[] isVisited = new boolean[numDisjunctions];
        boolean continueIteration = true;

        while (continueIteration) {
            continueIteration = false;

            for (int i = 0; i < numDisjunctions; ++i) {
                if (isVisited[i]) continue;

                int uniqueVariableIndex = 0;
                int variableCount = 0;

                for (int j = 0; j < numVariables; ++j) {
                    if (formula[i][j] != -1) {
                        ++variableCount;
                        uniqueVariableIndex = j;
                    }
                    if (variableCount > 1) break;
                }

                if (variableCount != 1) continue;
                continueIteration = true;
                isVisited[i] = true;

                for (int j = 0; j < numDisjunctions; ++j) {
                    if (i == j || isVisited[j] || formula[j][uniqueVariableIndex] == -1) continue;

                    int nonExistentCount = 0;
                    int negativeCount = 0;

                    for (int m = 0; m < numVariables; ++m) {
                        if (m == uniqueVariableIndex) {
                            if (formula[j][m] == formula[i][m]) {
                                isVisited[j] = true;
                                break;
                            }
                            negativeCount++;
                        } else if (formula[j][m] == -1) {
                            nonExistentCount++;
                        }
                    }

                    formula[j][uniqueVariableIndex] = -1;

                    if (negativeCount + nonExistentCount == numVariables) {
                        System.out.println("YES");
                        return;
                    }
                }
            }
        }

        System.out.println("NO");
    }
}