package algorithms;

import interfaces.Algorithm;

public class InsertionSort implements Algorithm {

    private int[] data;

    public InsertionSort(int[] data) {
        this.data = data;
    }

    @Override
    public void execute() {
        insertionSort(data);
    }

    private void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    public int[] getResult() {
        return data;
    }
}
