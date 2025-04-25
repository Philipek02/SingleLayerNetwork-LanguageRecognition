package perceptron;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public abstract class Perceptron {

    private final double[] weights;
    private final int dimension;
    private double threshold;
    private double alpha = 0.01;
    private double beta = 0.01;

    /**
     * Konstruktor perceptronu.
     *
     * @param dimension liczba atrybutów (długość wektora wejściowego)
     */
    public Perceptron(int dimension) {
        this.dimension = dimension;
        this.weights = new double[dimension];
        Random rand = new Random();
        for (int i = 0; i < dimension; i++) {
            weights[i] = rand.nextDouble();
        }
        threshold = rand.nextDouble();
    }

    /**
     * Funkcja aktywacji.
     * @param net iloczyn skalarny wejść i wag pomniejszony o próg
     * @return wyjście perceptronu
     */
    public abstract double activationFunction(double net);

    /**
     * Pochodna funkcji aktywacji.<br>
     * UWAGA: jest to uproszczenie, nie zawsze zwracamy faktyczną wartość pochodnej z matematycznego punktu widzenia.
     * @param net iloczyn skalarny wejść i wag pomniejszony o próg
     * @return wartość pochodnej funkcji aktywacji
     */
    public abstract double derivativeActivationFunction(double net);

    /**
     * Oblicza wyjście perceptronu dla danego wektora wejściowego.
     *
     * @param inputs tablica wartości wejściowych o długości równej dimension
     * @return wyjście perceptronu
     */
    public double compute(double[] inputs) {
        if (inputs.length != dimension) {
            throw new IllegalArgumentException("Nieprawidłowa liczba atrybutów w wektorze wejściowym");
        }

        double sum = 0;
        for (int i = 0; i < dimension; i++) {
            sum += weights[i] * inputs[i];
        }
        
        double net = sum - threshold;
        return activationFunction(net);
    }

    /**
     * Oblicza wyjście perceptronu dla danej listy wartości wejściowych.
     * @param inputs lista wartości wejściowych
     * @return wyjście perceptronu
     */
    public double compute(List<Double> inputs) {
        return compute(inputs.stream().mapToDouble(Double::doubleValue).toArray());
    }

    /**
     * Uczy perceptron, aktualizując wagi oraz próg.
     *
     * @param inputs   tablica wartości wejściowych
     * @param correctDecision oczekiwana wartość wyjściowa
     */
    public void learn(double[] inputs, int correctDecision) {
        if (inputs.length != dimension) {
            throw new IllegalArgumentException("Nieprawidłowa liczba atrybutów w wektorze wejściowym");
        }

        double output = compute(inputs);
        double error = correctDecision - output;
        double delta = error * derivativeActivationFunction(output);

        for (int i = 0; i < dimension; i++) {
            weights[i] += alpha * delta * inputs[i];
        }
        threshold -= beta * delta;
    }

    /**
     * Uczy perceptron, aktualizując wagi oraz próg.
     *
     * @param inputs   lista wartości wejściowych
     * @param correctDecision oczekiwana wartość wyjściowa
     */
    public void learn(List<Double> inputs, int correctDecision) {
        learn(inputs.stream().mapToDouble(Double::doubleValue).toArray(), correctDecision);
    }

    /**
     * Zwraca aktualne wagi perceptronu.
     *
     * @return tablica wag
     */
    public double[] getWeights() {
        return weights;
    }

    /**
     * Zwraca aktualny próg perceptronu.
     *
     * @return próg
     */
    public double getThreshold() {
        return threshold;
    }

    /**
     * Wypisuje aktualne wagi i próg.
     */
    public void print() {
        System.out.println(this);
    }

    /**
     * Ustawia wartość współczynnika uczenia wag.
     * @param alpha wartość współczynnika uczenia wag
     */
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /**
     * Ustawia wartość współczynnika uczenia progu.
     * @param beta wartość współczynnika uczenia progu
     */
    public void setBeta(double beta) {
        this.beta = beta;
    }

    @Override
    public String toString() {
        return "Wagi: " + Arrays.toString(weights) + ", próg: " + threshold;
    }
}
