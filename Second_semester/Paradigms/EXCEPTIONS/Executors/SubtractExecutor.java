package expression.generic;

import expression.exceptions.OverflowException;

import java.math.BigInteger;

public class SubtractExecutor extends BinaryExecutor {
    @Override
    protected Integer intExecute(Integer num1, Integer num2) {
        if (num2 == Integer.MIN_VALUE) {
            if (num1 >= 0) {
                throw new OverflowException("subtract overflowed");
            }
            if (num1 == Integer.MIN_VALUE) {
                return 0;
            }
        }

        if ((num2 > 0 && num1 < Integer.MIN_VALUE + num2) || (num2 < 0 && num1 > Integer.MAX_VALUE + num2)) {
            throw new OverflowException("subtract overflowed");
        }
        return num1 - num2;
    }

    @Override
    protected Double doubleExecute(Double num1, Double num2) {
        return num1 - num2;
    }

    @Override
    protected BigInteger bigIntegerExecute(BigInteger num1, BigInteger num2) {
        return num1.subtract(num2);
    }
}
