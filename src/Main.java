/**
 * OpenNLP Introduction
 * Using OpenNLP library and pre-trained models to complete natural language processing tasks.
 * Project includes sentence detection, tokenization, part of speech (POS) tagging, and entity identification.
 */
import java.io.IOException;


public class Main {
	
	// path to input text file
	private static final String INPUT_PATH = "news article.txt";
	
	// custom Utils class to abstract out the file i/o
	// all sentence detection, tokenization, and part of speech tagging is implemented in NlpUtils
	private static final NlpUtils nlpUtils = NlpUtils.getInstance();
	
	
	/**
	 * Main
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
	  // split input file content into array of sentences
      String[] sentences = nlpUtils.detectSentences(INPUT_PATH);
      System.out.println("\n\nPrinting detected sentences:");
      for(int i = 0, n = sentences.length; i < n; i++) {
    	  	System.out.println("\n" + sentences[i]);
      }

      // we iterate over each sentence and complete 3 tasks per iteration:
      // 1: create 2D array that contains an array of tokens for each sentence
      // 2: create 2D array that contains an array of POSTags for each sentence
      // 3. print sentence tokens and POS Tags to console for inspection  
      System.out.println("\n\nPrinting sentence tokens vs sentence POS tags for comparison:\n\n");
      int sentenceCount = sentences.length;
      String[][] tokenizedSentences = new String[sentenceCount][];
      String[][] posTaggedSentences = new String[sentenceCount][];
      for(int i = 0; i < sentenceCount; i++) {
    	  	tokenizedSentences[i] = nlpUtils.tokenizeString(sentences[i]);
    	  	nlpUtils.printStringArray(tokenizedSentences[i]);
    	  	posTaggedSentences[i] = nlpUtils.tagPOS(tokenizedSentences[i]);
    	  	nlpUtils.printStringArray(posTaggedSentences[i]);
    	  	System.out.println();
      }
      
  
      // next we will search each string for Name and Location entities
      System.out.println("Starting Name and Location search...");
      String[][] locations = new String[sentenceCount][];
      String[][] people = new String[sentenceCount][];
      for(int i = 0; i < sentenceCount; i++) {
    	  	locations[i] = nlpUtils.detectNames("location", tokenizedSentences[i]);
    	  	people[i] = nlpUtils.detectNames("person", tokenizedSentences[i]);
      }
      
      
      // print location results
      System.out.println("\nLocations Found: ");
      nlpUtils.printMatrixValues(locations);
      
      
      // print people results
      System.out.println("\nNames Found: ");
      nlpUtils.printMatrixValues(people);

	}

}
