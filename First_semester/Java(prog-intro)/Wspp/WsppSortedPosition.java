import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class WsppSortedPosition {
    public static void main(String[] args) {
        Map<String, IntList[]> wordsOccurance = new TreeMap<>();
        try {
            Scanner fileSc = new Scanner(new File(args[0]));
            int linesCount = 1;
            while (fileSc.hasNextLine()) {
                Scanner sc = new Scanner(fileSc.nextLine());
                String[] words = new String[5];
                int num = 0;
                while (sc.hasNextWord()) {
                    if (words.length <= num + 1) {
                        words = Arrays.copyOf(words, num * 2);
                    }
                    words[num] = sc.nextWord().toLowerCase();
                    num++;
                }
                for (int i = 0; i < num; i++) {
                    IntList[] list = wordsOccurance.getOrDefault(words[i], new IntList[] {new IntList(), new IntList()});
                    list[0].add(linesCount);
                    list[1].add(num - i);
                    wordsOccurance.put(words[i], list);
                }
                linesCount++;

            }
            fileSc.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(args[1]), StandardCharsets.UTF_8)
            );
            try {
                for (String key : wordsOccurance.keySet()) {
                    IntList[] list = wordsOccurance.get(key);
                    int size = list[0].size();
                    writer.write(key + " " + size);
                    for (int i = 0; i < size; i++) {
                        if (list[1].get(i) == 0) {
                            break;
                        }
                        writer.write( " " + list[0].get(i) + ":" + list[1].get(i));
                    }
                    writer.newLine();
                }
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
