public class WordPattern implements Pattern {
    @Override
    public boolean isSeparator(char ch) {
        return !(Character.getType(ch) == Character.DASH_PUNCTUATION || Character.isLetter(ch) || ch == '\'');
    }
}