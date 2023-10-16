import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String[] in = new String[1 << n];
        int[] table = new int[1 << n];
        int onesCount = 0;
        for (int i = 0; i < 1 << n; i++) {
            sc.next();
            table[i] = sc.nextInt();
            if (table[i] == 1) {
                onesCount++;
            }
        }
        if (n > 1) {
            int elementCount = 2*n + 1;
            if (onesCount >= 1) {
                String[] clos = new String[onesCount];
                int[] closElementNum = new int[onesCount];
                int oneNum = 0;
                for (int i = 0; i < 1 << n; i++) {
                    if (table[i] == 1) {
                        String tempArg = Integer.toBinaryString(i);
                        char[] zeros = new char[n - tempArg.length()];
                        Arrays.fill(zeros, '0');
                        String arg = (new String(zeros)) + tempArg;
                        StringBuilder tempClos = new StringBuilder();
                        // 1 элемент
                        if (arg.charAt(0) == '0') {
                            tempClos.append("2 " + (n+1) + " ");
                        } else {
                            tempClos.append("2 " + 1 + " ");
                        }
                        // 2 элемент
                        if (arg.charAt(1) == '0') {
                            tempClos.append((n+2) + "\n");
                        } else {
                            tempClos.append(2 + "\n");
                        }
                        elementCount++;
                        for (int j = 2; j < n; j++) {
                            if (arg.charAt(j) == '0') {
                                tempClos.append("2 " + (n + j + 1) + " " + (elementCount - 1) + "\n");
                            } else {
                                tempClos.append("2 " + (j + 1) + " " + (elementCount - 1) + "\n");
                            }
                            elementCount++;
                        }
                        clos[oneNum] = tempClos.substring(0, tempClos.length() - 1).toString();
                        closElementNum[oneNum] = elementCount - 1;
                        oneNum++;

                    }
                }
                StringBuilder form = new StringBuilder();
                for (int i = 0; i < clos.length; i++) {
                    form.append(clos[i] + "\n");
                }
                if (clos.length >= 2) {
                    form.append("3 " + closElementNum[0] + " " + closElementNum[1] + "\n");
                    elementCount++;
                    for (int i = 2; i < clos.length; i++) {
                        form.append("3 " + (elementCount - 1) + " " + closElementNum[i] + "\n");
                        elementCount++;
                    }
                }
                System.out.println(elementCount - 1);


                for (int i = 0; i < n; i++) {
                    System.out.println("1 " + (i+1));
                }
                System.out.println(form.toString());
            }  else {
                System.out.println(n + 2);
                System.out.println("1 1");
                System.out.println("2 " + (n + 1) + " 1");
            }

        } else {
            if (table[0] == 0 & table[1] == 0) {
                System.out.println("3");
                System.out.println("1 1");
                System.out.println("2 1 2");
            } else if (table[0] == 0 & table[1] == 0) {
                System.out.println("3");
                System.out.println("1 1");
                System.out.println("3 1 2");
            } else if (table[0] == 1 & table[1] == 0) {
                System.out.println("3");
                System.out.println("1 1");
            } else if (table[0] == 0 & table[1] == 1) {
                System.out.println("3");
                System.out.println("1 1");
                System.out.println("1 2");
            }
        }


    }
}