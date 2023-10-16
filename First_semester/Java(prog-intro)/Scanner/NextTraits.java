public class NextTraits implements Traits {
    @Override
    public boolean isSeparator(char ch) {
        return Character.isWhitespace(ch);
    }
}