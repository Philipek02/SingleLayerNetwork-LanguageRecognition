package perceptron;

public class Main {

    public static void main(String[] args) {

        //training and testing single perceptron on iris dataset

        String trainFileName = "resources/perceptron.data";
        String testFileName = "resources/perceptron.test.data";
        int epochLimit = 1;

        Teacher teacher = new Teacher(trainFileName);

        Perceptron perceptron = new DiscretePerceptron(teacher.getDimension());
        teacher.train(perceptron, epochLimit);

        Evaluator evaluator = new Evaluator(testFileName);
        evaluator.evaluate(perceptron);
    }
}
