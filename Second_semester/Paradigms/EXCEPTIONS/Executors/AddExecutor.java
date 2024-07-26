package expression.generic;

import expression.exceptions.OverflowException;

import java.math.BigInteger;

public class AddExecutor extends BinaryExecutor {


    @Override
    protected Integer intExecute(Integer num1, Integer num2) {
        if (num1 > 0 && num2 > 0 && Integer.MAX_VALUE - num1 < num2) {
            throw new OverflowException("add overflowed");
        }
        if (num1 < 0 && num2 < 0 && Integer.MIN_VALUE - num1 > num2) {
            throw new OverflowException("add overflowed");
        }
        return num1 + num2;
    }

    @Override
    protected Double doubleExecute(Double num1, Double num2) {
        return num1 + num2;
    }

    @Override
    protected BigInteger bigIntegerExecute(BigInteger num1, BigInteger num2) {
        return num1.add(num2);
    }
}
