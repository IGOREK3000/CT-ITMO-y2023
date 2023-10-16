public class IntAbcTraits implements Traits {
    private final String s = "abcdefghij";
    @Override
    public boolean isSeparator(char ch) {
        return !(ch == '-' || s.contains(String.valueOf(ch)));
    }
}
