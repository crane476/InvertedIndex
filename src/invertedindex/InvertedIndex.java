/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invertedindex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Crane476
 */
public class InvertedIndex {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String inDirectory = JOptionPane.showInputDialog(null, "Enter a Directory: ");
        File fileNames = new File(inDirectory);
        File fList[] = fileNames.listFiles();
        ArrayList<String> filePaths = new ArrayList<>();
        ArrayList<String> fileID = new ArrayList<>();
        for (int i = 0; i < fList.length; i++) {
            filePaths.add(fList[i].toString());
            fileID.add(fList[i].getName());
        }
        getTokens(filePaths);
    }

    public static void getTokens(ArrayList<String> files) {
        String newToken;
        HashMap<String, HashMap<Integer, Integer>> index = new HashMap<>();
        HashMap<Integer, Integer> termFrequency = new HashMap<>();
        for (int i = 0; i < files.size(); i++) {
            try {
                Scanner fileIn = new Scanner(new File(files.get(i)));
                while (fileIn.hasNext()) {
                    newToken = fileIn.next();
                    newToken = newToken.toLowerCase();
                    newToken = removeStopWords(newToken);
                    if (!newToken.equals("")) {
                        WordStem(newToken);
                        if (index.containsKey(newToken)) {
                            termFrequency.put(i, index.get(newToken).get(i) + 1);
                            index.put(newToken, termFrequency);
                        } else {
                            termFrequency.put(i, 1);
                            index.put(newToken, termFrequency);
                        }
                    }
                    termFrequency.clear();
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(InvertedIndex.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        output(index);
    }

    public static void output(HashMap<String, HashMap<Integer, Integer>> invertedIndex) {
        Iterator tokens = invertedIndex.keySet().iterator();
        while (tokens.hasNext()) {
            String token = tokens.next().toString();
            System.out.println(token);
            Iterator ID = invertedIndex.get(token).keySet().iterator();
            while (ID.hasNext()) {
                int docID = (Integer) ID.next();
                int frequency = invertedIndex.get(token).get(docID);
                System.out.println("<" + frequency + ", " + docID + ">");
            }

        }
    }

    public static String removeStopWords(String word) {
        List<String> stopWords = Arrays.asList("a", "About", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are",
                "aren't", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both",
                "but", "by", "can't", "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't",
                "doing", "don't", "down", "during", "each", "few", "for", "from", "further", "had", "hadn't",
                "has", "hasn't", "have", "haven't", "having", "he", "he'd", "he'll", "he's", "her", "here",
                "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm",
                "i've", "if", "in", "into", "is", "isn't", "it", "it's", "its", "itself", "let's", "me", "more",
                "most", "mustn't", "my", "myself", "no", "nor", "not", "of", "off", "on", "once", "only", "or",
                "other", "ought", "our", "ours", "ourselves", "out", "over", "own", "same", "shan't", "she", "she'd",
                "she'll", "she's", "should", "shouldn't", "so", "some", "such", "than", "that", "that's", "the", "their",
                "theirs", "them", "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're",
                "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "wasn't", "we", "we'd",
                "we'll", "we're", "we've", "were", "weren't", "what", "what's", "when", "when's", "where", "where's", "which",
                "while", "who", "who's", "whom", "why", "why's", "with", "won't", "would", "wouldn't", "you", "you'd", "you'll",
                "you're", "you've", "your", "yours", "yourself", "yourselves");

        if (stopWords.contains(word)) {
            word = "";
        }
        return word;
    }

    public static void WordStem(String word) {
        Stemmer st = new Stemmer();
        String s1 = st.step1(word);
        String s2 = st.step2(s1);
        String s3 = st.step3(s2);
        String s4 = st.step4(s3);
        String s5 = st.step5(s4);
        word = s5;
        //return word;
    }
}
