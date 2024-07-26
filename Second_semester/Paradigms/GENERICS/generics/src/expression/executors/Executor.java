package expression.executors;

public interface Executor<T> extends BinaryExecutor<T>, UnaryExecutor<T> {
    T executeNumber(String number);
}
