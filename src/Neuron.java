import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Neuron {

  List<Double> weights = new ArrayList<>();
  private double value;
  private boolean isOutput;


  // Sigmoid function
  private void applyOutputActivationFunction() {
    value = 1 / (1 + Math.pow(Math.E, -value));
  }

  private void applyIntermediateActivationFunction() {
    value = Math.tanh(value);
  }

  public Neuron(int numNeuronsLastLayer, boolean isOutput) {
    Random random = new Random();
    this.isOutput = isOutput;

    for (int x = 0; x < numNeuronsLastLayer; x++) {
      weights.add(random.nextDouble() * 2 - 1);
    }
  }

  public void feedForward(List<Double> input) {

    double sum = 0;
    for(int x = 0; x < input.size(); x ++) {
      sum += input.get(x) * weights.get(x);
    }
    value = sum;
    if (isOutput) {
      applyOutputActivationFunction();
    } else {
      applyIntermediateActivationFunction();
    }
  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }
}
