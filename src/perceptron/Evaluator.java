package perceptron;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Evaluator {

    private final String testFileName;
    private final ArrayList<double[]> testInputs;
    private final ArrayList<Integer> testOutputs;
    private final HashMap<String, Integer> labelsMap;

    public Evaluator(String testFileName) {
        this.testFileName = testFileName;
        this.testInputs = new ArrayList<>();
        this.testOutputs = new ArrayList<>();
        this.labelsMap = new HashMap<>();
        loadTestData();
    }

    private void loadTestData() {
        try (BufferedReader br = new BufferedReader(new FileReader(testFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();

                String[] tokens = line.split(",");
                int n = tokens.length;

                double[] features = new double[n - 1];
                for (int i = 0; i < n - 1; i++) {
                    features[i] = Double.parseDouble(tokens[i].trim());
                }

                String labelName = tokens[n - 1].trim();

                if (!labelsMap.containsKey(labelName)) {
                    if (labelsMap.isEmpty()) {
                        labelsMap.put(labelName, 1);
                    } else {
                        labelsMap.put(labelName, 0);
                    }
                }

                int mappedLabel = labelsMap.get(labelName);

                testInputs.add(features);
                testOutputs.add(mappedLabel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void evaluate(Perceptron perceptron) {
        int TP = 0;
        int FP = 0;
        int TN = 0;
        int FN = 0;

        for (int i = 0; i < testInputs.size(); i++) {
            double[] inputs = testInputs.get(i);
            int actual = testOutputs.get(i);

            double prediction = perceptron.compute(inputs);

            int predicted = (int) prediction;

            if (predicted == 1 && actual == 1) {
                TP++;
            } else if (predicted == 1 && actual == 0) {
                FP++;
            } else if (predicted == 0 && actual == 0) {
                TN++;
            } else if (predicted == 0 && actual == 1) {
                FN++;
            }
        }

        double accuracy = (double) (TP + TN) / (TP + TN + FP + FN);
        double precision = (TP + FP) == 0 ? 0 : (double) TP / (TP + FP);
        double recall = (TP + FN) == 0 ? 0 : (double) TP / (TP + FN);
        double f1 = (precision + recall) == 0 ? 0 : 2.0 * precision * recall / (precision + recall);

        System.out.println("WYNIKI EWALUACJI");
        System.out.println(String.format("Accuracy: %.2f", accuracy));
        System.out.println(String.format("Precision: %.2f", precision));
        System.out.println(String.format("Recall: %.2f", recall));
        System.out.println(String.format("F1: %.2f", f1));
    }
}
