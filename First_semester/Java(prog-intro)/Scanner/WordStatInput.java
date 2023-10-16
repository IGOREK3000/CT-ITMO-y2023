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
import java.util.Arrays;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.FileNotFoundException;

/* 
Character.DASH_PUNCTUATION
toLowerCase()
*/
public class WordStatInput {

    public static void main(String[] args) {
        String[] uniqueWords = new String[1];
        int[] freq = new int[1];
        String[] words = new String[1];
        int wordsCount = 0;
        int uniqueWordsCount = 0;
        Scanner sc = new Scanner(new File(args[0]));
        while (sc.hasNextWord()) {
            if (words.length < wordsCount + 1) {
                words = Arrays.copyOf(words, words.length*2);
            }
            words[wordsCount++] = sc.nextWord().toLowerCase();
        }
        sc.close();
        boolean[] visited = new boolean[wordsCount];
        Arrays.fill(visited, false);
        for (int i = 0; i < wordsCount; i++) {
            if (!visited[i]) {
                //        visited[i] = true;
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


        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8));
            try {
                for (int i = 0; i < uniqueWordsCount; i++) {
                    writer.write(uniqueWords[i] + " " + freq[i]);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Can't write into file" + e.getMessage());
            } finally {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Can't close writer: " + e.getMessage());
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("Can't find file to write in" + e.getMessage());
        }




    }
}
