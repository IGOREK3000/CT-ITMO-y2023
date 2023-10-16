import java.util.Scanner;

public class Main {
    public static boolean majorizes(int a, int b) {
        boolean k = true;
        String as = Integer.toBinaryString(a);
        String bs = Integer.toBinaryString(b);
        int c = a | b;
        String cs = Integer.toBinaryString(c);
        for (int i = 0; i < as.length(); i++) {
            if (as.charAt(i) == '0' && cs.charAt(i) == '1') {
                k = false;
                break;
            }
        }
        return k;
    }

    public static boolean isLinear(int[] truthTable) {
        int m = truthTable.length;
        int n = (int) (Math.log(m)/Math.log(2));
        boolean k = true;
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
            if (coeffs[i] == 1) {
                int count = 0;
                String is = Integer.toBinaryString(i);
                for (int j = 0; j < is.length(); j++) {
                    if (is.charAt(j) == '1') {
                        count++;
                    }
                    if (count >= 2) {
                        break;
                    }
                }
                if (count >= 2) {
                    k = false;
                    break;
                }
            }
        }
        return k;
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt(); // количество функций
        int[][] functions = new int[n][]; // массив для хранения значений функций
        for (int i = 0; i < n; i++) {
            int si = scanner.nextInt(); // количество аргументов очередной функции
            String ai = scanner.next(); // строка, описывающая таблицу истинности
            functions[i] = new int[(int) Math.pow(2, si)];
            for (int j = 0; j < functions[i].length; j++) {
                functions[i][j] = ai.charAt(j) - '0'; // заполняем массив значениями функции
            }
            // здесь можно выполнить необходимые действия с полученными данными
        }

        boolean savesZero = true;
        boolean savesOne = true;
        boolean isSelfDual = true;
        boolean isMonotone = true;
        boolean isLinear = true;

        for (int i = 0; i < n; i++) {
            // сохраняет ноль
            if (functions[i][0] != 0) {
                savesZero = false;
            }
            // сохраняет единицу
            if (functions[i][functions[i].length - 1] != 1) {
                savesOne = false;
            }
          //  System.out.println(functions[i][functions[i].length - 1]);

            // самодвойственность
     //       System.out.println(functions[i].length);
            if (functions[i].length > 1) {
                for (int j = 0; j <= functions[i].length / 2 - 1; j++) {
                    if (functions[i][j] == functions[i][functions[i].length - j - 1]) {
                        isSelfDual = false;

                    }
                }
            } else {
                isSelfDual = false;
            }


            // монотонность
            if (functions[i].length > 1) {
                for (int j = 0; j < functions[i].length; j++) {
                    if (functions[i][j] == 0) {
                        for (int k = 0; k < j; k++) {
                            if (functions[i][k] == 1 && majorizes(j, k)) {
                                isMonotone = false;
                                break;
                            }
                        }
                    }
                }
            }

        //    System.out.println(functions[i].length);

            //линейность
            if (functions[i].length > 1) {
                if(!isLinear(functions[i])) {
                    isLinear = false;
                }
            }

//            System.out.println(savesZero);
//            System.out.println(savesOne);
//            System.out.println(isSelfDual);
//            System.out.println(isMonotone);
//            System.out.println(isLinear);
        }
        boolean result = false;
//        System.out.println(savesZero);
//        System.out.println(savesOne);
//        System.out.println(isSelfDual);
//        System.out.println(isMonotone);
//        System.out.println(isLinear);
        if (!savesZero && !savesOne && !isLinear && !isMonotone && !isSelfDual) {
            result = true;
        }
        if (result) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }
    }
}