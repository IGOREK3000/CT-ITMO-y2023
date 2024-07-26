package expression.executors;

import expression.exceptions.DBZException;

import java.math.BigInteger;

public class BigIntegerExecutor implements Executor<BigInteger> {
    @Override
    public BigInteger add(BigInteger x, BigInteger y) {
        return x.add(y);
    }

    @Override
    public BigInteger subtract(BigInteger x, BigInteger y) {
        return x.subtract(y);
    }

    @Override
    public BigInteger multiply(BigInteger x, BigInteger y) {
        return x.multiply(y);
    }

    @Override
    public BigInteger divide(BigInteger x, BigInteger y) {
        if (y.toString().equals("0")) {
            throw new DBZException("Exception for division by zero");
        }
        return x.divide(y);
    }

    @Override
    public BigInteger negate(BigInteger x) {
        return x.negate();
    }

    @Override
    public BigInteger executeNumber(String number) {
        return new BigInteger(number);
    }
}
