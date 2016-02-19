/*
 * Name: Justin Ridgeway
 * Course: COSC 4315-001
 * Date: 2/19/16
 * Description: This program reads in text from a given directory of files and
 * uses them to build an inverted index. The inverted index is then written to a
 * file in a directory specified by the user.
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
import java.util.TreeMap;
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
    
    /*
    * Function Name: main()
    * Arguments: String[] args
    * Returns: N/A
    * Description: This method takes two inputs from the user. These inputs
    * are the input file directory, and stop word list directory respectively.
    * The file paths for each file in the input directory are then read into an
    * array. Next, the stop words from the specified stop word file are read
    * into an array. These two arrays are then passed to the getTokens()
    * function.
    */
    public static void main(String[] args) {
        // TODO code application logic here
        //DON'T FORGET TO PUT COMMENT HEADERS FOR FUNCTIONS!!!!!!!!!
        String inDirectory = JOptionPane.showInputDialog(null, "Enter an Input Directory: ");
        File fileNames = new File(inDirectory);
        //add prompt for stopword file
        File fList[] = fileNames.listFiles();
        ArrayList<String> filePaths = new ArrayList<>();
        ArrayList<String> fileID = new ArrayList<>(); //change to arrayList for stopwords
        for (int i = 0; i < fList.length; i++) {
            filePaths.add(fList[i].toString());
            fileID.add(fList[i].getName());
        }
        HashMap<String, HashMap<Integer, Integer>> invertedIndex = getTokens(filePaths); //temporary for debugging purposes
        output(invertedIndex);
    }

    /*
    * Function Name: getTokens()
    * Arguments: ArrayList<String>, ArrayList<String>
    * Returns: N/A
    * Description: This method iterates through a list of files and reads in
    * text from each file. Each word is converted to lowercase, checked for 
    * special characters, and stemmed. If it is a stop word it is removed. Once
    * the word has been tokenized it is then added to the inverted index. After
    * the inverted index has been built it is passed to the output function.
    */
    
    public static HashMap<String, HashMap<Integer, Integer>> getTokens(ArrayList<String> files) {
        String newToken; //current word being read from file
        HashMap<String, HashMap<Integer, Integer>> index = new HashMap<>(); //List of tokens which map to posting
        HashMap<Integer, Integer> posting; //list of document ID's which map to number of occurrences
        for (int i = 0; i < files.size(); i++) { //loop through list of files
            try {
                Scanner fileIn = new Scanner(new File(files.get(i))); //read in word from file
                while (fileIn.hasNext()) {
                    newToken = fileIn.next(); //assign word to newToken
                    newToken = newToken.toLowerCase(); //convert to lowercase
                    newToken = removeStopWords(newToken); //if stopword remove
                    if (!newToken.equals("")) {
                        newToken = newToken.replaceAll("[-+.^:,]", ""); //remove all special characters
                        newToken = WordStem(newToken); //reduce word to root form
                        if (index.containsKey(newToken)) {
                            if (index.get(newToken).containsKey(i)) { //if term has already been encountered in this document
                                HashMap<Integer, Integer> newPosting = (HashMap) index.get(newToken).clone(); //shallow copy of posting
                                newPosting.put(i, newPosting.get(i) + 1); //update number of occurrences
                                index.put(newToken, newPosting); //overwrite posting with new one
                            } else { //term has been encountered, but not in current document
                                HashMap<Integer, Integer> newPosting = (HashMap) index.get(newToken).clone();
                                newPosting.put(i, 1);
                                index.put(newToken, newPosting);
                            }
                        } else { //term has not been encountered yet
                            posting = new HashMap<>();
                            posting.put(i, 1);
                            index.put(newToken, posting);
                        }
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(InvertedIndex.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        //output(index);
        return index;
    }

    /*
    * Function Name: output()
    * Arguments: HashMap<String, HashMap<Integer, Integer>> map, String string
    * Returns: N/A
    * Description: This function iterates through the inverted index and appends 
    * the output to a formatted string. This string is then written to a file
    * in a directory specified by the user.
    */
    
    public static void output(HashMap<String, HashMap<Integer, Integer>> invertedIndex) {
        System.out.printf("%-15s %20s %n", "Token", "<Frequency, DocID>");
        System.out.println(" ");
        TreeMap<String, HashMap<Integer, Integer>> sortedIndex = new TreeMap(invertedIndex); //sort invertedIndex by converting to treeMap
        Iterator tokens = sortedIndex.keySet().iterator();
        while (tokens.hasNext()) {
            String token;
            String posting = "";
            token = tokens.next().toString();
            //System.out.println(token);
            Iterator ID = sortedIndex.get(token).keySet().iterator();
            while (ID.hasNext()) {
                int docID = (Integer) ID.next();
                int frequency = sortedIndex.get(token).get(docID);
                if(sortedIndex.get(token).size() < 2){
                    posting += "<" + frequency + ", " + docID + ">";
                } else {
                    posting += "<" + frequency + ", " + docID + ">\n";
                }
                //System.out.println("\t\t<" + frequency + ", " + docID + ">");
            }
            System.out.printf("%-15s %20s %n", token, posting);
            //System.out.print(token + "\t\t" + posting);
            //System.out.println(" ");
        }
    }

    //remove method.
    public static String removeStopWords(String word) {
        List<String> stopWords = Arrays.asList("a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are",
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

    /*
    * Function Name: WordStem
    * Arguments: String string
    * Returns: String
    * Description: This method takes a string and reduces it to its root form.
    */
    public static String WordStem(String word) {
        Stemmer st = new Stemmer();
        String s1 = st.step1(word);
        String s2 = st.step2(s1);
        String s3 = st.step3(s2);
        String s4 = st.step4(s3);
        String s5 = st.step5(s4);
        word = s5;
        return word;
    }
}
