import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[][] firstRelation = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                firstRelation[i][j] = sc.nextInt();
            }
        }
        int[][] secondRelation = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                secondRelation[i][j] = sc.nextInt();
            }
        }


        int isReflexive1 = 1;
        int isAntiReflexive1 = 1;
        int isSymmetric1 = 1;
        int isAntiSymmetric1 = 1;
        int isTransitive1 = 1;

        int isReflexive2 = 1;
        int isAntiReflexive2 = 1;
        int isSymmetric2 = 1;
        int isAntiSymmetric2 = 1;
        int isTransitive2 = 1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (firstRelation[i][j] != firstRelation[j][i]) {
                    isSymmetric1 = 0;
                }
                if (firstRelation[i][j] == 1 && firstRelation[j][i] == 1 && i != j) {
                    isAntiSymmetric1 = 0;
                }
                for (int k = 0; k < n; k++) {
                    if (firstRelation[i][j] == 1 && firstRelation[j][k] == 1 && firstRelation[i][k] != 1) {
                        isTransitive1 = 0;
                    }
                }
            }
            if (firstRelation[i][i] != 1) {
                isReflexive1 = 0;
            }
            if (firstRelation[i][i] != 0) {
                isAntiReflexive1 = 0;
            }
        }




        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (secondRelation[i][j] != secondRelation[j][i]) {
                    isSymmetric2 = 0;
                }
                if (secondRelation[i][j] == 1 && secondRelation[j][i] == 1 && i != j) {
                    isAntiSymmetric2 = 0;
                }
                for (int k = 0; k < n; k++) {
                    if (secondRelation[i][j] == 1 && secondRelation[j][k] == 1 && secondRelation[i][k] != 1) {
                        isTransitive2 = 0;
                    }
                }
            }
            if (secondRelation[i][i] != 1) {
                isReflexive2 = 0;
            }
            if (secondRelation[i][i] != 0) {
                isAntiReflexive2 = 0;
            }
        }

        System.out.println(isReflexive1 + " " + isAntiReflexive1 + " " + isSymmetric1 +
                " " + isAntiSymmetric1 + " " + isTransitive1);
        System.out.println(isReflexive2 + " " + isAntiReflexive2 + " " + isSymmetric2 +
                " " + isAntiSymmetric2 + " " + isTransitive2);

        int[][] comp = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    comp[i][j] += firstRelation[i][k] * secondRelation[k][j];
                }
                if (comp[i][j] > 0) {
                    comp[i][j] = 1;
                }
            }
        }


        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(comp[i][j] + " ");
            }
            System.out.println();
        }
    }
}