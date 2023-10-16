
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
        try {
            Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), "UTF-8"));
            int read = 0;
            try {
                char[] buffer = new char[1024];
                StringBuilder currentWord = new StringBuilder("");
                while (read >= 0) {
                    read = reader.read(buffer);
                //    System.out.println(read);

                    for (int i = 0; i < read; i++) {
                        if (Character.getType(buffer[i]) == Character.DASH_PUNCTUATION || Character.isLetter(buffer[i])
                         || buffer[i] == '\'') {
                            currentWord.append(buffer[i]);
                        } else {
                            if (currentWord.length() > 0) {
                                if (words.length < wordsCount + 1) {
                                    words = Arrays.copyOf(words, words.length*2);
                                }
                                words[wordsCount++] = currentWord.toString().toLowerCase();
                            }
                            currentWord.setLength(0);
                        }
                    }
                } 
                if (currentWord.length() > 0) {
                    if (words.length < wordsCount + 1) {
                        words = Arrays.copyOf(words, words.length*2);
                    }
                    words[wordsCount++] = currentWord.toString().toLowerCase();
                }

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
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), "UTF-8"));
                    try {                  
                        for (int i = 0; i < uniqueWordsCount; i++) {
                            writer.write(uniqueWords[i] + " " + freq[i]);
                            writer.newLine();
                        }
                  //      System.out.println(wordsCount);

                    } catch (IOException e) {
                        System.out.println("Can't write into file" + e.getMessage());
                    } finally {
                        writer.close();
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Can't find file to write in" + e.getMessage());
                }
                               
            } catch (IOException e) {
                System.out.println("Can't read file " + e.getMessage());
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            System.out.println("Can't open file or close reader" + e.getMessage());
        }

        

    }
}
