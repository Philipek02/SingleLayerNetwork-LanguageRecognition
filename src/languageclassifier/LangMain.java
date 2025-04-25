package languageclassifier;

import java.util.List;
import java.util.Scanner;

public class LangMain {
    public static void main(String[] args) {
        String trainCsv = "resources/lang.train.csv";
        String testCsv  = "resources/lang.test.csv";
        int epochCount = 2000;

        boolean showWrongPredictions = true;




        MultiTeacher teacher = new MultiTeacher();
        teacher.loadTrainingData(trainCsv);
        teacher.loadTestData(testCsv);

        MultiLayerNetwork net = new MultiLayerNetwork(26, teacher.languageCount());
        teacher.train(net, epochCount);
        teacher.evaluate(net, showWrongPredictions);


        /* --- interaktywne rozpoznawanie --- */
        List<String> langs = teacher.languages();
        Scanner sc = new Scanner(System.in);
        for (;;) {
            System.out.print("\nWpisz tekst (exit aby wyjsc): ");
            String line = sc.nextLine();
            if ("exit".equalsIgnoreCase(line)) break;
            int pred = net.predict(MultiTeacher.textToVector(line));
            System.out.println("Wykryto jezyk: " + langs.get(pred));
        }
    }
}
