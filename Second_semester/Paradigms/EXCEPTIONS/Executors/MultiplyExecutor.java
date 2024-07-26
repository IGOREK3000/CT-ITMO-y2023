package expression.generic;

import expression.exceptions.OverflowException;

import java.math.BigInteger;

public class MultiplyExecutor extends BinaryExecutor {

    @Override
    protected Integer intExecute(Integer num1, Integer num2) {
        if (num1 == Integer.MIN_VALUE || num2 == Integer.MIN_VALUE) {
            if ((num2 != 0 && num2 != 1) && (num1 != 0 && num1 != 1)) {
                throw new OverflowException("multiply");
            }
        } else if (num1 != 0 && num2 != 0) {
            if (((num1 > 0 && num2 < 0) && (num2 < Integer.MIN_VALUE / num1)) ||
                    ((num1 < 0 && num2 > 0) && (num1 < Integer.MIN_VALUE / num2)) ||
                    ((num1 > 0 && num2 > 0) && (num1 > Integer.MAX_VALUE / num2)) ||
                    ((num1 < 0 && num2 < 0) && (num1 < Integer.MAX_VALUE / num2))) {
                throw new OverflowException("multiply");
            }
        }
        return num1 * num2;
    }

    @Override
    protected Double doubleExecute(Double num1, Double num2) {
        return num1 * num2;
    }

    @Override
    protected BigInteger bigIntegerExecute(BigInteger num1, BigInteger num2) {
        return num1.multiply(num2);
    }
}
