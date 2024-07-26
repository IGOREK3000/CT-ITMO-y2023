package expression.generic;

public class GenericConst<T> implements MainExpression<T> {
    private final T x;

    public GenericConst(T x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return String.valueOf(x);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && this.getClass() == obj.getClass()) {
            return this.x == ((GenericConst<?>) obj).x;
        }
        return false;
    }

    public T getConst() {
        return this.x;
    }

    @Override
    public int hashCode() {
        return x.hashCode();
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return this.x;
    }
}

