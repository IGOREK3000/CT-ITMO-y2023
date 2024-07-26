package expression.executors;

public interface BinaryExecutor<T> {
    T add(T x, T y);
    T subtract(T x, T y);
    T multiply(T x, T y);
    T divide(T x, T y);
}
