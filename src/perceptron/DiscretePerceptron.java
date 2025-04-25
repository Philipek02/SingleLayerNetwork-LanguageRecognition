package perceptron;

public class DiscretePerceptron extends Perceptron {

    /**
     * Konstruktor perceptronu.
     *
     * @param dimension liczba atrybutów (długość wektora wejściowego)
     */
    public DiscretePerceptron(int dimension) {
        super(dimension);
    }

    @Override
    public double activationFunction(double net) {
        return net >= 0 ? 1 : 0;
    }

    @Override
    public double derivativeActivationFunction(double net) {
        return 1;
    }
}
