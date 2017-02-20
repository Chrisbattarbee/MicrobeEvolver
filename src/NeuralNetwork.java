import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NeuralNetwork {

  List<Integer> topology = new ArrayList<>();
  List<List<Neuron>>  nn = new ArrayList<>();

  private final int numberOfHiddenLayers = 2;
  private final int lowerNNWidth = 4;
  private final int upperNNWidth = 12;
  private final int numberOfOutputs = 2;
  private final int additionalInputsThatArentVision = 0;

  public NeuralNetwork(List<Integer> topology) {
    initFromTopology(topology);
  }

  private void initFromTopology(List<Integer> topology) {
    this.topology = topology;

    nn.add(new ArrayList<>());

    for (int x = 0; x < topology.get(0); x ++){
      nn.get(0).add(new Neuron(0, false));
    }

    for (int x = 1; x < topology.size(); x ++) {
      nn.add(new ArrayList<>());

      for (int y = 0; y < topology.get(x); y ++) {
        if (x == topology.size() - 1) {
          nn.get(x).add(new Neuron(topology.get(x - 1), true));
        }
        nn.get(x).add(new Neuron(topology.get(x - 1), false));
      }
    }
  }

  public NeuralNetwork(int numberOfDivisions) {
    Random random = new Random();

    List<Integer> topology = new ArrayList<>();

    topology.add(numberOfDivisions * 2 + 1 + additionalInputsThatArentVision);

    for (int x = 0; x < numberOfHiddenLayers; x ++ ) {
      topology.add(random.nextInt(upperNNWidth - lowerNNWidth) + lowerNNWidth);
    }

    topology.add(numberOfOutputs);
    initFromTopology(topology);
  }

  public void feedForward(List<Double> inputs) {

    // Set the input neurons
    for (int x = 0; x < inputs.size(); x ++) {
      nn.get(0).get(x).setValue(inputs.get(x));
    }

    // Hidden layer neurons
    for (int x = 1; x < nn.size(); x ++) {
      for (int y = 0; y < nn.get(x).size(); y ++) {

        List<Double> values = new ArrayList<>();

        for (int z = 0; z < nn.get(x - 1).size(); z++) {

          values.add(nn.get(x - 1).get(z).getValue());

        }

        nn.get(x).get(y).feedForward(values);
      }
    }
  }

  public List<Double> getOutput() {
    List<Double> values = new ArrayList<>();

    for (int x = 0; x < nn.get(nn.size() - 1).size(); x ++) {
      values.add(nn.get(nn.size() - 1).get(x).getValue());
    }

    return values;
  }

  public double getDistance() {
    return getOutput().get(0);
  }

  public double getDirection() {
    return getOutput().get(1);
  }
}
