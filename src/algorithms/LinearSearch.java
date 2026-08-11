package algorithms;

import interfaces.Algorithm;


public class LinearSearch implements Algorithm {

    private int[] data;
    private int target;
    private int result = -1; // index of target, or -1 if not found

    public LinearSearch(int[] data, int target) {
        this.data = data;
        this.target = target;
    }

    @Override
    public void execute() {
        result = linearSearch(data, target);
        if (result != -1) {
            System.out.println("Found " + target + " at index " + result);
        } else {
            System.out.println(target + " not found in array");
        }
    }

    private int linearSearch(int[] arr, int key) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == key) {
                return i;
            }
        }
        return -1;
    }

    public int getResult() {
        return result;
    }
}