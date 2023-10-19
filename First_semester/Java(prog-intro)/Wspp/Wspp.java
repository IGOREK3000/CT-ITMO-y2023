import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.FileNotFoundException;
import java.util.Map.Entry;

public class Wspp{
    public static void main(String[] args) {
        Map<String, IntList> wordsOccurance = new LinkedHashMap<>();
        try {
            Scanner sc = new Scanner(new File(args[0]));
            int wordCount = 1;
            while (sc.hasNextWord()) {
                String word = sc.nextWord().toLowerCase();
                IntList list = wordsOccurance.getOrDefault(word, new IntList());
                list.add(wordCount);
                wordsOccurance.put(word, list);
                wordCount++;
            }
            sc.close();

        } catch (FileNotFoundException e) {
            System.out.println("Can't find file: " + e.getMessage());
        }
        try {
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(args[1]), StandardCharsets.UTF_8)
            );
            try {
                for (Map.Entry<String, IntList> entry : wordsOccurance.entrySet()) {
                    IntList list = entry.getValue();
                    writer.write(entry.getKey() + " " + list.size() + " " + list.toString());
                    writer.newLine();
                }
//                    for (String key : wordsOccurance.keySet()) {
//                        IntList list = wordsOccurance.get(key);
//                        writer.write(key + " " + list.size() + " " + list.toString());
//                        writer.newLine();
//                    }
            } catch (IOException e) {
                System.out.println("Can't write into file: " + e.getMessage());
            } finally {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Problem closing writer: " + e.getMessage());
                }

            }

        }  catch (FileNotFoundException e) {
            System.out.println("Can't find file to write in: " + e.getMessage());
        }
    }
}
