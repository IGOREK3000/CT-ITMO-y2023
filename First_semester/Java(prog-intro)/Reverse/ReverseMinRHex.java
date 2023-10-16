import java.util.Scanner;
import java.util.Arrays;

public class ReverseMinRHex {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[][] numbers = new int[1][];
        int[] arr_sizes = new int[1];

        int lines_count = 0;
        while(sc.hasNextLine()) {
            Scanner line = new Scanner(sc.nextLine());
            lines_count++;
            if (numbers.length < lines_count) {
                numbers = Arrays.copyOf(numbers, numbers.length * 2);
                arr_sizes = Arrays.copyOf(arr_sizes, arr_sizes.length * 2);
            }
            int nums_count = 0;
            numbers[lines_count - 1] = new int[1];
         //   int[] nums = numbers[lines_count - 1]; 

            while(line.hasNext()) {
                nums_count++;
                if (numbers[lines_count - 1].length < nums_count) {
                    numbers[lines_count - 1] = Arrays.copyOf(numbers[lines_count - 1], numbers[lines_count - 1].length * 2);
                }
                String num = line.next();
                if (num.startsWith("0x")) {
                    numbers[lines_count - 1][nums_count - 1] = Integer.parseInt(num.substring(2), 16);
                } else {
                    numbers[lines_count - 1][nums_count - 1] = Integer.parseInt(num);
                }
                
            }

            arr_sizes[lines_count - 1] = nums_count;
        }

        for (int i = 0; i < lines_count; i++) {
            int minn = numbers[i][0];
            for (int j = 0; j < arr_sizes[i] - 1; j++) {
                minn = Math.min(minn, numbers[i][j]);
                System.out.print(minn + " ");
            }
            if (arr_sizes[i] > 0) {
                minn = Math.min(minn, numbers[i][arr_sizes[i] - 1]);
                System.out.print(minn + " ");
            }
            System.out.println();
        }

   /*     for (int i = lines_count -1 ; i >= 0; i--) {
            for (int j = arr_sizes[i] - 1; j >= 0; j--) {
                System.out.print(numbers[i][j] + " ");
            }  
            System.out.println(); 
             
        }  */

   /*     int[][] reversedNums = new int[lines_count][];
        for (int i = lines_count - 1; i >= 0; i--) {
            reversedNums[i] = new int[arr_sizes[lines_count - i - 1]]; 
            for (int j = arr_sizes[lines_count - i - 1] - 1; j >= 0; j--) {
                reversedNums[i][j] = numbers[lines_count - i - 1][arr_sizes[lines_count - i - 1] - j - 1];
            }
        } 
        for (int i = 0; i < reversedNums.length; i++) {
            for(int j = 0; j < reversedNums[i].length; j++) {
                System.out.print(reversedNums[i][j] + " ");
            }
            System.out.println();

        } */

    }
}      