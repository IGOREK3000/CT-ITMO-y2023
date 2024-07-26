package expression.executors;

import expression.exceptions.DBZException;
import expression.exceptions.EvaluateException;

import java.lang.Byte;

public class ByteExecutor implements Executor<Byte> {

    @Override
    public Byte add(Byte x, Byte y) {
        return (byte) (x + y);
    }

    @Override
    public Byte subtract(Byte x, Byte y) {
        return (byte) (x - y);
    }

    @Override
    public Byte multiply(Byte x, Byte y) {
        return (byte) (x * y);
    }

    @Override
    public Byte divide(Byte x, Byte y) {
        if (y == 0) {
            throw new DBZException("Division by zero in Byte: ");
        }

        return (byte) (x / y);
    }

    @Override
    public Byte executeNumber(String number) {
        return (byte) Integer.parseInt(number);
    }

    @Override
    public Byte negate(Byte x) {
        return (byte) -x;
    }
}
