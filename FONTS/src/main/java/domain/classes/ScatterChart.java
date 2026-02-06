package domain.classes;
import java.util.ArrayList;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;


/**
 * Class ScatterChart
 * Responsible for generating a 3D scatter plot based on PCA components of the answers
 */
public class ScatterChart {
    /**
     * Generates a 3D scatter plot based on PCA components of the answers
     * @param questions list of questions
     * @param answers list of answers
     * @param af list of affinity groups
     */
    public static void scatterChart(
        ArrayList<Question> questions,
        ArrayList<Answer> answers,
        ArrayList<AffinityGroup> af) {

        PCAtranformation pca = new PCAtranformation(questions);
        PCAResult result = pca.pcaApache(answers, 3);
        double[][] pcs = result.projectedData;
        double[] singularValues = result.eigenValues;
        
        int[] clusterSizes = new int[af.size()];
        for(int i = 0; i<af.size(); i++) {
            clusterSizes[i] = af.get(i).getMemberIDs().size();
        }

        //compute explained variance
        double totalVariance = 0.0;
        for (double s : singularValues)
            totalVariance += s * s;

        double[] explained = new double[singularValues.length];
        for (int i = 0; i < singularValues.length; i++)
            explained[i] = (singularValues[i] * singularValues[i]) / totalVariance;

        if (pcs == null || pcs.length == 0) {
            throw new IllegalStateException("PCA returned no data");
        }

        int nPoints = pcs.length;
        int dims = pcs[0].length;

        if (dims < 2) {
            throw new IllegalArgumentException("PCA must return at least 2 components");
        }

        Coord3d[] points = buildPoints(pcs, dims);
        Color[] colors = assignColors(answers, af, nPoints);

        Scatter scatter = new Scatter(points, colors);
        scatter.setWidth(6);//point size

        Chart chart = AWTChartComponentFactory.chart(Quality.Advanced, "awt");
        configureAxes(chart, dims);
        
        chart.getScene().add(scatter);
        chart.addMouseCameraController();
        javax.swing.JFrame frame = new javax.swing.JFrame("PCA Scatter Plot");
        frame.setLayout(new java.awt.BorderLayout());

        // chart canvas
        frame.add((java.awt.Component) chart.getCanvas(), java.awt.BorderLayout.CENTER);

        // ===== SIDE INFO PANEL =====
        javax.swing.JPanel info = new javax.swing.JPanel();
        info.setPreferredSize(new java.awt.Dimension(260, 600));
        info.setLayout(new javax.swing.BoxLayout(info, javax.swing.BoxLayout.Y_AXIS));

        info.add(new javax.swing.JLabel("PCA Summary"));
        info.add(javax.swing.Box.createVerticalStrut(10));

        
        info.add(javax.swing.Box.createVerticalStrut(10));

        double cumulative = 0.0;
        for (int i = 0; i < Math.min(3, explained.length); i++) {
            cumulative += explained[i];
            info.add(new javax.swing.JLabel(
                String.format("PC%d: %.2f%%", i + 1, explained[i] * 100)
            ));
        }

        info.add(new javax.swing.JLabel(
            String.format("Cumulative: %.2f%%", cumulative * 100)
        ));

        info.add(javax.swing.Box.createVerticalStrut(20));
        info.add(new javax.swing.JLabel("Clusters"));
        info.add(javax.swing.Box.createVerticalStrut(10));

        info.add(new javax.swing.JLabel("Number of clusters: " + af.size()));

        for (int i = 0; i < clusterSizes.length; i++) {
            info.add(new javax.swing.JLabel(
                "Cluster " + (i + 1) + ": " + clusterSizes[i] + " points"
            ));
        }

        frame.add(info, java.awt.BorderLayout.EAST);

        // final frame settings
        frame.setSize(1000, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Builds the 3D coordinates for the scatter plot points
     * @param pcs PCA components
     * @param dims number of dimensions
     * @return array of 3D coordinates
     */
    private static Coord3d[] buildPoints(double[][] pcs, int dims) {
        int n = pcs.length;
        Coord3d[] points = new Coord3d[n];

        boolean is3D = dims >= 3;

        for (int i = 0; i < n; i++) {
            double x = pcs[i][0];
            double y = pcs[i][1];
            double z = is3D ? pcs[i][2] : 0.0;

            points[i] = new Coord3d(x, y, z);
        }
        return points;
    }
    
    /**
     * Generates the colors for the chart so thay are diferent enough for visualitzation
     * @param k The number of colors needed
     * @return 
     */
    public static Color[] generateClusterColors(int k) {
        Color[] colors = new Color[k];
        float saturation = 0.8f;
        float value = 0.9f;

        for (int i = 0; i < k; i++) {
            float hue = (float) i / k;
            colors[i] = hsv2Rgb(hue, saturation, value);
        }
        return colors;
    }
    /**
     * Assigns colors to each point based on its affinity group
     * @param answers list of answers
     * @param af list of affinity groups
     * @param nPoints number of points
     * @return array of colors for each point
     */
    private static Color[] assignColors(ArrayList<Answer> answers,ArrayList<AffinityGroup> af,int nPoints) {

        Color[] clusterColors = generateClusterColors(af.size());
        Color[] pointColors = new Color[nPoints];

        for (int i = 0; i < nPoints; i++) {
            Integer uuid = answers.get(i).getResponderUUID();
            pointColors[i] = Color.GRAY; // default

            for (int k = 0; k < af.size(); k++) {
                if (af.get(k).isMember(uuid)) {
                    pointColors[i] = clusterColors[k];
                    break;
                }
            }
        }
        return pointColors;
    }
    /**
     * Configures the axes labels based on the number of dimensions
     * @param chart the chart to configure
     * @param dims number of dimensions
     */
    private static void configureAxes(Chart chart, int dims) {
        chart.getAxeLayout().setXAxeLabel("PC1");
        chart.getAxeLayout().setYAxeLabel("PC2");
    
        if (dims >= 3) {
            chart.getAxeLayout().setZAxeLabel("PC3");
        } else {
            chart.getAxeLayout().setZAxeLabel(""); // 2D 
        }
    }

    

    /**
     * Given a color in HSV transforms it to RGB
     * @param h
     * @param s
     * @param v
     * @return Color in rgb
     */
    // HSV in [0,1], returns RGB in [0,1]
    private static Color hsv2Rgb(float h, float s, float v) {
        float r, g, b;

        int i = (int) Math.floor(h * 6);
        float f = h * 6 - i;
        float p = v * (1 - s);
        float q = v * (1 - f * s);
        float t = v * (1 - (1 - f) * s);

        switch (i % 6) {
            case 0: r = v; g = t; b = p; break;
            case 1: r = q; g = v; b = p; break;
            case 2: r = p; g = v; b = t; break;
            case 3: r = p; g = q; b = v; break;
            case 4: r = t; g = p; b = v; break;
            case 5: r = v; g = p; b = q; break;
            default: r = g = b = 0; //per treure warning, mai pasa
        }

        return new Color(r, g, b);
    }
}
