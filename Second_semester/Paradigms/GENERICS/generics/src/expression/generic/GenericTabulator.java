package expression.generic;

import expression.exceptions.EvaluateException;
import expression.exceptions.ParsingException;
import expression.executors.*;
import expression.executors.IntegerExecutor;

import java.util.Map;

public class GenericTabulator implements Tabulator {
    private static final Map <String, Executor<?>> executorChooser = Map.of(
            "i", new IntegerExecutor(true),
            "d", new DoubleExecutor(),
            "bi", new BigIntegerExecutor(),
             "u", new IntegerExecutor(false),
            "b", new ByteExecutor(),
            "bool", new BooleanExecutor()
    );

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws ParsingException {
        Executor<?> executor = executorChooser.get(mode);
        return eval(expression, x1, x2, y1, y2, z1, z2, executor);
    }

    public <T> Object[][][] eval(String expression, int x1, int x2, int y1, int y2, int z1, int z2, Executor<T> executor) throws ParsingException {
        ExpressionParser<T> expressionParser = new ExpressionParser<>();
        Object[][][] table = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        MainExpression<T> mainExpression = expressionParser.parse(expression, executor);
        assert mainExpression != null;
        for (int i = 0; i <= x2 - x1; i++) {
            for (int j = 0; j <= y2 - y1; j++) {
                for (int k = 0; k <= z2 - z1; k++) {
                    try {
                        table[i][j][k] = mainExpression.evaluate(executor.executeNumber(Integer.toString(x1 + i)),
                                executor.executeNumber(Integer.toString(y1 + j)),
                                executor.executeNumber(Integer.toString(z1 + k)));
                    } catch (EvaluateException e) {
                        table[i][j][k] = null;
                    }
                }
            }
        }
        return table;
    }
}