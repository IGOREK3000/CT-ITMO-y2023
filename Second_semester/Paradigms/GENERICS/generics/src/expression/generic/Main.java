package expression.generic;

import expression.exceptions.EvaluateException;
import expression.exceptions.ParsingException;

import java.io.StringBufferInputStream;
import java.util.HashMap;
import java.util.Map;

public class Main {
//    private Map<String, String> modeChooser = new HashMap<>(Map.of(
//            "-i", "i",
//            "-d", "d",
//            "-bi", "bi"
//    ));
    public static void main(String[] args) {
        String mode = args[0];


        StringBuilder expressionSB = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            expressionSB.append(args[i]);
        }
        String expression = expressionSB.toString();


        GenericTabulator tabulator = new GenericTabulator();
        Object[][][] table = new Object[0][][];
        try {
            table = tabulator.tabulate(mode.substring(1), expression, -2, 2, -2, 2, -2, 2);
        } catch (ParsingException e) {
            System.out.println("You lose :( " + e.getMessage());
        }
        for (int i = 0; i <= 4; i++) {
            for (int j = 0; j <= 4; j++) {
                for (int k = 0; k <= 4; k++) {
                    System.out.print(table[i][j][k] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}