package languageclassifier;

import perceptron.DiscretePerceptron;
import perceptron.Perceptron;

/** Jednowarstwowa sieć: tablica perceptronów (po jednym na język) */
public class MultiLayerNetwork {

    private final Perceptron[] perceptrons;

    public MultiLayerNetwork(int inputDim, int numLanguages) {
        perceptrons = new Perceptron[numLanguages];
        for (int i=0;i<numLanguages;i++)
            perceptrons[i] = new DiscretePerceptron(inputDim);
    }

    public void trainOnExample(double[] x, int correctIdx) {
        for (int i=0;i<perceptrons.length;i++)
            perceptrons[i].learn(x, i==correctIdx ? 1 : 0);
    }

    public int predict(double[] x) {
        double best = Double.NEGATIVE_INFINITY;
        int    idx  = -1;
        for (int i=0;i<perceptrons.length;i++) {
            double val = perceptrons[i].compute(x);
            if (val > best) { best = val; idx = i; }
        }
        return idx;
    }
}
