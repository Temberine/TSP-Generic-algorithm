import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TSPGeneticAlgorithm {
    
    public static List<List<Integer>> readTSPFile(String filename) throws IOException {
        List<List<Integer>> costs = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        int n = Integer.parseInt(br.readLine());
        for (int i = 0; i < n; i++) {
            List<Integer> row = new ArrayList<>();
            String[] line = br.readLine().split("\\s+");
            for (String s : line) {
                row.add(Integer.parseInt(s));
            }
            costs.add(row);
        }
        br.close();
        return costs;
    }

    // Function to generate a random path
    public static List<Integer> randomPath(int n) {
        List<Integer> path = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            path.add(i);
        }
        Collections.shuffle(path);
        return path;
    }

    // Function to calculate the cost of a path
    public static int pathCost(List<Integer> path, List<List<Integer>> costs) {
        int totalCost = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            totalCost += costs.get(path.get(i)).get(path.get(i + 1));
        }
        totalCost += costs.get(path.get(path.size() - 1)).get(path.get(0));
        return totalCost;
    }

    // Function to perform selection
    public static List<List<Integer>> selection(List<List<Integer>> population, List<List<Integer>> costs) {
        population.sort((p1, p2) -> pathCost(p1, costs) - pathCost(p2, costs));
        return population.subList(0, population.size() / 2);
    }

    // Function to perform crossover
    public static List<Integer> crossover(List<Integer> parent1, List<Integer> parent2) {
        int n = parent1.size();
        Random rand = new Random();
        int start = rand.nextInt(n);
        int end = rand.nextInt(n);
        if (start > end) {
            int temp = start;
            start = end;
            end = temp;
        }

        List<Integer> child = new ArrayList<>(Collections.nCopies(n, null));
        child.subList(start, end + 1).clear();
        for (int i = start; i <= end; i++) {
            child.set(i, parent1.get(i));
        }

        int index = 0;
        for (int i = 0; i < n; i++) {
            if (!child.contains(parent2.get(i))) {
                while (child.get(index) != null) {
                    index++;
                }
                child.set(index++, parent2.get(i));
            }
        }
        return child;
    }

    // Function to perform mutation
    public static List<Integer> mutation(List<Integer> path) {
        Random rand = new Random();
        int i = rand.nextInt(path.size());
        int j = rand.nextInt(path.size());
        Collections.swap(path, i, j);
        return path;
    }

    // Genetic algorithm function
public static List<Integer> geneticAlgorithm(String filename, int generations, int populationSize) throws IOException {
    List<List<Integer>> costs = readTSPFile(filename);
    int n = costs.size();
    List<List<Integer>> population = new ArrayList<>();
    for (int i = 0; i < populationSize; i++) {
    population.add(randomPath(n));
    }
    for (int i = 0; i < generations; i++) {
    List<List<Integer>> newPopulation = new ArrayList<>();
    List<List<Integer>> parents = selection(population, costs);
    for (int j = 0; j < populationSize / 2; j++) {
    List<Integer> parent1 = parents.get(j);
    List<Integer> parent2 = parents.get(populationSize / 2 + j);
    List<Integer> child1 = crossover(parent1, parent2);
    List<Integer> child2 = crossover(parent2, parent1);
    newPopulation.add(mutation(child1));
    newPopulation.add(mutation(child2));
    }
    population = newPopulation;
    }
    population.sort((p1, p2) -> pathCost(p1, costs) - pathCost(p2, costs));
    return population.get(0);
    }

    public static void main(String[] args) throws IOException {
        String filename = "tsp99.txt";
        int generations = 100;
        int populationSize = 100;
    
        // Run the genetic algorithm
        List<Integer> bestPath = geneticAlgorithm(filename, generations, populationSize);
    
        // Print the best path and its cost
        System.out.println("Best path: " + bestPath);
        List<List<Integer>> costs = readTSPFile(filename);
        int cost = pathCost(bestPath, costs);
        System.out.println("Cost: " + cost);
    }
    
}