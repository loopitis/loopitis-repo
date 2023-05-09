package general;

import java.util.ArrayList;
import java.util.List;

public class TestBalancing {

    private static Integer[] c1 = {100, 56, 56, 34};
    private static Integer[] c2 = {100, 34};
    private static Integer[] c3 = {100, 56, 34};

    private static List<Integer[]> consumersWeights = new ArrayList<>();

    public static void main(String[] args) {
        consumersWeights.add(c1);
        consumersWeights.add(c2);
        consumersWeights.add(c3);

        int allAvg = calcAllAvg(consumersWeights);
        int distance = calcDistanceFromAvg(allAvg, consumersWeights);

        System.out.println("Avg = " + allAvg + " Distance = " + distance);
        boolean existsChange = true;
        while (existsChange) {
            existsChange = false;
            for (int i = 0; i < consumersWeights.size(); i++) {//index 0 is the most weighted\bigger
                for (int j = consumersWeights.size() - 1; j > i; j--) {
                    Integer[] bigger = consumersWeights.get(i);
                    Integer[] smaller = consumersWeights.get(j);
                    for (Integer b : bigger) {//from the bigger to the smaller assuming the array is sorted
                        int newDistance = getNewDistance(bigger, smaller, b, allAvg);
                        if (newDistance < 0) {
                            distance -= newDistance;
                            existsChange = true;
                        }
                    }
                }
            }
        }

    }

    private static int getNewDistance(Integer[] bigger, Integer[] smaller, Integer candidate, int allAvg) {
        int sumBigger = getSum(bigger);
        int sumSmaller = getSum(smaller);
        int distanceOld = Math.abs(sumBigger - allAvg) + Math.abs(sumSmaller - allAvg);

        int sumNewBigger = sumBigger - candidate;
        int sumNewSmaller = sumSmaller + candidate;

        int distanceNew = Math.abs(sumNewBigger - allAvg) + Math.abs(sumNewSmaller - allAvg);

        return distanceNew - distanceOld;

    }

    private static int getSum(Integer[] cons) {
        int sum = 0;
        for (Integer i : cons) {
            sum += i;
        }
        return sum;
    }

    private static int calcDistanceFromAvg(int allAvg, List<Integer[]> consumersWeights) {
        int distance = 0;

        for (Integer[] cons : consumersWeights) {
            int sum = getSum(cons);
            distance += Math.abs(sum - allAvg);
        }

        return distance;

    }

    private static int calcAllAvg(List<Integer[]> consumersWeights) {
        int sum = 0;
        for (Integer[] cons : consumersWeights) {
            sum += getSum(cons);
        }
        return sum / consumersWeights.size();

    }
}
