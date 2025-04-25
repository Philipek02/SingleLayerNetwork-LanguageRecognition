package perceptron;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Teacher {

    private final String trainFileName;
    private final ArrayList<double[]> trainingInputs;
    private final ArrayList<Integer> trainingOutputs;
    private final Map<String, Integer> labels;

    /**
     * Konstruktor Teacher.
     *
     * @param trainFileName nazwa pliku CSV ze zbioru treningowego
     */
    public Teacher(String trainFileName) {
        this.trainFileName = trainFileName;
        trainingInputs = new ArrayList<>();
        trainingOutputs = new ArrayList<>();
        labels = new HashMap<>();
        this.loadTrainingData();
    }

    public static void main(String[] args) {
        String trainFileName;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Podaj nazwę pliku CSV ze zbiorem treningowym:");
        trainFileName = scanner.nextLine();

        Teacher teacher = new Teacher(trainFileName);
        Perceptron perceptron = new DiscretePerceptron(teacher.getDimension());
        int epochLimit = 100;

        teacher.train(perceptron, epochLimit);
    }

    /**
     * Wczytuje dane z pliku CSV. Mapuje pierwszą napotkaną klasę na 1, a kolejne na 0.
     *
     * @param filename nazwa pliku
     * @param inputs   lista, do której zapisywane są wektory cech
     * @param outputs  lista, do której zapisywane są etykiety
     */
    private void loadData(String filename, ArrayList<double[]> inputs, ArrayList<Integer> outputs) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.split(",");
                int n = tokens.length;
                double[] features = new double[n - 1];
                for (int i = 0; i < n - 1; i++) {
                    features[i] = Double.parseDouble(tokens[i].trim());
                }

                String labelName = tokens[n - 1].trim();
                int label;
                if (labels.isEmpty()) {
                    label = 1;
                    labels.put(labelName, label);
                } else {
                    label = labels.getOrDefault(labelName, 0);
                    labels.put(labelName, label);
                }

                inputs.add(features);
                outputs.add(label);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wczytuje dane treningowe.
     */
    public void loadTrainingData() {
        loadData(trainFileName, trainingInputs, trainingOutputs);
    }

    /**
     * Uczy perceptron na zbiorze treningowym
     * @param perceptron perceptron do nauczenia
     * @param epochLimit maksymalna liczba epok
     */
    public void train(Perceptron perceptron, int epochLimit) {
        if (trainingInputs.isEmpty()) {
            System.out.println("Brak danych treningowych!");
            return;
        }

        for (int epoch = 0; epoch < epochLimit; epoch++) {
            for (int i = 0; i < trainingInputs.size(); i++) {
                perceptron.learn(trainingInputs.get(i), trainingOutputs.get(i));
            }
            System.out.println("Epoka " + (epoch + 1) + ":");
            perceptron.print();
        }
    }

    /**
     * Zwraca wymiar wektora wejściowego / liczbę atrybutów.
     * @return wymiar wektora wejściowego
     */
    public int getDimension() {
        return trainingInputs.get(0).length;
    }

}
