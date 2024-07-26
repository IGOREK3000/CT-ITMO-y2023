package expression.executors;

import expression.exceptions.DBZException;
import expression.exceptions.OverflowException;

public class IntegerExecutor implements Executor<Integer> {

    private final boolean checkOverflow;

    public IntegerExecutor() {
        checkOverflow = true;
    }

    public IntegerExecutor(boolean checkOverflow) {
        this.checkOverflow = checkOverflow;
    }

    @Override
    public Integer add(Integer x, Integer y) {
        if (!checkOverflow) {
            return x + y;
        }
        if (x > 0 && y > 0 && Integer.MAX_VALUE - x < y) {
            throw new OverflowException("add overflowed");
        }
        if (x < 0 && y < 0 && Integer.MIN_VALUE - x > y) {
            throw new OverflowException("add overflowed");
        }
        return x + y;
    }

    @Override
    public Integer subtract(Integer x, Integer y) {
        if (!checkOverflow) {
            return x - y;
        }
        if (y == Integer.MIN_VALUE) {
            if (x >= 0) {
                throw new OverflowException("subtract overflowed");
            }
            if (x == Integer.MIN_VALUE) {
                return 0;
            }
        }

        if ((y > 0 && x < Integer.MIN_VALUE + y) || (y < 0 && x > Integer.MAX_VALUE + y)) {
            throw new OverflowException("subtract overflowed");
        }
        return x - y;
    }

    @Override
    public Integer multiply(Integer x, Integer y) {
        if (!checkOverflow) {
            return x * y;
        }
        if (x == Integer.MIN_VALUE || y == Integer.MIN_VALUE) {
            if ((y != 0 && y != 1) && (x != 0 && x != 1)) {
                throw new OverflowException("multiply");
            }
        } else if (x != 0 && y != 0) {
            if (((x > 0 && y < 0) && (y < Integer.MIN_VALUE / x)) ||
                    ((x < 0 && y > 0) && (x < Integer.MIN_VALUE / y)) ||
                    ((x > 0 && y > 0) && (x > Integer.MAX_VALUE / y)) ||
                    ((x < 0 && y < 0) && (x < Integer.MAX_VALUE / y))) {
                throw new OverflowException("multiply");
            }
        }
        return x * y;
    }

    @Override
    public Integer divide(Integer x, Integer y) {
        if (y == 0) {
            throw new DBZException("Exception for division by zero");
        }
        if (!checkOverflow) {
            return x / y;
        }
        if (x == Integer.MIN_VALUE && y == -1) {
            throw new OverflowException("divide");
        }

        return x / y;
    }

    @Override
    public Integer negate(Integer x) {
        if (!checkOverflow) {
            return -x;
        }
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException("Negate overflowed");
        }
        return -x;
    }

    @Override
    public Integer executeNumber(String number) {
        return Integer.parseInt(number);
    }
}
