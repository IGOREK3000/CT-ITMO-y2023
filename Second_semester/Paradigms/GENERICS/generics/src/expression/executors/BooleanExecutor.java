package expression.executors;

import expression.exceptions.DBZException;

public class BooleanExecutor implements Executor<Boolean> {
    @Override
    public Boolean add(Boolean x, Boolean y) {
        return x | y;
    }

    @Override
    public Boolean subtract(Boolean x, Boolean y) {
        return x ^ y;
    }

    @Override
    public Boolean multiply(Boolean x, Boolean y) {
        return x & y;
    }

    @Override
    public Boolean divide(Boolean x, Boolean y) {
        if (!y) {
            throw new DBZException("DBZ in Boolean: ");
        }
        return (x ? 1 : 0) / (y ? 1 : 0) != 0;
    }

    @Override
    public Boolean executeNumber(String number) {
        int x = Integer.parseInt(number);

        return x != 0;

        //return Integer.parseInt(number) % 2 == 1;
    }

    @Override
    public Boolean negate(Boolean x) {
        return x;
    }
}
