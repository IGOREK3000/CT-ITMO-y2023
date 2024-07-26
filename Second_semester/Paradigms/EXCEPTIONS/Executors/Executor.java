package expression.generic;

public interface Executor {
    public <T extends Number> Number execute(T num1, T num2);
}
