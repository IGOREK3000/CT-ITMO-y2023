public class IntAbcPattern implements Pattern {
    private final String s = "abcdefghij";
    @Override
    public boolean isSeparator(char ch) {
        return !(ch == '-' || s.contains(String.valueOf(ch)));
    }
}
