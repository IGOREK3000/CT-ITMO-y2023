package expression.parser;

import expression.*;


/*


priority1:
factor: Unary Brackets Const Variable
 */
public class ExpressionParser extends BaseParser implements TripleParser {
    public ExpressionParser(CharSource source) {
        super(source);
    }

    public ExpressionParser(String source) {
        super(new StringSource(source));
    }

    public ExpressionParser() {}


    public MainExpression parse(String expression) {
        return (new ExpressionParser(expression)).parse();
    }


    private MainExpression parse() {
        final MainExpression mainExpression =  parseElement();
        if (eof()) {
            return mainExpression;
        }
        throw error("End of Expression Expected. Found: " + "'" + take() + "'");
    }

    private MainExpression parseElement() {
        skipWhitespace();
        final MainExpression result = parseExpression();
        skipWhitespace();
        return result;
    }

    private MainExpression parseExpression() {
        return parseExpressionUntil(END);
    }

    private MainExpression parseBrackets() {
        return parseExpressionUntil(')');
    }

    private MainExpression parseExpressionUntil(char ch) {
        MainExpression expression = parsePriority5();
        if (take(ch)) {
            return expression;
        } else {
            throw error("Expected symbol: " + "'" + ch + "'" + " found: " + this.ch);
        }
    }

    private MainExpression parsePriority5() {
        MainExpression priorityExpression5 = parseFourthPriority();
        while(take('|')){
            priorityExpression5 = new OrBitwise(priorityExpression5, parseFourthPriority());
        }
        return priorityExpression5;
    }

    private MainExpression parseFourthPriority() {
        MainExpression fourthPriorityExpression = parseThirdPriority();
        while(take('^')){
            fourthPriorityExpression = new XorBitwise(fourthPriorityExpression, parseThirdPriority());
        }
        return fourthPriorityExpression;
    }

    private MainExpression parseThirdPriority() {
        MainExpression thirdPriorityExpression = parseSecondPriority();
        while(take('&')){
            thirdPriorityExpression = new AndBitwise(thirdPriorityExpression, parseSecondPriority());
        }
        return thirdPriorityExpression;
    }

    private MainExpression parseSecondPriority() {
        MainExpression plusMinusExpression = parseFirstPriority();
        while(test('+') || test('-')){
            if (take('+')) {
                plusMinusExpression = new Add(plusMinusExpression, parseFirstPriority());
            } else if (take('-')) {
                plusMinusExpression = new Subtract(plusMinusExpression, parseFirstPriority());
            }
        }
        return plusMinusExpression;
    }

    private MainExpression parseFirstPriority() {
        MainExpression multiDivExpression = parseFactor();
        while(test('*') || test('/')){
            if (take('*')) {
                multiDivExpression = new Multiply(multiDivExpression, parseFactor());
            } else if (take('/')) {
                multiDivExpression = new Divide(multiDivExpression, parseFactor());
            }
        }
        return multiDivExpression;
    }

    private MainExpression parseFactor() {
        MainExpression factorExpression;
        skipWhitespace();
        if (take('(')) {
            factorExpression = parseBrackets();
        } else if (take('-')) {
            factorExpression = parseNegate();
        } else if (Character.isDigit(ch)) {
            factorExpression = parseNumber();
        } else if (take('~')) {
            factorExpression = new NotUnary(parseFactor());
        } else if (take('l')) {
            if (take('1')) {
                factorExpression = new L1Unary(parseFactor());
            } else if (take('o')) {
                expect('w');
                factorExpression = new LowUnary(parseFactor());
            } else {
                throw error("Invalid symbol: " + "'" + ch + "'");
            }
        } else if (take('h')) {
            expect("igh");
            factorExpression = new HighUnary(parseFactor());
        } else if (take('t')) {
            expect('1');
            factorExpression = new T1Unary(parseFactor());
        } else if (Character.isLetter(ch)) {
            factorExpression = parseXYZ();
        } else {
            throw error("Invalid symbol: " + "'" + ch + "'");
        }
        skipWhitespace();
        return factorExpression;
    }

    private MainExpression parseNegate() {
        MainExpression factorExpression;
        boolean hasWhitespace = Character.isWhitespace(ch);
        skipWhitespace();
        if (hasWhitespace || test('-') || test('(') || Character.isLetter(ch) || test('0')) {
            factorExpression = new Negate(parseFactor());
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

    private MainExpression parseVariable() {
        final StringBuilder sb = new StringBuilder();
        while (Character.isLetter(ch)) {
            sb.append(take());
        }
        return new Variable(sb.toString());
    }

    private MainExpression parseXYZ() {
        if (!(ch == 'x' || ch == 'y' || ch == 'z')) {
            throw error("xyz");
        }
        return new Variable(String.valueOf(ch));
    }
}
