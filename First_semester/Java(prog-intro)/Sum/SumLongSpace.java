public class SumLongSpace {
    public static void main(String[] args) {
        long sum = 0L;
        for (int i = 0; i < args.length; i++) {
          //  System.out.println((int) args[i].charAt(0));
            for (int j = 0; j < args[i].length(); j++) {

                if (!Character.isWhitespace(args[i].charAt(j)) && args[i].charAt(j) != 160) {
                    int start = j;
                    while(j < args[i].length() && !Character.isWhitespace(args[i].charAt(j))
                        && args[i].charAt(j) != 160) {
                        j++;
                    }
                    int end = j;
               //     System.out.println("***" + args[i].substring(start, end) + "***");
                    sum += Long.parseLong(args[i].substring(start, end));
                }
            }
        }
        System.out.println(sum);
    }
}
    