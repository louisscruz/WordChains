package WordChains;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by louisstephancruz on 4/30/17.
 */
public class WordChains {
    private Set<String> dictionary;
    private HashMap<String, String> allWords;
    private Set<String> currentWords;

    public WordChains(Set<String> dictionary) {
        this.dictionary = dictionary;
        this.allWords = new HashMap<String, String>(1);
        this.currentWords = new HashSet<String>(1);
    }

    public ArrayList<String> run(String source, String target) {
        if (source.length() != target.length()) throw new IllegalArgumentException();
        this.currentWords.add(source);
        this.allWords.put(source, null);
        while (!this.currentWords.isEmpty() && !this.allWords.containsKey(target)) {
            this.exploreCurrentWords();
        }
        return this.buildPath(source, target);
    }

    private void exploreCurrentWords() {
        Set<String> newCurrentWords = new HashSet<String>(0);
        for (String currentWord : this.currentWords) {
            for (String adjacentWord : this.adjacentWords(currentWord)) {
                if (!this.allWords.containsKey(adjacentWord)) {
                    newCurrentWords.add(adjacentWord);
                    this.allWords.put(adjacentWord, currentWord);
                }
            }
        }
        this.currentWords = newCurrentWords;
    }

    private Set<String> adjacentWords(String word) {
        Set<String> adjacentWordSet = new HashSet<String>(0);
        for (int i = 0; i < word.length(); i++) {
            for (char c = 'a'; c <= 'z'; c++) {
                String newWord = word.substring(0, i) + c + word.substring(i + 1);
                if (this.dictionary.contains(newWord) && !this.allWords.containsKey(newWord)) {
                    adjacentWordSet.add(newWord);
                }
            }
        }
        return adjacentWordSet;
    }

    private ArrayList<String> buildPath(String source, String target) {
        if (!this.allWords.containsKey(target)) throw new NullPointerException();
        ArrayList<String> path = new ArrayList<String>(1);
        path.add(target);
        while (path.get(path.size() - 1) != source) {
            path.add(this.allWords.get(path.get(path.size() - 1)));
        }
        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) {
        String line;
        Set<String> dictionary = new HashSet<String>();
        try {
            String fileName = "dictionary.txt";
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                dictionary.add(line);
            }
        } catch(FileNotFoundException ex) {
            System.out.println("Unable to open dictionary");
        } catch(IOException ex) {
            System.out.println("Error reading line");
        }
        WordChains wordChains = new WordChains(dictionary);
        long startTime = System.currentTimeMillis();
        ArrayList<String> path = wordChains.run("ding", "camp");
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) + "ms");
        System.out.println(path);
    }
}
