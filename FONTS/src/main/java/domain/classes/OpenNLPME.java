package domain.classes;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

/**
 * Singleton class for OpenNLP Named Entity Recognition and Text Processing
 */
public class OpenNLPME {
    /**
     * Atribute: tokenizer does tokenization
     */
    private TokenizerME tokenizer;
    /**
     * Atribute: posTagger does POS tagging
     */
    private POSTaggerME posTagger;
    /**
     * Atribute: lemmatizer does lemmatization
     */
    private DictionaryLemmatizer lemmatizer;

    /**
     * Singleton instance
     */
    private static OpenNLPME INSTANCE = null;

    /**
     * Private constructor to prevent instantiation
     */
    private OpenNLPME(){
        initializeOpenNLP();
    }

    /**
     * Returns the singleton instance of OpenNLPME
     * @return OpenNLPME instance
     */
    public static OpenNLPME getInstance(){
        if(INSTANCE == null){
            INSTANCE = new OpenNLPME();
        }
        return INSTANCE;
    }

    /**
     * Initializes OpenNLP models
     */
    private void initializeOpenNLP() {
        try {
            InputStream tokenModelStream = getClass().getResourceAsStream("/opennlp-en-ud-ewt-tokens-1.3-2.5.4.bin");
            InputStream posModelStream = getClass().getResourceAsStream("/en-pos-maxent.bin");
            InputStream lemmaDictStream = getClass().getResourceAsStream("/en-lemmatizer.dict");

            if (tokenModelStream == null)
                throw new IllegalStateException("Tokenizer model not found in resources!");
            if (posModelStream == null)
                throw new IllegalStateException("POS model not found in resources!");
            if(lemmaDictStream == null)
                throw new IllegalStateException("Lemmanizer diccionary not found in resources!");


            TokenizerModel tokenModel = new TokenizerModel(tokenModelStream);
            POSModel posModel = new POSModel(posModelStream);

            lemmatizer = new DictionaryLemmatizer(lemmaDictStream);
            tokenizer = new TokenizerME(tokenModel);
            posTagger = new POSTaggerME(posModel);
        }catch(Exception e) {
            throw new RuntimeException("Error initializing OpenNLP models", e);
        }
    }

    /**
     * Extracts meaningful words from the given text using tokenization, POS tagging, and lemmatization
     * @param text input text
     * @return string of meaningful words
     */
    public String getMeaningfulWords(String text){
        if(text == null) return null;
        // Tokenize
        String[] tokens = tokenizer.tokenize(text);

        // Tag POS
        String[] tags = posTagger.tag(tokens);

        // Lemmatization
        String[] lemmas = lemmatizer.lemmatize(tokens, tags);


        // Define meaningful POS tags
        List<String> meaningfulTags = Arrays.asList( 
            "NN", "NNS", "NNP", "NNPS", // nouns 
            "VB", "VBD", "VBG", "VBN", "VBP", "VBZ", // verbs 
            "JJ", "JJR", "JJS", // adjectives 
            "RB", "RBR", "RBS" // adverbs 
        );

        List<String> meaningfulWords = new ArrayList<>();

        for (int i = 0; i < tokens.length; i++) {
            //If not a meaningful word
            if (!meaningfulTags.contains(tags[i])) continue;
        
            String lemma = lemmas[i].equals("O") ? tokens[i] : lemmas[i];
            meaningfulWords.add(lemma.toLowerCase());
        }
        return String.join(" ",meaningfulWords);
    }
}