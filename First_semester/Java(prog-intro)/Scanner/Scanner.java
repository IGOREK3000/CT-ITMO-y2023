import java.io.*;
import java.nio.charset.StandardCharsets;

public class Scanner {
    private final int BUFFER_SIZE = 1024;
    private Reader reader;
    private final char[] buffer = new char[BUFFER_SIZE];
    private int read = -1;
    private int pose = -1;
    private char currentChar;

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

    private boolean hasNextElement(Pattern pattern) {
        if (!hasNextChar()) {
            return false;
        }
        char ch = nextChar();
        while (hasNextChar() && pattern.isSeparator(ch)) {
            ch = nextChar();
        }
        return !pattern.isSeparator(ch);
    }

    private String nextElement(Pattern pattern) {
        char ch = currentChar;
        StringBuilder nextElement = new StringBuilder(Character.toString(ch));
        while (hasNextChar()) {
            ch = nextChar();
            if (!pattern.isSeparator(ch)) {
                nextElement.append(ch);
            } else {
                break;
            }
        }
        return nextElement.toString();
    }
    public boolean hasNext() {return hasNextElement(new NextPattern());}
    public String next() {return nextElement(new NextPattern());}

    public boolean hasNextWord() {
        return hasNextElement(new WordPattern());
    }
    public String nextWord() {
        return nextElement(new WordPattern());
    }

    public boolean hasNextInt() {
        return hasNextElement(new IntPattern());
    }
    public int nextInt() {
        return Integer.parseInt(nextElement(new IntPattern()));
    }

    public boolean hasNextIntAbc() {
        return hasNextElement(new IntAbcPattern());
    }
    public String nextIntAbc() {
        return nextElement(new IntAbcPattern());
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
        String lineSeparator = "";
        StringBuilder nextLine = new StringBuilder();
        while (hasNextChar() && !isLineSeparator(lineSeparator)) {
            ch = nextChar();
            if (isPartOfLineSeparator(ch)) {
                lineSeparator += String.valueOf(ch);
            } else {
                nextLine.append(ch);
            }
        }
        return nextLine.toString();
    }

    void close() {
        try {
            reader.close();
        } catch (IOException e)  {
            System.out.println("Error closing reader: " + e.getMessage());
        }
    }
}
