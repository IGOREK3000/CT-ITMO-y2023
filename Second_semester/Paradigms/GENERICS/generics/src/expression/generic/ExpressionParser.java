package expression.generic;


import expression.exceptions.BracketsException;
import expression.exceptions.IncorrectArgumentException;
import expression.exceptions.MissingArgumentException;
import expression.executors.Executor;
import expression.parser.CharSource;
import expression.parser.StringSource;

import java.util.*;

public class ExpressionParser<T> extends BaseParser implements TripleParser<T> {
    private final Set<String> OPERATIONS = Set.of("+", "-", "*", "/");

    private final Map<String, Oper> BINARYOPERATIONS = Map.of(
            "+", Oper.ADD,
            "-", Oper.SUBTRACT,
            "*", Oper.MULTIPLY,
            "/", Oper.DIVIDE);

    private final Map<Character, Character> BRACKETS = Map.of(
            '(', ')',
            '[', ']',
            '{', '}'
    );

    private Executor<T> executor;

    private List<String> variables = new ArrayList<>(List.of("x", "y", "z"));

    public ExpressionParser(CharSource source) {
        super(source);
    }

    public ExpressionParser(String source) {
        super(new StringSource(source));
    }

    public ExpressionParser(String source, Executor<T> executor) {
        super(new StringSource(source));
        this.executor = executor;
    }

    public ExpressionParser(String source, List<String> variables) {
        super(new StringSource(source));
        this.variables = variables;
    }

    public ExpressionParser() {
    }

    @Override
    public MainExpression<T> parse(String expression) throws BracketsException, MissingArgumentException {
        return (new ExpressionParser<T>(expression)).parse();
    }

    public MainExpression<T> parse(String expression, Executor<T> executor) throws BracketsException, MissingArgumentException {
        return (new ExpressionParser<T>(expression, executor)).parse();
    }

    private MainExpression<T> parse() throws BracketsException, MissingArgumentException {
        final MainExpression<T> mainExpression = parseElement();
        if (eof()) {
            return mainExpression;
        }
        throw error("End of Expression Expected. Found: " + "'" + take() + "'");
    }

    private MainExpression<T> parseElement() throws BracketsException, MissingArgumentException {
        skipWhitespace();
        final MainExpression<T> result = parseExpression();
        skipWhitespace();
        return result;
    }

    private MainExpression<T> parseExpression() throws BracketsException, MissingArgumentException {
        return parseExpressionUntil(END);
    }

    private MainExpression<T> parseBrackets(char bracket) throws BracketsException {
        try {
            return parseExpressionUntil(BRACKETS.get(bracket));
        } catch (IllegalArgumentException | MissingArgumentException e) {
            throw new BracketsException("No closing parenthesis");
        }

    }

    private MainExpression<T> parseExpressionUntil(char ch) throws BracketsException, MissingArgumentException {
    //    MainExpression<T> expression = parseSecondPriority();
        MainExpression<T> expression = parsePriority(2, Oper.E);
        if (take(ch)) {
            return expression;
        } else {
            throw error("Expected symbol: " + "'" + ch + "'" + " found: " + this.ch);
        }
    }

    private String[] getPriorityOperations(int priority) {
        return switch (priority) {
            case 1 -> new String[]{"*", "/"};
            case 2 -> new String[]{"+", "-"};
            default -> throw new IncorrectArgumentException("Chose wrong priority");
        };
    }

    private boolean getPriorityCondition(String[] operations) {
        boolean condition = false;
        for (String operation : operations) {
            condition = condition || test(operation.charAt(0));
        }
        return condition;
    }

    private MainExpression<T> makeBinaryOperation(Oper op, MainExpression<T> first, MainExpression<T> second) {
        return switch (op) {
            case ADD -> new GenericAdd<>(first, second, executor);
            case SUBTRACT -> new GenericSubtract<>(first, second, executor);
            case MULTIPLY -> new GenericMultiply<>(first, second, executor);
            case DIVIDE -> new GenericDivide<>(first, second, executor);
            default -> throw new IncorrectArgumentException("Chose wrong operation");
        };
    }

    private MainExpression<T> parsePriority(int priority, Oper operation) throws BracketsException, MissingArgumentException {
        if (priority <= 0) {
            return parseFactor(operation);
        }
        MainExpression<T> priorityExpression = parsePriority(priority - 1, Oper.E);
        String[] operations = getPriorityOperations(priority);

        while (getPriorityCondition(operations)) {
            Oper op = BINARYOPERATIONS.get(String.valueOf(take()));
            priorityExpression = makeBinaryOperation(op, priorityExpression, parsePriority(priority - 1, op));

        }
        return priorityExpression;
    }

    private MainExpression<T> parseFactor(Oper operation) throws BracketsException, MissingArgumentException {
        MainExpression<T> factorExpression;
        skipWhitespace();
        if (BRACKETS.containsKey(ch)) {
            char ch1 = ch;
            take();
            factorExpression = parseBrackets(ch1);
        } else if (take('-')) {
            factorExpression = parseNegate();
        } else if (Character.isDigit(ch)) {
            factorExpression = parseNumber();
        } else if (Character.isJavaIdentifierStart(ch)) {
            StringBuilder sb = new StringBuilder();
            while (Character.isJavaIdentifierPart(ch) && !eof()) {
                sb.append(ch);
                take();
            }
            if (!variables.contains(sb.toString())) {
                throw error("not expected variable " + "'" + sb + "'");
            } else {
                GenericVariable<T> v = new GenericVariable<>(variables.indexOf(sb.toString()));
                v.setName(sb.toString());
                factorExpression = v;
            }
        } else if (OPERATIONS.contains(String.valueOf(ch))) {
            if (operation != Oper.E) {
                throw new MissingArgumentException(operation.toString() + " on position " + source.getPosition());
            } else {
                throw new MissingArgumentException("Missing first argument on position: " + source.getPosition());
            }
        } else if (take(END)) {
            if (operation != Oper.E) {
                throw new MissingArgumentException(operation.toString() + "'s last argument on position: " + source.getPosition());
            } else {
                throw new MissingArgumentException("expected factor, found EOF on position: " + source.getPosition());
            }
        } else if (ch == ')') {
            if (operation != Oper.E) {
                throw new MissingArgumentException(operation.toString() + "'s last argument on position: " + source.getPosition());
            } else {
                throw new BracketsException("Expected start of factor, found " + ch + " on position: " + source.getPosition());
            }
        } else {
            throw error("Invalid symbol " + "'" + ch + "'" + " on position " + source.getPosition());
        }
        skipWhitespace();
        return factorExpression;
    }


    private MainExpression<T> parseNegate() throws BracketsException, MissingArgumentException {
        MainExpression<T> factorExpression;
        boolean hasWhitespace = Character.isWhitespace(ch);
        skipWhitespace();
        if (hasWhitespace || test('-') || test('(') || test('[') ||
                test('{') || Character.isLetter(ch) || test('0') || Character.isJavaIdentifierStart(ch)) {
            factorExpression = new GenericNegate<>(parseFactor(Oper.NEGATE), executor);
        } else if (between('1', '9')) {
            factorExpression = parseNegativeNumber();
        } else {
            throw error("Invalid symbol: " + "'" + ch + "'");
        }
        return factorExpression;
    }

    private void skipWhitespace() {
        while (Character.isWhitespace(ch)) {
            take();
        }
    }

    private MainExpression<T> parseNegativeNumber() {
        return parseInteger(new StringBuilder("-"));
    }

    private MainExpression<T> parseNumber() {
        return parseInteger(new StringBuilder());
    }

    private MainExpression<T> parseInteger(StringBuilder sb) {
        takeInteger(sb);
        try {
            return new GenericConst<>(executor.executeNumber(sb.toString()));
        } catch (NumberFormatException e) {
            throw error("Invalid number: " + sb);
        }
    }

    private void takeDigits(final StringBuilder sb) {
        while (between('0', '9')) {
            sb.append(take());
        }
    }

    private void takeInteger(final StringBuilder sb) {
        if (take('-')) {
            sb.append('-');
        }
        if (take('0')) {
            sb.append('0');
        } else if (between('1', '9')) {
            takeDigits(sb);
        } else {
            throw error("Invalid number");
        }
    }
}

