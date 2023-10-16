import java.io.*;
import java.nio.charset.StandardCharsets;

public class Scanner {
    private final int bufferSize = 10;
    private Reader reader;
    private final char[] buffer = new char[bufferSize];
    private int read = -1;
    private int pose = -1;
    private char currentChar;
    private String lineSeparator = "";
    private Scanner (Reader reader) {
        this.reader = reader;
        try {
            this.read = reader.read(buffer);
        } catch (IOException e) {
            System.out.println("Can't read: " + e.getMessage());
        }

    }

    public Scanner (InputStream stream) {
        this(new InputStreamReader(stream, StandardCharsets.UTF_8));
    }

    public Scanner (String s) {
        this(new StringReader(s));
    }

    public Scanner (File file) throws FileNotFoundException {
        this(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
    }

    private boolean hasNextChar() {
        return read > 0;
    }

    private char nextChar() {
        pose++;
        currentChar = buffer[pose];
        try {
            if (pose >= read - 1) { //заранее считываем следующий буфер
                read = reader.read(buffer);
                pose = -1;
            }
        } catch (IOException e) {
            System.out.println("Can't read next char with reader" + e.getMessage());
            close();
        }
        return currentChar;
    }
    private boolean hasNextElement(Traits traits) {
        if (!hasNextChar()) {
            return false;
        }
        char ch = nextChar();
        while (hasNextChar() && traits.isSeparator(ch)) {
            ch = nextChar();
        }
        return !traits.isSeparator(ch);
    }

    private String nextElement(Traits traits) {
        char ch = currentChar;
        StringBuilder nextElement = new StringBuilder(Character.toString(ch));
        while (hasNextChar()) {
            ch = nextChar();
            if (!traits.isSeparator(ch)) {
                nextElement.append(ch);
            } else {
                break;
            }
        }
        return nextElement.toString();
    }
    public boolean hasNext() {return hasNextElement(new NextTraits());}
    public String next() {return nextElement(new NextTraits());}

    public boolean hasNextWord() {
        return hasNextElement(new WordTraits());
    }
    public String nextWord() {
        return nextElement(new WordTraits());
    }

    public boolean hasNextInt() {
        return hasNextElement(new IntTraits());
    }
    public int nextInt() {
        return Integer.parseInt(nextElement(new IntTraits()));
    }

    public boolean hasNextIntAbc() {
        return hasNextElement(new IntAbcTraits());
    }
    public int nextIntAbc() {
        return convertAbcToInt(nextElement(new IntAbcTraits()));
    }

    public static boolean isPartOfLineSeparator(char ch) {
        return System.lineSeparator().contains(String.valueOf(ch));
    }
    public boolean isLineSeparator(String str) { return System.lineSeparator().equals(str);}
    public boolean hasNextLine() {
        return hasNextChar();
    }
    public String nextLine() {
        char ch = currentChar;
        lineSeparator = "";
        StringBuilder nextLine = new StringBuilder();
        while (hasNextChar()) {
            ch = nextChar();
            if (isPartOfLineSeparator(ch)) {
                lineSeparator += String.valueOf(ch);
            } else {
                nextLine.append(ch);
            }
            if (isLineSeparator(lineSeparator)) {
                break;
            }
        }
        return nextLine.toString();
    }

    public int convertAbcToInt(String nextInt) {
        StringBuilder intNum = new StringBuilder();
        if (nextInt.charAt(0) == '-') {
            intNum.append('-');
            for (int i = 1; i < nextInt.length(); i++) {
                intNum.append(nextInt.charAt(i) - 'a');
            }
        } else {
            for (int i = 0; i < nextInt.length(); i++) {
                intNum.append(nextInt.charAt(i) - 'a');
            }
        }
        return Integer.parseInt(intNum.toString());
    }


    void close() {
        try {
            reader.close();
        } catch (IOException e)  {
            System.out.println("Error closing reader: " + e.getMessage());
        }
    }
}
