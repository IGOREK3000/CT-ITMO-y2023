public class Sum {
	public static void main(String[] args) {
		int sum = 0;
		for (int i = 0; i < args.length; i++) {
			for (int j = 0; j < args[i].length(); j++) {
				if (!Character.isWhitespace(args[i].charAt(j))) {
					int start = j;
					while(j < args[i].length() && !Character.isWhitespace(args[i].charAt(j))) {
						j++;
					}
					int end = j;
					sum += Integer.parseInt(args[i].substring(start, end));
				}
			}
		}
		System.out.println(sum);
	}
} 

