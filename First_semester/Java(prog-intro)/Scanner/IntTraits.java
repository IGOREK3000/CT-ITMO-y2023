public class IntTraits implements Traits {
    @Override
    public boolean isSeparator(char ch) {
        return !(Character.isDigit(ch) || ch == '-');
    }
}