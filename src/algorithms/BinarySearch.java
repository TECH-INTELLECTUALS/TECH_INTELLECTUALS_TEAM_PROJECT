package algorithms;

import interfaces.Algorithm;

public class BinarySearch implements Algorithm {

    private int[] data;
    private int target;
    private int result = -1; 
    public BinarySearch(int[] data, int target) {
        this.data = data;
        this.target = target;
    }

    @Override
    public void execute() {
        result = binarySearch(data, target);
        if (result != -1) {
            System.out.println("Found " + target + " at index " + result);
        } else {
            System.out.println(target + " not found in array");
        }
    }

    /**
     * Iterative binary search on a sorted array.
     * @param arr sorted array to search
     * @param key value to find
     * @return index of key, or -1 if not present
     */
    private int binarySearch(int[] arr, int key) {
        int low = 0;
        int high = arr.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2; 

            if (arr[mid] == key) {
                return mid;
            } else if (arr[mid] < key) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    public int getResult() {
        return result;
    }
}