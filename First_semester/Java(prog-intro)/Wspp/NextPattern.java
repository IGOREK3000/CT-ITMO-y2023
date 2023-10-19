public class NextPattern implements Pattern {
    @Override
    public boolean isSeparator(char ch) {
        return Character.isWhitespace(ch);
    }
}