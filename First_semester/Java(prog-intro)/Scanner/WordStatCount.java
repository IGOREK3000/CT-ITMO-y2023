import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.Writer;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.io.File;
/*
Character.DASH_PUNCTUATION
toLowerCase()
compareTo()
*/
public class WordStatCount {

    public static void main(String[] args) {
        String[] uniqueWords = new String[1];
        int[] freq = new int[1];
        String[] words = new String[1];
        int wordsCount = 0;
        int uniqueWordsCount = 0;
        try {
            Scanner sc = new Scanner(new File(args[0]));
            while (sc.hasNextWord()) {
                if (words.length < wordsCount + 1) {
                    words = Arrays.copyOf(words, words.length*2);
                }

                words[wordsCount++] = sc.nextWord().toLowerCase();
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }



        boolean[] visited = new boolean[wordsCount];
        Arrays.fill(visited, false);
        for (int i = 0; i < wordsCount; i++) {
            if (!visited[i]) {
                if (uniqueWords.length < uniqueWordsCount + 1) {
                    uniqueWords = Arrays.copyOf(uniqueWords, uniqueWords.length*2);
                    freq = Arrays.copyOf(freq, freq.length*2);
                }
                uniqueWords[uniqueWordsCount] = words[i];
                freq[uniqueWordsCount] = 1;
                for (int j = i + 1; j < wordsCount; j++) {
                    if (words[i].equals(words[j])) {
                        visited[j] = true;
                        freq[uniqueWordsCount]++;
                    }
                }
                uniqueWordsCount++;
            }
        }

        for (int i = 1; i < uniqueWordsCount; i++) {
            int j = i;
            while (j > 0 && freq[j] < freq[j-1]) {
                String interString = uniqueWords[j];
                int interInt = freq[j];
                uniqueWords[j] = uniqueWords[j-1];
                uniqueWords[j-1] = interString;
                freq[j] = freq[j-1];
                freq[j-1] = interInt;
                j--;
            }
        }


        try { //Проверка на существование файла и на ошибку кодировки у Writer
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), "UTF-8"));
            try { // Проверка на IOException в методе write()
                for (int i = 0; i < uniqueWordsCount; i++) {
                    writer.write(uniqueWords[i] + " " + freq[i]);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.err.println("Can't write into file" + e.getMessage());
            } finally {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.err.println("Can't close writer: " + e.getMessage());
                }

            }
        } catch (FileNotFoundException e) {
            System.err.println("Can't find file to write in" + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.err.println("Encoding error while writing: " + e.getMessage());
        }
    }
}

