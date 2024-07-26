package expression.exceptions;

import expression.*;
import expression.parser.BaseParser;
import expression.parser.CharSource;
import expression.parser.StringSource;

import java.util.*;


/*


priority1:
factor: Unary Brackets Const Variable
 */
public class ExpressionParser extends BaseParser implements TripleParser, ListParser {

    private final Set<String> operations = new HashSet<>(Set.of("+", "-", "*", "/"));

    private final Map<Character, Character> brackets = new HashMap<>(Map.of(
            '(', ')',
            '[', ']',
            '{', '}'
    ));

    private List<String> variables = new ArrayList<>(List.of("x", "y", "z"));

    public ExpressionParser(CharSource source) {
        super(source);
    }

    public ExpressionParser(String source) {
        super(new StringSource(source));
    }

    public ExpressionParser(String source, List<String> variables) {
        super(new StringSource(source));
        this.variables = variables;
    }

    public ExpressionParser() {
    }

    @Override
    public MainExpression parse(String expression) throws BracketsException, MissingArgumentException {
        return (new ExpressionParser(expression)).parse();
    }

    @Override
    public MainExpression parse(String expression, List<String> variables) throws BracketsException, MissingArgumentException {
        return (new ExpressionParser(expression, variables)).parse();
    }

    private MainExpression parse() throws BracketsException, MissingArgumentException {
        final MainExpression mainExpression = parseElement();
        if (eof()) {
            return mainExpression;
        }
        throw error("End of Expression Expected. Found: " + "'" + take() + "'");
    }

    private MainExpression parseElement() throws BracketsException, MissingArgumentException {
        skipWhitespace();
        final MainExpression result = parseExpression();
        skipWhitespace();
        return result;
    }

    private MainExpression parseExpression() throws BracketsException, MissingArgumentException {
        return parseExpressionUntil(END);
    }

    private MainExpression parseBrackets(char bracket) throws BracketsException {
        try {
            return parseExpressionUntil(brackets.get(bracket));
        } catch (IllegalArgumentException | MissingArgumentException e) {
            throw new BracketsException("No closing parenthesis");
        }

    }

    private MainExpression parseExpressionUntil(char ch) throws BracketsException, MissingArgumentException {
        MainExpression expression = parsePriority5();
        if (take(ch)) {
            return expression;
        } else {
            throw error("Expected symbol: " + "'" + ch + "'" + " found: " + this.ch);
        }
    }

    private MainExpression parsePriority5() throws BracketsException, MissingArgumentException {
        MainExpression priorityExpression5 = parseFourthPriority();
        while (take('|')) {
            priorityExpression5 = new OrBitwise(priorityExpression5, parseFourthPriority());
        }
        return priorityExpression5;
    }

    private MainExpression parseFourthPriority() throws BracketsException, MissingArgumentException {
        MainExpression fourthPriorityExpression = parseThirdPriority();
        while (take('^')) {
            fourthPriorityExpression = new XorBitwise(fourthPriorityExpression, parseThirdPriority());
        }
        return fourthPriorityExpression;
    }

    private MainExpression parseThirdPriority() throws BracketsException, MissingArgumentException {
        MainExpression thirdPriorityExpression = parseSecondPriority();
        while (take('&')) {
            thirdPriorityExpression = new AndBitwise(thirdPriorityExpression, parseSecondPriority());
        }
        return thirdPriorityExpression;
    }

    private MainExpression parseSecondPriority() throws BracketsException, MissingArgumentException {
        MainExpression plusMinusExpression = parseFirstPriority(Op.E);

        while (test('+') || test('-')) {
            if (take('+')) {
                plusMinusExpression = new CheckedAdd(plusMinusExpression, parseFirstPriority(Op.ADD));
            } else if (take('-')) {
                plusMinusExpression = new CheckedSubtract(plusMinusExpression, parseFirstPriority(Op.SUBTRACT));
            }
        }
        return plusMinusExpression;
    }

    private MainExpression parseFirstPriority(Op operation) throws BracketsException, MissingArgumentException {
        MainExpression multiDivExpression = parseFactor(operation);

        while (test('*') || test('/')) {
            if (take('*')) {
                multiDivExpression = new CheckedMultiply(multiDivExpression, parseFactor(Op.MULTIPLY));
            } else if (take('/')) {
                multiDivExpression = new CheckedDivide(multiDivExpression, parseFactor(Op.DIVIDE));
            }
        }
        return multiDivExpression;
    }

    private MainExpression parseFactor(Op operation) throws BracketsException, MissingArgumentException {
        MainExpression factorExpression;
        skipWhitespace();
        if (brackets.containsKey(ch)) {
            char ch1 = ch;
            take();
            factorExpression = parseBrackets(ch1);
        } else if (take('-')) {
            factorExpression = parseNegate();
        } else if (Character.isDigit(ch)) {
            factorExpression = parseNumber();
        } else if (take('~')) {
            factorExpression = new NotUnary(parseFactor(Op.E));
        } else if (take('l')) {
            if (take('1')) {
                factorExpression = new L1Unary(parseFactor(Op.E));
            } else if (take('o')) {
                if (take('w')) {
                    factorExpression = new LowUnary(parseFactor(Op.E));
                } else if (take('g')) {
                    expect('2');
                    if (Character.isWhitespace(ch) || brackets.containsKey(ch)) {
                        factorExpression = new CheckedLog2Unary(parseFactor(Op.E));
                    } else {
                        throw new MissingArgumentException("log2, found: '" + ch + "'");
                    }
                } else {
                    throw error("Invalid symbol: " + "'" + ch + "'");
                }
            } else {
                throw error("Invalid symbol: " + "'" + ch + "'");
            }
        } else if (take('p')) {
            expect("ow2");
            if (Character.isWhitespace(ch) || brackets.containsKey(ch)) {
                factorExpression = new CheckedPow2Unary(parseFactor(Op.E));
            } else {
                throw new MissingArgumentException("pow2, found: '" + ch + "'");
            }

        } else if (take('h')) {
            expect("igh");
            factorExpression = new HighUnary(parseFactor(Op.E));
        } else if (take('t')) {
            expect('1');
            factorExpression = new T1Unary(parseFactor(Op.E));
        } else if (Character.isJavaIdentifierStart(ch)) {
            StringBuilder sb = new StringBuilder();
            while (Character.isJavaIdentifierPart(ch) && !eof()) {
                sb.append(ch);
                take();
            }
            if (!variables.contains(sb.toString())) {
                throw error("not expected variable " + "'" + sb + "'");
            } else {
                Variable v = new Variable(variables.indexOf(sb.toString()));
                v.setName(sb.toString());
                factorExpression = v;
            }
        } else if (operations.contains(String.valueOf(ch))) {
            if (operation != Op.E) {
                throw new MissingArgumentException(operation.toString() + " on position " + source.getPosition());
            } else {
                throw new MissingArgumentException("Missing first argument on position: " + source.getPosition());
            }
        } else if (take(END)) {
            if (operation != Op.E) {
                throw new MissingArgumentException(operation.toString() + "'s last argument on position: " + source.getPosition());
            } else {
                throw new MissingArgumentException("expected factor, found EOF on position: " + source.getPosition());
            }
        } else if (ch == ')') {
            if (operation != Op.E) {
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


    private MainExpression parseNegate() throws BracketsException, MissingArgumentException {
        MainExpression factorExpression;
        boolean hasWhitespace = Character.isWhitespace(ch);
        skipWhitespace();
        if (hasWhitespace || test('-') || test('(') || test('[') ||
                test('{') || Character.isLetter(ch) || test('0') || Character.isJavaIdentifierStart(ch)) {
            factorExpression = new CheckedNegate(parseFactor(Op.NEGATE));
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

    private MainExpression parseNegativeNumber() {
        return parseInteger(new StringBuilder("-"));
    }

    private MainExpression parseNumber() {
        return parseInteger(new StringBuilder());
    }

    private MainExpression parseInteger(StringBuilder sb) {
        takeInteger(sb);
        try {
            return new Const(Integer.parseInt(sb.toString()));
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

