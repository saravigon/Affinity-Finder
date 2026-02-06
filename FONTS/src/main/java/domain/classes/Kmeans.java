package domain.classes;
import java.util.ArrayList;


/** Class to encapsulate the result of a kmeans execution, stores the clusters and the centroids
*/
final class KmeansRes{
    /**
     * Atribute: Stores the clusters
     */
    public ArrayList<ArrayList<Answer>> clusters;
    /**
     * Atribute: Stores the centroids
     */
    public ArrayList<Answer> centroids;
    /**
     * Simple Creator for KmeansRes
     * @param clusters The Array of Arrays of answers that represent the cluster
     * @param centroids The Array of Answers that represent the centroids
     */    
    public KmeansRes( ArrayList<ArrayList<Answer>> clusters,ArrayList<Answer> centroids ){
        this.clusters = clusters;
        this.centroids = centroids;
    }
}
/**
 * Kmeans class implements the K-means clustering algorithm for grouping form answers  
 */
public class Kmeans{
    /**
     * Attributes
     * form: The form containing answers to be clustered
     * matDades: List of answers from the form
     * questions: List of questions in the form
     * nQuestions: Number of questions in the form
     * K: Number of clusters
     * maxIterations: Maximum number of iterations for the algorithm
     * precision: Convergence precision threshold
     * finalClusters: Final clusters of answers after clustering
     */
    private ArrayList<Answer> matDades;
    private ArrayList<Question> questions;
    private int nQuestions,K;
    final private int maxIterations = 500;
    final private double precision = 0.0001;
    private KmeansRes lastClustering ;
    
    //setter
    /**
     * Sets the form and K value for the K-means algorithm
     * Initializes necessary parameters
     * @param f The form to be clustered
     */
    public  Kmeans(Form f){
        if(f == null) throw new IllegalArgumentException("Form is null");
        updateData(f);
        this.K = 3;//default      
    }
    /**
     * Sets K value
     * @param K The K
     * @throws IllegalArgumentException when K is not positive
     */
    public void setK(int K){
        if(K <= 0) throw new IllegalArgumentException("K must be greater than 0: "+K);
        this.K = K;
    }

    /**
     * Executes the clustering algorithm with elbow method
     * @return The affinity group list of all the users that have responded with optimal K
     * @throws Exception
     */
    public ArrayList<AffinityGroup> createClustersElbowMethod(Form f,ArrayList<Answer> ans) throws Exception{
        updateData(f);
        setAnswers(ans);
        this.lastClustering = assignClusters(elbowMethod());
        return generateAffinityGroups(this.lastClustering);
    }

    /**
     * Executes the clustering algorithm, expects the admin to set the K beforehand
     * @return The affinity group list of all the users that have responded
     * @throws Exception
     */
    public ArrayList<AffinityGroup> createClustersSetK(Form f,ArrayList<Answer> ans) throws Exception{
        updateData(f);
        setAnswers(ans);
        this.lastClustering = kMeansExec();
        return generateAffinityGroups(this.lastClustering);
    }



    /**
     * Sets the answers to the actual answer list of the form
     * @param ans ArrayList of answers
     */
    private void setAnswers(ArrayList<Answer> ans){
        if(ans.size() == 0) throw new IllegalArgumentException("Kmeans cannot be executed with 0 answers");
        matDades = new ArrayList<>();
        for(Answer a : ans){
            matDades.add(KmeansHelper.cleanData(a));
        }
    }

    /**
     *  Elbow method to cluster data with best K
     * @return returns the centroids that permit the generation of the best clustering acording to elbow method
     * @throws Exception
     */
    private ArrayList<Answer> elbowMethod() throws Exception{
        if(matDades == null ) throw new IllegalStateException("answers not initialized properly ");
        int maxK = (int) Math.round(Math.sqrt(matDades.size()));
        ArrayList<ArrayList<Answer>> savedFinalCentroids = new  ArrayList<>(); //Save final clusters to save space then run kmeans with this centroids
        double[] computedWCSS = new double[maxK];
        for(int i = 1; i<=maxK; i++){
            setK(i);
            KmeansRes kr =  kMeansExec();
            lastClustering = kr;
            savedFinalCentroids.add(kr.centroids);
            computedWCSS[i-1] = evaluateClusteringWCSS();
        }
        int bestK = KmeansHelper.detectElbow(computedWCSS);
        return savedFinalCentroids.get(bestK - 1);  // minus 1 because K=1 is in index 0
    }
    
    /**
     * Given a form updates the data necesary for executing clustering algorithm
     * @param form The form were the clustering is ran
     */
    private void updateData(Form form){
        nQuestions = form.getQuestionsCount();
        questions = form.getQuestions();
    }
    //inicialitzacions
    // random

    /**
     * Basic initialitzation, return a list with K indexes from the form responders, pseudo random centroid
     * @return arrayList of int of K centorids at random,
     */
    @SuppressWarnings("unused")
    private ArrayList<Answer> randomIni(){
        ArrayList<Answer> l = new ArrayList<>();
        for(int i = 0; i < K; i++){
            Answer c = new Answer(i,i);
            for(int j = 0; j < nQuestions; j++){
                int randIdx = (int)(Math.random() * (matDades.size()));
                c.addAnswer(matDades.get(randIdx).getQuestionAnswer(j));
            }
            l.add(c);
        }
        return l;
    }   


    /**
     * Kmeans++ initialization, return a list with K indexes from the form responders, attempts to maximize distances between centroids
     * @return ArrayList of k centroids generated by kmeans++
     */

    private ArrayList<Answer> kmeansPlus(){
        //pick first point randomly
        ArrayList<Answer> centroids = new ArrayList<>();
        int randIdx = (int)(Math.random() * (matDades.size()));// random never is 1.0 [0.0,1.0)]
        centroids.add(matDades.get(randIdx));

        double[] dist = new double[matDades.size()];

        while(centroids.size() < K){
            //calcular distancies
            int i = 0;
            for(Answer a : matDades){
                double minDist = Double.MAX_VALUE;

                for(Answer c : centroids){
                    double d = distanceTotal(a, c);
                    if(d < minDist) minDist = d;
                }
                dist[i] = minDist*minDist;
                i++;
            }

            //seleccio aleatoria proporcional a D(x)^2

            double total = 0;
            for(double d : dist) total+=d;

            double threshold = Math.random() * total;

            double sum = 0;
            for(i = 0; i < matDades.size(); i++){
                sum+= dist[i];
                if(sum >= threshold) break;
            }
            centroids.add(matDades.get(i));
        }
        return centroids;
    }

    /**
     * Calculates the total distance between an answer and a centroid across all questions
     * @param a 
     * @param c
     * @return double average distance normalized between 0 and 1
     */
    private double distanceTotal(Answer a, Answer c){
        double total = 0.0;
        int questionCounted = 0;
        double dist;
        for(int i = 0; i < nQuestions; i++){
            switch (a.getQuestionAnswer(i).getQuestionType()) {
                case NUMERIC:
                    int range = questions.get(i).getRange();
                    if (range == 0) {
                        throw new IllegalStateException(
                            "Numeric question with range 0 at index " + i
                        );
                    }
                    dist = KmeansHelper.distanceNumeric( a.getQuestionAnswer(i).getAnswerInteger(), c.getQuestionAnswer(i).getAnswerInteger(), range);
                    break;
                case MULTIPLE_CHOICE:
                    if(questions.get(i).isOrder()) dist = KmeansHelper.distanceMultipleOrder( a.getQuestionAnswer(i).getAnswerMultiple(), 
                                                                                              c.getQuestionAnswer(i).getAnswerMultiple(),
                                                                                              questions.get(i).getChoices());
                    else dist = KmeansHelper.distanceMultipleUnOrder( a.getQuestionAnswer(i).getAnswerMultiple(), c.getQuestionAnswer(i).getAnswerMultiple());
                    break;
                case OPEN_ENDED:
                    dist = KmeansHelper.distanceOpen(a.getQuestionAnswer(i).getAnswerString(), c.getQuestionAnswer(i).getAnswerString());
                    break;
                default:
                    dist = -1.0;
                    break;
            }
            if(dist >= 0.0) {
                total += dist;
                questionCounted++;
            }
        }
        if (questionCounted == 0) return 0.5; // If no questions were counted, return a neutral distance
        return total/questionCounted;
    }

    /**
     * Generates a new centroid by calculating the mean of all answers in a cluster
     * @param clusterAnswers
     * @param centroid
     * @return Answer representing the new centroid
     */
    private Answer generateCentroidMean(ArrayList<Answer> clusterAnswers,Answer centroid){
        if(clusterAnswers.isEmpty()) return centroid;
        int nAnswers = clusterAnswers.size();
        ArrayList<QuestionAnswer> centroidAnswers = new ArrayList<>(nAnswers);
        int qIdx = 0;
        QuestionAnswer qa;
        for(Question q : questions){
            switch (q.getQuestionType()) {
                case NUMERIC: 
                    qa = KmeansHelper.numericMean(clusterAnswers, qIdx);
                    break;
                case MULTIPLE_CHOICE:
                    if(questions.get(qIdx).isOrder()){
                        qa = KmeansHelper.MCquantitativeMean(clusterAnswers,qIdx,q.getChoices());
                    }
                    else{
                        qa = KmeansHelper.MCQualitativeMMean(clusterAnswers, qIdx, q.getChoices());
                    }
                    break;
                case OPEN_ENDED:
                    qa = KmeansHelper.openEndedMean(clusterAnswers, qIdx);
                    break;
                default:
                    qa = new QuestionAnswer(q.getQuestionType(),null);
                    break;
            }
            qIdx++;
            centroidAnswers.add(qa);
        }
        return new Answer(0,0,centroidAnswers);
    }
    /**
     * Gets the cleaned answers from last execution,
     * @return
     */
    public ArrayList<Answer> getCleanAnswers(ArrayList<Answer> answers){
        if(matDades == null){
            setAnswers(answers);
        }
        return matDades;
    }
    /**
     * Returns the answer that is the most far from the centroid, so the point that is worst asigned
     * @param ans
     * @param centroid
     * @return Answer The answer with most distance to it's centroid
     */
    @SuppressWarnings("unused")
    private Answer worstPoint(ArrayList<Answer> ans, Answer centroid){
        Answer result = null;
        double maxDistance = 0.0;
        for(Answer a : ans){
            double dist = distanceTotal(a, centroid);
            if(dist > maxDistance){
                maxDistance = dist;
                result = a;
            }
        }
        return result;
    }

    /**
     * Returns the answer that is the most close from the centroid
     * @param ans List of answers
     * @param centroid Centroid
     * @return Answer The answer with minimum distance to it's centroid
     */
    private Answer bestPoint(ArrayList<Answer> ans, Answer centroid){
        Answer result = null;
        double minDistance = Double.MAX_VALUE;
        for(Answer a : ans){
            double dist = distanceTotal(a, centroid);
            if(dist < minDistance){
                minDistance = dist;
                result = a;
            }
        }
        return result;
    }
    /**
     * Executs the K-means clustering algorithm on the form's answers
     * @return KmeansRes of the execution
     */
    private KmeansRes kMeansExec() throws Exception{
        ArrayList<Answer> centroids = kmeansPlus();
        ArrayList<ArrayList<Answer>> clusters = new ArrayList<>(K);
        boolean changed = true;
        int currentIteration = 0;
        while (changed && currentIteration < maxIterations){
            //buida clusters
            clusters = new ArrayList<>(K);
            for (int i = 0; i < K; i++) {
                clusters.add(new ArrayList<>());
            }
            //asigna cada answer al cluster al centroide mes proper
            for(Answer a : matDades){     
                int k = 0;
                double minDist = Double.POSITIVE_INFINITY;
                int closestCluster = 0;
                for(Answer c : centroids){
                    double d = distanceTotal(a, c);
                    if(d < minDist){
                        minDist = d;
                        closestCluster = k;
                    }
                    k++;
                }
                clusters.get(closestCluster).add(a);
            }
            //crear seguents centroides
            changed = false;
            ArrayList<Answer> newCentroids = new ArrayList<>(K);
            for(int k = 0; k<K; k++) {
                Answer newCentroid;

                if(clusters.get(k).isEmpty()){
                    int randIndex = (int) (Math.random() * matDades.size());
                    newCentroid = matDades.get(randIndex);
                }
                else{
                    newCentroid = generateCentroidMean(clusters.get(k),centroids.get(k));
                }
                if(distanceTotal(newCentroid, centroids.get(k)) > precision) changed = true;
                
                newCentroids.add(newCentroid);
              
            }
            centroids = newCentroids;
            currentIteration++;
        }
        return new KmeansRes(clusters, centroids);
    }
    /**
     * Generates the affinity groups for the answers starting from Centroids, with one iteration.
     * @param centroids The centroids to initialize clustering
     * @return Returns KmeansRes of the execution
     * @throws Exception
     */
    private  KmeansRes assignClusters(ArrayList<Answer> centroids) throws Exception{
        setK(centroids.size());
        ArrayList<ArrayList<Answer>> clusters = new ArrayList<>(K);
        int k;
        for (k = 0; k < K; k++) {
            clusters.add(new ArrayList<Answer>());
        }
        //asigna cada answer al cluster del centroide mes proper
        for(Answer a : matDades){     
            k = 0;
            double minDist = Double.POSITIVE_INFINITY;
            int closestCluster = 0;

           
            for(Answer c : centroids){
                double d = distanceTotal(a, c);
                if(d < minDist){
                    minDist = d;
                    closestCluster = k;
                }
                k++;
            }
            clusters.get(closestCluster).add(a);
        }
        return new KmeansRes(clusters, centroids);
    }
    /**
     * From a KmeansRes create the affinity groups to store, sets the representative as the point closest to centroid,
     * For each answer stores its responderUUID
     * @param kr
     * @return List of affinityGroup
     */
    private ArrayList<AffinityGroup> generateAffinityGroups(KmeansRes kr){
        ArrayList<AffinityGroup> ret = new ArrayList<>();
        for(int k = 0; k < kr.centroids.size(); k++){
            Answer rep = bestPoint(kr.clusters.get(k), kr.centroids.get(k));
            if(rep == null) continue; //skip empty clusters
            AffinityGroup af = new AffinityGroup(rep.getResponderUUID(), null);
            for(Answer a : kr.clusters.get(k)){
                af.addMember(a.getResponderUUID());
            }
            ret.add(af);
        }
        return ret;
    }

    /**
     * The Silhouette Score measures how well each data point fits within its assigned cluster compared to other clusters.
     * Greater is better
     * @return double in the range [-1.0,1.0]
     */
    public double evaluateClusteringSilhouete(){
        double totalEval = 0.0;
        //calculate evaluation metric
        for(ArrayList<Answer> clusterAnswers : lastClustering.clusters){
            if (clusterAnswers.size() <= 1) continue; //coeficient is 0 if one or zero points in cluster
            // optimitzar (precalcular distancies)
            for(Answer a : clusterAnswers){
                double ai = 0.0; //mean intra-cluster distance
                for(Answer b : clusterAnswers){
                    if(a != b) ai += distanceTotal(a,b);
                }
                ai /= (clusterAnswers.size() - 1);
                double bi = Double.POSITIVE_INFINITY; //minimum mean between i an any other cluster
                for(ArrayList<Answer> otherClusterAnswers : lastClustering.clusters){
                    if(otherClusterAnswers != clusterAnswers){
                        if(otherClusterAnswers.size() <= 1) continue; //skip empty clusters (or only one)
                        double meanDist = 0.0;
                        for(Answer b : otherClusterAnswers) meanDist += distanceTotal(a,b);
                        meanDist /= otherClusterAnswers.size();
                        if(meanDist < bi) bi = meanDist;
                    }
                }
                totalEval += (bi - ai) / Math.max(ai, bi);
            }
        }
        totalEval /= matDades.size();
        return totalEval;           
    }
    
    
    /**
     * Returns the Within-Cluster Sum of Squares which shows how close the data points are to their cluster centroid
     * Lower is better
     * @return
     */
    public double evaluateClusteringWCSS(){
        int i = 0;
        double total = 0;
        for(ArrayList<Answer> clusterAnswers : lastClustering.clusters){
            Answer c = lastClustering.centroids.get(i);
            for(Answer a : clusterAnswers){
                double d = distanceTotal(a, c);
                total += d * d;
            }
            i++;
        }
        return total;
    }    
}