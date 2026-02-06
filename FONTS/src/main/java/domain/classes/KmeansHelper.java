package domain.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Helper class for K-means clustering operations
 */
public class KmeansHelper{
    
    


    // Distances 
    //--------------------------------------------------------------------------------
    /**
     * Numeric distance normalized
     * @param a answer
     * @param c centroid
     * @param range
     * @return double distance normalized between 0 and 1, -1 if any is null
     */
    static double  distanceNumeric(Integer a, Integer c,int range){
        if(a == null || c == null) return -1.0;
        return (double) Math.abs(a - c) / range;
    }
    /**
     * Multiple choice ordered distance normalized
     * @param a answer
     * @param c  centroid
     * @param choices
     * @return double distance normalized between 0 and 1, -1 if any is null
     */
    static double distanceMultipleOrder(ArrayList<String> a,ArrayList<String> c,ArrayList<String> choices){
        if(a == null || c == null) return -1.0;
        ArrayList<String> choicesAux = new ArrayList<>(choices);
        choicesAux.sort(null);
        int modA = choicesAux.indexOf(a.get(0));
        int modC = choicesAux.indexOf(c.get(0));
        return (double) Math.abs(modA - modC) / choices.size();

    }
    /**
     * Multiple choice unordered distance normalized (Jaccard)
     * @param a answer
     * @param c centroid
     * @return double distance normalized between 0 and 1, -1 if any is null
     */
    static double distanceMultipleUnOrder(ArrayList<String> a,ArrayList<String> c){
        if (a == null || c == null) return -1.0;
        int inter = 0;
        int union = a.size() + c.size();
        for(String s : a){
            if(c.contains(s)){
                inter++;
                union--;
            }
        }
        if(union == 0) return 0.0; //both empty
        return 1.0 - ((double) inter/union);
        
    }
    /**
     * Open Ended distance normalized (levenshtein)
     * @param str1
     * @param str2
     * @return double distance normalized between 0 and 1, -1 if any is null
     */
    static double distanceOpen(String str1, String str2){
        if(str1 == null || str2 == null) return -1.0;
        int steps = levenshteinDynamic(str1, str2);
        double aux = Math.abs(str1.length()-str2.length());
        double max = Math.max(str1.length(),str2.length());
        if(max == aux) return 0.0; //both empty
        return  (steps - aux ) / ( max - aux);
    }
    /**
     * Levenshtein distance dynamic programming
     * cost O(n*m)
     * memory cost O(n*m)
     * @param str1 string 1
     * @param str2 string 2
     * @return int min steps to convert str1 into str2
     */
    static int levenshteinDynamic(String str1, String str2){
        int m = str1.length();
        int n = str2.length();
        // Initializing two arrays to store the current and previous row values
        int[] prevRow = new int[n + 1];
        int[] currRow = new int[n + 1];

        // Initializing the first row with increasing integers
        for (int j = 0; j <= n; j++) prevRow[j] = j;

        for (int i = 1; i <= m; i++) {
            // Initializing the first element of the current row with the row number
            currRow[0] = i;
            // Looping through each character of str2
            for (int j = 1; j <= n; j++) {
                // If characters are equal, no operation needed, take the diagonal value
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    currRow[j] = prevRow[j - 1];
                } else {
                    // If characters are not equal, find the minimum value of insert, delete, or replace
                    currRow[j] = 1 + Math.min(currRow[j - 1], Math.min(prevRow[j], prevRow[j - 1]));
                }
            }
            // Update prevRow with currRow values
            prevRow = Arrays.copyOf(currRow, currRow.length);
        }
        // Final distance is in bottom right corner
        return currRow[n];
    }
    //------------------------------------------------------------------------------------
    // means

    /**
     * Generates the numeric mean of a cluster of answers for a given question index
     * @param clusterAnswers all answers in the cluster
     * @param qIdx  question index to create the mean for
     * @return QuestionAnswer storing the numeric mean
     */
    static QuestionAnswer numericMean(ArrayList<Answer> clusterAnswers,int qIdx){
        int total = 0;
        int answeredCount = 0;
        for(Answer a : clusterAnswers){
            if(!a.getQuestionAnswer(qIdx).isUnAnswered()){
                total += a.getQuestionAnswer(qIdx).getAnswerInteger();
                answeredCount++;
            }
        }
        if (answeredCount == 0) {
            return new QuestionAnswer((Integer) null);
        }
        total /= answeredCount;        
        QuestionAnswer qa = new QuestionAnswer(total);
        return qa;
    }
    /**
     * Generates the multiple choice quantitative mean of a cluster of answers for a given question index
     * @param clusterAnswers all answers in the cluster
     * @param qIdx question index to create the mean for
     * @param questionChoices list of possible choices for the question
     * @return  QuestionAnswer storing the multiple choice quantitative mean
     */
    static QuestionAnswer MCquantitativeMean(ArrayList<Answer> clusterAnswers,int qIdx,ArrayList<String> questionChoices){
        int total = 0;
        int answeredCount = 0;
        for(Answer a : clusterAnswers){
            if(!a.getQuestionAnswer(qIdx).isUnAnswered()){
                String s = a.getQuestionAnswer(qIdx).getAnswerMultiple().get(0);
                total += questionChoices.indexOf(s);
                answeredCount++;
            }
        }
        ArrayList<String> answers = null;
        if(answeredCount > 0){
            int index = (int) Math.round((double)total/answeredCount);
            String s = questionChoices.get(index);
            answers = new ArrayList<String>(){{add(s);}};
        }
        QuestionAnswer qa = new QuestionAnswer(Question.QuestionType.MULTIPLE_CHOICE, answers );
        return qa;
    }
    /**
     * Generates the multiple choice qualitative mean  of a cluster of answers for a given question index
     * @param clusterAnswers all answers in the cluster
     * @param qIdx question index to create the mean for
     * @param questionChoices list of possible choices for the question
     * @return QuestionAnswer storing the multiple choice qualitative mean (multiple choice)
     */
    static QuestionAnswer MCQualitativeMMean(ArrayList<Answer> clusterAnswers,int qIdx,ArrayList<String> questionChoices){ //segurar que mante ordre
        int[] freq = new int[questionChoices.size()];
        int maxFreq = 0;
        ArrayList<String> centroidChoices = new ArrayList<>();
        for (Answer a : clusterAnswers) {
            if(!a.getQuestionAnswer(qIdx).isUnAnswered()){
                for (String s : a.getQuestionAnswer(qIdx).getAnswerMultiple()) {
                    int idx = questionChoices.indexOf(s);
                    freq[idx]++;
                    if (freq[idx] > maxFreq) {
                        maxFreq = freq[idx];
                        centroidChoices.clear();
                        centroidChoices.add(questionChoices.get(idx));
                    } else if (freq[idx] == maxFreq) {
                        if (!centroidChoices.contains(questionChoices.get(idx))) {
                            centroidChoices.add(questionChoices.get(idx));
                        }
                    }
                }
            }  
        }
        if(centroidChoices.isEmpty()) centroidChoices = null;
        else centroidChoices.sort(null);
        QuestionAnswer qa = new QuestionAnswer(Question.QuestionType.MULTIPLE_CHOICE, centroidChoices);
        return qa;
    }
    /**
     * Generates the open ended mean of a cluster of answers for a given question index
     * @param clusterAnswers all answers in the cluster
     * @param qIdx question index to create the mean for
     * @return QuestionAnswer storing the open ended mean
     */
    static QuestionAnswer openEndedMean(ArrayList<Answer> clusterAnswers,int qIdx){
        HashMap<String,Integer> freqWords = new HashMap<>();
        for(Answer a : clusterAnswers){
            if(a.getQuestionAnswer(qIdx).isUnAnswered()) continue;

            String ans = a.getQuestionAnswer(qIdx).getAnswerString();
            String[] words = ans.split(" ");
            for(String s : words){
                freqWords.put(s, freqWords.getOrDefault(s,0)+1);
            }
        }
        int max = -1;
        String out = null;
        for(String s : freqWords.keySet()){
            int count = freqWords.get(s);
            if( count > max){
                max = count;
                out = s;
            }
        }
        QuestionAnswer qa = new QuestionAnswer(Question.QuestionType.OPEN_ENDED, out);
        return qa;
    }
    //----------------------------------------------------------------------------------
    //miscelaneous

    /**
     * Given the within cluster sum of squares 
     * returns the K that creates the elbow.
     * @param computedWCSS double[] storing the computed WCSS
     * @return int K where the elbow is found
     */
    static int detectElbow(double[] computedWCSS){
        int nK = computedWCSS.length;

        if(nK == 1) return 1; //if nk is less than 3 no elbow can be found
        if(nK == 2) return 2; //doesent matter 1 or 2

        double x1 = 1;
        double y1 = computedWCSS[0];

        double x2 = nK;
        double y2 = computedWCSS[nK-1];

        int elbowIndex = -1;
        double maxDistance = -1.0;

        for (int i = 1; i < nK; i++) {
            double x0 = i;
            double y0 = computedWCSS[i-1];
            // Perpendicular distance from point (x0,y0) to the line (x1,y1)-(x2,y2)
            double numerator = Math.abs(
                    (y2 - y1) * x0
                    - (x2 - x1) * y0
                    + x2 * y1
                    - y2 * x1
            );
            double denominator = Math.sqrt(
                    Math.pow(y2 - y1, 2) +
                    Math.pow(x2 - x1, 2)
            );
            double distance = numerator / denominator;
            if (distance > maxDistance) {
                maxDistance = distance;
                elbowIndex = i;
            }
        }
        return elbowIndex;
    }  

    /**
     * Parses the answer to filter relevant words from Open Ended questions
     * @param a The answer to clean
     * @return The cleaned answer
     */
    static Answer cleanData(Answer a){
        Answer cleanA = new Answer(a.getResponderUUID(),a.getFormUFID());
        for(QuestionAnswer qa : a.getAnswer()){
            switch (qa.getQuestionType()) {
                case OPEN_ENDED:
                    String cleanString = OpenNLPME.getInstance().getMeaningfulWords(qa.getAnswerString());
                    QuestionAnswer cleanQa = new QuestionAnswer(cleanString);
                    cleanA.addAnswer(cleanQa);
                    break;
                default:
                    cleanA.addAnswer(qa);
                    break;
            }
        }
        return cleanA;
    }
}