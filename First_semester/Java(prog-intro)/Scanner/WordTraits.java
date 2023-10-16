public class WordTraits implements Traits {
    @Override
    public boolean isSeparator(char ch) {
        return !(Character.getType(ch) == Character.DASH_PUNCTUATION || Character.isLetter(ch) || ch == '\'');
    }
}