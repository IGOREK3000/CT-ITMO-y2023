import java.util.Arrays;

public class ReverseMinCAbc {


    public static String converIntToAbc(int number) {
        String s = "abcdefghij";
        String intNum = "";
        StringBuilder intAbc = new StringBuilder();
        if (number < 0) {
            intAbc.append("-");
            intNum = Integer.toString(-number);
        } else {
            intNum = Integer.toString(number);
        }
        for (int i = 0; i < intNum.length(); i++) {
            char Abc = s.charAt(Integer.parseInt(String.valueOf(intNum.charAt(i))));
            intAbc.append(Abc);
        }
        return intAbc.toString();
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[][] numbers = new int[10][];
        int[] arr_sizes = new int[10];

        int lines_count = 0;
        while(sc.hasNextLine()) {
            Scanner line = new Scanner(sc.nextLine());
            lines_count++;
            if (numbers.length < lines_count) {
                numbers = Arrays.copyOf(numbers, numbers.length * 2);
                arr_sizes = Arrays.copyOf(arr_sizes, arr_sizes.length * 2);
            }
            int nums_count = 0;
            numbers[lines_count - 1] = new int[10];

            while(line.hasNextIntAbc()) {
                nums_count++;
                if (numbers[lines_count - 1].length < nums_count) {
                    numbers[lines_count - 1] = Arrays.copyOf(numbers[lines_count - 1], numbers[lines_count - 1].length * 2);
                }
                numbers[lines_count - 1][nums_count - 1] = line.nextIntAbc();
            }
            arr_sizes[lines_count - 1] = nums_count;
        }

        int len_max = 0;
        for (int i = 0; i < arr_sizes.length; i++) {
            len_max = Math.max(len_max, arr_sizes[i]);
        }
        int[] mins = new int[len_max];
        Arrays.fill(mins, Integer.MAX_VALUE);
        for (int i = 0; i < arr_sizes[0]; i++) {
            System.out.print(converIntToAbc(numbers[0][i]) + " ");
            mins[i] = numbers[0][i];
        }
        System.out.println();
        for (int i = 1; i < lines_count; i++) {
            for (int j = 0; j < arr_sizes[i]; j++) {
                mins[j] = Math.min(numbers[i][j], mins[j]);
                int m = Math.min(numbers[i][j], mins[j]);
                System.out.print(converIntToAbc(m) + " ");
            }
            System.out.println();
        }

    }
}