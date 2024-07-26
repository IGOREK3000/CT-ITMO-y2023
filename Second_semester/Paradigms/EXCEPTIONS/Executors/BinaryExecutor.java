package expression.generic;

import java.math.BigInteger;

public abstract class BinaryExecutor implements Executor {

    @Override
    public <T extends Number> Number execute(T num1, T num2) {
        if (num1 instanceof Integer) {
            return intExecute(num1.intValue(), num2.intValue());
        } else if (num1 instanceof Double) {
            return doubleExecute(num1.doubleValue(), num2.doubleValue());
        } else if (num1 instanceof BigInteger) {
            return bigIntegerExecute((BigInteger) num1, (BigInteger) num2);
        } else {
            throw new IllegalArgumentException("Unsupported number type");
        }
    }

    protected abstract Integer intExecute(Integer num1, Integer num2);

    protected abstract Double doubleExecute(Double num1, Double num2);

    protected abstract  BigInteger bigIntegerExecute(BigInteger num1, BigInteger num2);
}



