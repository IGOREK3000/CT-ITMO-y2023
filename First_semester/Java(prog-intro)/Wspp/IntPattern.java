public class IntPattern implements Pattern {
    @Override
    public boolean isSeparator(char ch) {
        return !(Character.isDigit(ch) || ch == '-');
    }
}