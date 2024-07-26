package expression;

import java.util.List;

public class Const implements MainExpression {
    private final int x;

    public Const(int x) {
        this.x = x;
    }

    @Override
    public int evaluate(int x) {
        return this.x;
    }

    @Override
    public String toString() {
        return String.valueOf(x);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && this.getClass() == obj.getClass()) {
            return this.x == ((Const) obj).x;
        }
        return false;
    }

    public int getConst() {
        return this.x;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(x);
    }

    @Override
    public String toMiniString() {
        return toString();
    }


    @Override
    public int evaluate(int x, int y, int z) {
        return this.x;
    }

    @Override
    public int evaluate(List<Integer> variables) {
        return this.x;
    }
}

