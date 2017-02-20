import java.util.Random;

public class GeneticAlgorithms {

  private static final double weightChangeChanceThreshold = 0.99;
  private static final double weightChangeAmount =  0.05;

  // Run through each weight of the new child and update them if the random number is > threshold.

  public static NeuralNetwork reproduceWithModifications(NeuralNetwork neuralNetwork) {

    Random random = new Random();

    // Start indexing with x = 1 as the input layer does not need to be updated
    for(int x = 1; x < neuralNetwork.nn.size(); x++) {
      for (int y = 0; y < neuralNetwork.nn.get(x).size(); y ++) {
        for (int z = 0; z < neuralNetwork.nn.get(x).get(y).weights.size(); z ++)

        if(random.nextDouble() > weightChangeChanceThreshold) {

          double weight = neuralNetwork.nn.get(x).get(y).weights.get(z);

          // Get amount to change, between -0.5 and 0.5 then * by weightChangeAmount
          double amountToChange = (random.nextDouble() - 0.5) * weightChangeAmount;

          neuralNetwork.nn.get(x).get(y).weights.set(z, weight + amountToChange);
        }
      }
    }
    return neuralNetwork;
  }
}
