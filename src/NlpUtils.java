import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;


public class NlpUtils {
	
	// define pre-trained models
	private static NlpUtils INSTANCE = null;
	private final String SENT_DET_MODEL = "en-sent.bin";
	private final String POS_TAG_MODEL = "en-pos-perceptron.bin";
	private final String NAME_DET_MODEL = "en-ner-person.bin";
	private final String LOCATION_DET_MODEL = "en-ner-location.bin";
	
	// init OpenNLP tools null, load as needed
	SentenceDetectorME sentenceDetector = null;
	SimpleTokenizer tokenizer = null;
	POSTaggerME posTagger = null;
	NameFinderME nameFinder = null;
	NameFinderME locationFinder = null;
	
	
	/**
	 * Private constructor to enforce Singleton pattern
	 */
	private NlpUtils() { }
	
	
	/**
	 * Singleton Instance Getter
	 * @return
	 */
	public static NlpUtils getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new NlpUtils();
		}
		return INSTANCE;
	}
	
	
	/**
	 * Detect Sentences
	 * Uses OpenNLP pre-trained sentence detection model to construct array of sentences from input text
	 * @param inputFilePath path to input text
	 * @return String[] sentences
	 * @throws IOException
	 */
	public String[] detectSentences(String inputFilePath) throws IOException {
		String inputText = getStringFromFile(inputFilePath);
		if(sentenceDetector == null) {
			InputStream sentDetModelInput = new FileInputStream(SENT_DET_MODEL);
			SentenceModel sentDetModel = new SentenceModel(sentDetModelInput);
		    sentenceDetector = new SentenceDetectorME(sentDetModel);
		}
	    return sentenceDetector.sentDetect(inputText);
	}
	
	
	/**
	 * Tokenize String
	 * @param sentence
	 * @return String[] of tokens
	 */
	public String[] tokenizeString(String s) {
		if(tokenizer == null) {
			tokenizer = SimpleTokenizer.INSTANCE;
		}
		return tokenizer.tokenize(s);
	}
	
	
	/**
	 * Tag tokens with part of speech (POS) tags
	 * @param tokens to be tagged
	 * @return String[] of POS Tags
	 * @throws IOException
	 */
	public String[] tagPOS(String[] tokens) throws IOException {
		if(posTagger == null) {
			InputStream posTagModelInput = new FileInputStream(POS_TAG_MODEL);
			POSModel posTagModel = new POSModel(posTagModelInput);
			posTagger = new POSTaggerME(posTagModel);
		}
		return posTagger.tag(tokens);
	}
	
	
	/**
	 * Detect Names
	 * @param nameType 'person' or 'location'
	 * @param tokens to be searched for names
	 * @return String[] of names found
	 * @throws IOException
	 */
	public String[] detectNames(String nameType, String[] tokens) throws IOException {
		if(nameType.equals("person")) {
			Span[] spans = detectPersonNames(tokens);
			return Span.spansToStrings(spans, tokens);
		} else if(nameType.equals("location")) {
			Span[] spans = detectLocationNames(tokens);
			return Span.spansToStrings(spans, tokens);
		} else {
			throw new Error("Invalid name type: 'person' and 'location' are the two options");
		}
	}
	
	
	/**
	 * Detect People Names
	 * @param tokens
	 * @return Span[]
	 * @throws IOException
	 */
	private Span[] detectPersonNames(String[] tokens) throws IOException {
		if(nameFinder == null) {
			InputStream findNameModelInput = new FileInputStream(NAME_DET_MODEL);
			TokenNameFinderModel findNameModel = new TokenNameFinderModel(findNameModelInput);
			nameFinder = new NameFinderME(findNameModel);
		}
		return nameFinder.find(tokens);
	}
	
	
	/**
	 * Detect Location Names
	 * @param tokens
	 * @return Span[]
	 * @throws IOException
	 */
	private Span[] detectLocationNames(String[] tokens) throws IOException {
		if(locationFinder == null) {
			InputStream findLocationModelInput = new FileInputStream(LOCATION_DET_MODEL);
			TokenNameFinderModel findNameModel = new TokenNameFinderModel(findLocationModelInput);
			locationFinder = new NameFinderME(findNameModel);
		}
		return locationFinder.find(tokens);
	}
	
	
	/**
	 * Get String From File
	 * @param filePath
	 * @return
	 */
	public String getStringFromFile(String filePath) {
	    String content = "";
	    try {
	    		content = new String ( Files.readAllBytes( Paths.get(filePath) ) );
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return content;
	}
	
	
	/**
	 * Print String Array
	 * @param arr
	 */
	public void printStringArray(String[] arr) {
		String result = "";
		for(int i = 0, n = arr.length; i < n; i++) {
			result += arr[i] + " ";
		}
		System.out.println(result);
	}
	
	
	/**
	 * Printint String 2D Arrray
	 * @param matrix
	 */
	public void printStringMatrix(String[][] matrix) {
		for(int i = 0, n = matrix.length; i < n; i++) {
			String row = "";
			for(int j = 0, m = matrix[i].length; j < m; j++) {
				row += matrix[i][j] + " ";
			}
			System.out.println(row);
		}
	}
	
	
	/**
	 * Print Matrix values
	 * Print values of a sparse 2D array
	 * @param matrix
	 */
	public void printMatrixValues(String[][] matrix) {
		String result = "";
		for(int i = 0, n = matrix.length; i < n; i++) {
	  	  	for(int j = 0, m = matrix[i].length; j < m; j++) {
	  	  		result += matrix[i][j] + ", ";
	  	  	}
	      }
		System.out.println(result);
	}
	
	
}