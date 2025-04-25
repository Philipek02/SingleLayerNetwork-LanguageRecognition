package languageclassifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.*;

/**
 * Czyta pliki CSV o formacie:  <język>,<tekst>
 *  np.  polish,"To jest przykład ..."
 * Zbiera wszystkie unikalne języki i nadaje im kolejne indeksy 0..n-1
 */
public class MultiTeacher {

    // język → indeks
    private final Map<String, Integer> languageToIndex = new LinkedHashMap<>();
    private final List<String>          indexToLanguage = new ArrayList<>();

    private final List<String> testSentences = new ArrayList<>();

    private final List<double[]> trainingInputs = new ArrayList<>();
    private final List<Integer> trainingOutputs = new ArrayList<>();
    private final List<double[]> testInputs     = new ArrayList<>();
    private final List<Integer> testOutputs     = new ArrayList<>();

    /* ---------- wczytywanie ---------- */

    public void loadTrainingData(String trainCsv) { loadData(trainCsv, trainingInputs, trainingOutputs, true); }
    public void loadTestData    (String testCsv ) { loadData(testCsv , testInputs    , testOutputs    , false); }

    private void loadData(String file, List<double[]> inputs,
                          List<Integer> outputs, boolean buildDict) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                line = line.replaceAll("\"", "");
                String[] parts = line.split(",", 2);
                if (parts.length < 2) continue;

                String lang  = parts[0].trim().toLowerCase(Locale.ROOT);
                String text  = parts[1].trim();

                if (buildDict && !languageToIndex.containsKey(lang)) {
                    languageToIndex.put(lang, languageToIndex.size());
                    indexToLanguage.add(lang);
                }
                Integer idx = languageToIndex.get(lang);
                if (idx == null) continue;

                inputs.add(textToVector(text));
                outputs.add(idx);

                if (!buildDict) {
                    testSentences.add(text);
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }


    /* ---------- konwersja tekst → 26D wektor ---------- */

    public static double[] textToVector(String text) {
        String norm = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT);

        int[] cnt = new int[26];
        int total = 0;
        for (char c : norm.toCharArray())
            if (c >= 'a' && c <= 'z') { cnt[c-'a']++; total++; }

        double[] v = new double[26];
        for (int i = 0; i < 26; i++) v[i] = cnt[i];
        double len = 0;
        for (double d : v) len += d*d;
        if (len > 0) { len = Math.sqrt(len); for (int i=0;i<26;i++) v[i] /= len; }
        return v;
    }

    /* ---------- trening & ewaluacja ---------- */

    public void train(MultiLayerNetwork net, int epochs) {
        for (int e=1;e<=epochs;e++) {
            for (int i=0;i<trainingInputs.size();i++)
                net.trainOnExample(trainingInputs.get(i), trainingOutputs.get(i));
            System.out.println("Epoka "+e+" zakonczona.");
        }
    }

    public void evaluate(MultiLayerNetwork net, boolean showEvaluation) {
        int good = 0;
        for (int i = 0; i < testInputs.size(); i++) {
            int pred      = net.predict(testInputs.get(i));
            int trueLabel = testOutputs.get(i);

            if (pred == trueLabel) {
                good++;
            } else if (showEvaluation) {
                System.out.printf("""
                    -------------
                    ZDANIE   : %s
                    OCZEKIWANO: %s
                    PRZEWIDZIAL: %s
                    -------------
                    """,
                        testSentences.get(i),
                        indexToLanguage.get(trueLabel),
                        indexToLanguage.get(pred));
            }
        }
        System.out.printf("Dokladnosc: %.2f%%\n", 100.0 * good / testInputs.size());
    }



    public int languageCount()          { return languageToIndex.size(); }
    public List<String> languages()     { return indexToLanguage;        }
}
