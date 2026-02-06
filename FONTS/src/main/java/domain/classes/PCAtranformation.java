package domain.classes;

import java.util.ArrayList;


import org.apache.commons.math3.linear.*;
/**
 * Class to hold the PCA result
 */
class PCAResult {
    /**
     * Projected data matrix after PCA transformation
     */
    public final double[][] projectedData;   
    /**
     * Eigenvalues corresponding to the principal components
     */
    public final double[] eigenValues;      
    /**
     * Eigenvectors corresponding to the principal components  
    */  
    public final double[][] eigenVectors; 

    /**
     * Constructor for PCAResult
     * @param projectedData
     * @param eigenValues
     * @param eigenVectors
     */
    public PCAResult(double[][] projectedData, double[] eigenValues, double[][] eigenVectors) {
        this.projectedData = projectedData;
        this.eigenValues = eigenValues;
        this.eigenVectors = eigenVectors;
    }
}

/**
 * Class for performing PCA transformation on survey data
 */
public class PCAtranformation{
    /**
     * List of questions in the form
     */
    private ArrayList<Question> questions;

    /**
     * Constructor for PCAtranformation
     * @param questions List of questions in the form
     */
    public PCAtranformation(ArrayList<Question> questions){
        this.questions = questions;
    }

    /**
     * Performs PCA transformation using Apache Commons Math library, only for testing
     * @param ans List of answers
     * @param k Number of principal components to retain
     * @return Transformed data matrix with reduced dimensions
     */
    public PCAResult pcaApache(ArrayList<Answer> ans, int k) {
        // Center data
        double[][] data = FormDataAsMatrix(ans);
        //data = standardize(data);
        RealMatrix X = MatrixUtils.createRealMatrix(data);
        RealVector mean = meanVector(X);
        for (int i = 0; i < X.getRowDimension(); i++) {
            X.setRowVector(i, X.getRowVector(i).subtract(mean));
        }

        // SVD
        SingularValueDecomposition svd = new SingularValueDecomposition(X);
        double[] s = svd.getSingularValues();
        //System.out.println("Singular values:");
        //for (double v : s) {
        //    System.out.println(v);
        //}
        // Top-k right singular vectors
        RealMatrix V = svd.getV();
        RealMatrix Vk = V.getSubMatrix(0, V.getRowDimension() - 1, 0, k - 1);

        // Project data
        RealMatrix projected = X.multiply(Vk);
        double[] eigenValues = new double[k];
        for (int i = 0; i < k; i++) {
            eigenValues[i] = (s[i] * s[i]) / (X.getRowDimension() - 1);
        }

        return new PCAResult(projected.getData(), eigenValues, Vk.getData());
    }
    /**
     * Computes the mean vector of the data matrix, for apache commons math
     * @param X Data matrix
     * @return  Mean vector
     */
    private static RealVector meanVector(RealMatrix X) {
        int cols = X.getColumnDimension();
        double[] mean = new double[cols];

        for (int j = 0; j < cols; j++) {
            mean[j] = X.getColumnVector(j).getL1Norm() / X.getRowDimension();
        }
        return new ArrayRealVector(mean);
    }

    //////////////////////////////////
    /// Our own PCA implementation using SVD
    //////////////////////////////////
    
    
    /**
     * Performs PCA transformation using SVD on the original data matrix, uses our own implementation
     * @param originalMatrix Original data matrix as a list of answers
     * @param k Number of principal components to retain
     * @return Transformed data matrix with reduced dimensions
     */
    public PCAResult transformSVD(ArrayList<Answer> originalMatrix, int k) {
        // 1. Data + centering
        double[][] X = FormDataAsMatrix(originalMatrix);
        X = standardize(X);
    
        int nSamples = X.length;
        int nFeatures = X[0].length;
        k = Math.min(k, Math.min(nSamples, nFeatures));
    
        // 2. Random projection
        double[][] Omega = randomMat(nFeatures, k);
        double[][] Y = multMat(X, Omega); // nSamples × k
    
        // 3. Orthonormalize Y → Q
        double[][] Q = gramSchmidt(Y); // nSamples × k
    
        // 4. B = Qᵀ X
        double[][] Qt = transposeMatrix(Q);
        double[][] B = multMat(Qt, X); // k × nFeatures
    
        // 5. Compute covariance of B
        double[][] C = multMat(B, transposeMatrix(B)); // k × k
    
        // 6. Eigenvectors of C (power iteration)
        double[][] eigenVecs = topKEigenvectors(C, k);
    
        // 7. V = Bᵀ * eigenVecs
        double[][] V = multMat(transposeMatrix(B), eigenVecs); // nFeatures × k
        normalizeColumns(V);
    
        // 8. Project data
        
        return new PCAResult(multMat(X, V), null, V);
    }

    
    /**
     * Orthonormalizes the columns of matrix A using the Gram-Schmidt process
     * @param A Input matrix
     * @return Matrix with orthonormal columns
     */
    private static double[][] gramSchmidt(double[][] A){
        int rows = A.length;
        int cols = A[0].length;
        double[][] Q = new double[rows][cols];
    
        for(int j = 0; j < cols; j++){
            for(int i = 0; i < rows; i++){
                Q[i][j] = A[i][j];
            }
    
            for(int k = 0; k < j; k++){
                double dot = 0;
                for(int i = 0; i < rows; i++)
                    dot += Q[i][j] * Q[i][k];
    
                for(int i = 0; i < rows; i++)
                    Q[i][j] -= dot * Q[i][k];
            }
    
            double norm = 0;
            for(int i = 0; i < rows; i++)
                norm += Q[i][j] * Q[i][j];
            norm = Math.sqrt(norm);
    
            if(norm > 1e-10)
                for(int i = 0; i < rows; i++)
                    Q[i][j] /= norm;
        }
        return Q;
    }

    /**
     * Computes the top k eigenvectors of matrix A using the power iteration method
     * @param A Input matrix
     * @param k Number of eigenvectors to compute
     * @return Matrix containing the top k eigenvectors as columns
     */
    private static double[][] topKEigenvectors(double[][] A, int k){
        int n = A.length;
        double[][] eigenVecs = new double[n][k];
        double[][] B = copyMatrix(A);
    
        for(int c = 0; c < k; c++){
            double[] v = randomVector(n);
    
            for(int iter = 0; iter < 100; iter++){
                v = matVec(B, v);
                normalize(v);
            }
    
            for(int i = 0; i < n; i++)
                eigenVecs[i][c] = v[i];
    
            // Deflation
            double lambda = dot(v, matVec(B, v));
            for(int i = 0; i < n; i++)
                for(int j = 0; j < n; j++)
                    B[i][j] -= lambda * v[i] * v[j];
        }
        return eigenVecs;
    }


    /**
     * Converts the list of answers into a numerical data matrix
     * @param original List of answers
     * @return Numerical data matrix
     */
    private double[][] FormDataAsMatrix(ArrayList<Answer> original) {

        int cols = 0;
        for (Question q : questions) {
            switch (q.getQuestionType()) {
                case NUMERIC -> cols++;
                case MULTIPLE_CHOICE -> {
                    if (q.isOrder()) cols++;
                    else cols += q.getChoices().size();
                }
                case OPEN_ENDED -> {}
            }
        }
    
        double[][] mat = new double[original.size()][cols];
    
        int row = 0;
        for (Answer a : original) {
            int col = 0;
    
            for (int qIdx = 0; qIdx < questions.size(); qIdx++) {
                Question q = questions.get(qIdx);
                QuestionAnswer qa = a.getAnswer().get(qIdx);
    
                switch (q.getQuestionType()) {
    
                    case NUMERIC -> {
                        double min = q.getMinBound();
                        double max = q.getMaxBound();
    
                        if (qa.isUnAnswered()) {
                            mat[row][col] = 0.0; // neutral, will be standardized later
                        } else {
                            mat[row][col] = (qa.getAnswerInteger() - min) / (max - min);
                        }
                        col++;
                    }
    
                    case MULTIPLE_CHOICE -> {
                        ArrayList<String> choices = q.getChoices();
    
                        if (q.isOrder()) {
                            int idx = choices.indexOf(qa.getAnswerString());
                            mat[row][col] = (double) idx / (choices.size() - 1);
                            col++;
                        } else {
                            for (String choice : choices) {
                                mat[row][col++] = qa.getAnswerMultiple().contains(choice) ? 1.0 : 0.0;
                            }
                        }
                    }
                    case OPEN_ENDED -> {}
                }
            }
            row++;
        }
        return mat;
    }
    /**
     * Standardizes the data matrix to have zero mean and unit variance for each feature
     * @param X Input data matrix
     * @return  Standardized data matrix
     */
    private static double[][] standardize(double[][] X) {
        int n = X.length;
        int d = X[0].length;
        double[][] out = new double[n][d];
    
        for (int j = 0; j < d; j++) {
            double mean = 0.0;
            double var = 0.0;
    
            // mean
            for (int i = 0; i < n; i++) {
                mean += X[i][j];
            }
            mean /= n;
    
            // variance
            for (int i = 0; i < n; i++) {
                double diff = X[i][j] - mean;
                var += diff * diff;
            }
            var /= (n - 1);
    
            double std = Math.sqrt(var);
    
            // constant column keep zero
            if (std < 1e-12) continue;
    
            for (int i = 0; i < n; i++) {
                out[i][j] = (X[i][j] - mean) / std;
            }
        }
        return out;
    }
    //////////////////////////////////
    //helper matrix functions
    ////////////////////////////////

    /**
     * Multiplies two matrices A and B
     * @param A
     * @param B
     * @return  Resulting matrix after multiplication
     */
    private static double [][] multMat(double [][] A,double [][] B){
        if(A[0].length != B.length){
            throw new IllegalArgumentException("Matrix A and B are not compatible for mutliplication");
        }
        int rows = A.length;
        int cols = B[0].length;
        int common = A[0].length;
        double [][] out = new double[rows][cols];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++ ){
                for(int k = 0; k < common; k++){
                    out[i][j] += A[i][k]*B[k][j];
                }
            }
        }
        return out;
    }

    /**
     * Transposes matrix A
     * @param A
     * @return Transposed matrix
     */
    private static double[][] transposeMatrix(double[][] A){
        int rows = A.length;
        int cols = A[0].length;
        double[][] out = new double[cols][rows];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                out[j][i] = A[i][j];
            }
        }
        return out;
    }

    /**
     * Generates a random matrix with values between 0 and 1
     * @param rows
     * @param cols
     * @return Randomly generated matrix
     */
    private static double [][] randomMat(int rows,int cols){
        double [][] out = new double[rows][cols];
        for(int i = 0; i < rows; i++){
            out[i] = randomVector(cols);
        }
        return out;
    }

    /**
     * Multiplies matrix A with vector v
     * @param A
     * @param v
     * @return Resulting vector after multiplication
     */
    private static double[] matVec(double[][] A, double[] v){
        double[] out = new double[A.length];
        for(int i = 0; i < A.length; i++)
            for(int j = 0; j < v.length; j++)
                out[i] += A[i][j] * v[j];
        return out;
    }
    
    /**
     * Computes the dot product of vectors a and b
     * @param a
     * @param b
     * @return Dot product result
     */
    private static double dot(double[] a, double[] b){
        double s = 0;
        for(int i = 0; i < a.length; i++) s += a[i] * b[i];
        return s;
    }
    
    /**
     * Normalizes vector v to have unit length
     * @param v
     */
    private static void normalize(double[] v){
        double norm = Math.sqrt(dot(v, v));
        if(norm > 1e-10)
            for(int i = 0; i < v.length; i++)
                v[i] /= norm;
    }
    
    /**
     * Normalizes the columns of matrix A to have unit length
     * @param A
     */
    private static void normalizeColumns(double[][] A){
        for(int j = 0; j < A[0].length; j++){
            double norm = 0;
            for(int i = 0; i < A.length; i++)
                norm += A[i][j] * A[i][j];
            norm = Math.sqrt(norm);
            if(norm > 1e-10)
                for(int i = 0; i < A.length; i++)
                    A[i][j] /= norm;
        }
    }
    
    /**
     * Creates a copy of matrix A
     * @param A
     * @return Copied matrix
     */
    private static double[][] copyMatrix(double[][] A){
        double[][] B = new double[A.length][A[0].length];
        for(int i = 0; i < A.length; i++)
            System.arraycopy(A[i], 0, B[i], 0, A[0].length);
        return B;
    }
    
    /**
     * Generates a random vector of size n with values between 0 and 1
     * @param n
     * @return Randomly generated vector
     */
    private static double[] randomVector(int n){
        double[] v = new double[n];
        for(int i = 0; i < n; i++) v[i] = Math.random();
        return v;
    }


}