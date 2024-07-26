package expression.generic;

import java.util.Objects;

public class GenericVariable<T> implements MainExpression<T> {

    private String variable;

    private final int number;

    public GenericVariable(String variable) {
        this.number = 0;
        this.variable = variable;
    }

    public GenericVariable(int number) {
        this.number = number;
    }

    public void setName(String name) {
        this.variable = name;
    }

    @Override
    public String toString() {
        return variable;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && this.getClass() == obj.getClass()) {
            return Objects.equals(this.variable, ((GenericVariable) obj).variable);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return variable.hashCode();
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return switch (variable) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new IllegalArgumentException("No such variable: " + variable);
        };
    }

}
