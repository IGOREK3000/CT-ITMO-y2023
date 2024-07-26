package expression;

import expression.exceptions.EvaluateException;

import java.util.List;
import java.util.Objects;

public class Variable implements MainExpression {

    private String variable;

    private final int number;

    public Variable(String variable) {
        this.number = 0;
        this.variable = variable;
    }

    public Variable(int number) {
        this.number = number;
    }

    public void setName(String name) {
        this.variable = name;
    }

    @Override
    public int evaluate(int x) {
        return x;
    }

    @Override
    public String toString() {
        return variable;
    }

    @Override
    public boolean equals(Object obj) {
        // :NOTE: instanceof
        if (obj != null && this.getClass() == obj.getClass()) {
            return Objects.equals(this.variable, ((Variable) obj).variable);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return variable.hashCode();
    }

    @Override
    public String toMiniString() {
        return toString();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return switch (variable) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new IllegalArgumentException("No such variable: " + variable);
        };
    }

    @Override
    public int evaluate(List<Integer> variables) {
        if (number >= variables.size() || number < 0) {
            throw new EvaluateException("Incorrect argument for evaluate()");
        }
        return variables.get(number);
    }
}
