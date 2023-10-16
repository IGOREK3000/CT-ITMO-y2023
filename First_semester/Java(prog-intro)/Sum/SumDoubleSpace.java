public class SumDoubleSpace {
    public static void main(String[] args) {
        double sum = 0;
        // for (String str: args)
        for (int i = 0; i < args.length; i++) {
            for (int j = 0; j < args[i].length(); j++) {

                if (Character.getType(args[i].charAt(j)) != Character.SPACE_SEPARATOR) {
                    int start = j;
                    while(j < args[i].length() && Character.getType(args[i].charAt(j)) != Character.SPACE_SEPARATOR) {
                        j++;
                    }
                    int end = j;
                    sum += Double.parseDouble(args[i].substring(start, end));
                }
            }
        }
        System.out.println(sum);
    }
}
