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
    private Boolean levenshtein;

    public WordChains(Set<String> dictionary, Boolean levenshtein) {
        this.dictionary = dictionary;
        this.allWords = new HashMap<String, String>(1);
        this.currentWords = new HashSet<String>(1);
        this.levenshtein = levenshtein;
    }

    public ArrayList<String> run(String source, String target) {
        if (!this.levenshtein && (source.length() != target.length())) throw new IllegalArgumentException();
        this.currentWords.add(source.toLowerCase());
        this.allWords.put(source.toLowerCase(), null);
        while (!this.currentWords.isEmpty() && !this.allWords.containsKey(target.toLowerCase())) {
            System.out.println(this.currentWords);
            this.exploreCurrentWords();
        }
        return this.buildPath(source.toLowerCase(), target.toLowerCase());
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
            Set<String> adjacentWords = this.levenshtein ? this.levenshteinAdjacentWords(currentWord) : this.adjacentWords(currentWord);
            for (String word : adjacentWords) {
                if (!this.allWords.containsKey(word)) {
                    newCurrentWords.add(word);
                    this.allWords.put(word, currentWord);
                }
            }
        }
        this.currentWords = newCurrentWords;
    }

    private Boolean properWord(String word) {
        return this.dictionary.contains(word) && !this.allWords.containsKey(word);
    }

    private void addEquivalentLengthAdjacents(Set<String> adjacentWordSet, String word) {
        for (int i = 0; i < word.length(); i++) {
            for (char c = 'a'; c <= 'z'; c++) {
                String newWord = word.substring(0, i) + c + word.substring(i + 1);
                if (this.properWord(newWord)) {
                    adjacentWordSet.add(newWord);
                }
            }
        }
    }

    private void addSmallerLengthAdjacents(Set<String> adjacentWordSet, String word) {
        for (int i = 0; i < word.length(); i++) {
            String newWord = word.substring(0, i) + word.substring(i + 1);
            if (this.properWord(newWord)) {
                adjacentWordSet.add(newWord);
            }
        }
    }

    private void addLargerLengthAdjacents(Set<String> adjacentWordSet, String word) {
        for (int i = 0; i <= word.length(); i++) {
            for (char c = 'a'; c <= 'z'; c++) {
                String addedNewWord = word.substring(0, i) + c + word.substring(i);
                if (this.properWord(addedNewWord)) {
                    adjacentWordSet.add(addedNewWord);
                }
            }
        }
    }

    private Set<String> adjacentWords(String word) {
        Set<String> adjacentWordSet = new HashSet<String>(0);
        this.addEquivalentLengthAdjacents(adjacentWordSet, word);
        return adjacentWordSet;
    }

    private Set<String> levenshteinAdjacentWords(String word) {
        Set<String> adjacentWordSet = new HashSet<String>(0);
        this.addSmallerLengthAdjacents(adjacentWordSet, word);
        this.addEquivalentLengthAdjacents(adjacentWordSet, word);
        this.addLargerLengthAdjacents(adjacentWordSet, word);
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
        WordChains wordChains = new WordChains(dictionary, false);
        long startTime = System.currentTimeMillis();
        ArrayList<String> path = wordChains.run("ding", "camp");
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) + "ms");
        System.out.println(path);

        WordChains levenshteinWordChains = new WordChains(dictionary, true);
        long otherStartTime = System.currentTimeMillis();
        ArrayList<String> otherPath = levenshteinWordChains.run("ding", "dinner");
        long otherEndTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (otherEndTime - otherStartTime) + "ms");
        System.out.println(otherPath);
    }
}
