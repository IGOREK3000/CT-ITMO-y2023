package expression.generic;

import expression.exceptions.DBZException;
import expression.exceptions.OverflowException;

import java.math.BigInteger;

public class DivideExecutor extends BinaryExecutor {

    @Override
    protected Integer intExecute(Integer num1, Integer num2) {
        if (num2 == 0) {
            throw new DBZException("Exception for division by zero");
        }
        if (num1 == Integer.MIN_VALUE && num2 == -1) {
            throw new OverflowException("divide");
        }

        return num1 / num2;
    }

    @Override
    protected Double doubleExecute(Double num1, Double num2) {
        return num1 / num2;
    }

    @Override
    protected BigInteger bigIntegerExecute(BigInteger num1, BigInteger num2) {
        return num1.divide(num2);
    }
}
