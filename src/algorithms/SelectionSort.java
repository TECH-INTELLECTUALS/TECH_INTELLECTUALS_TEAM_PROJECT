package algorithms;

import interfaces.Algorithm;

public class SelectionSort implements Algorithm {

    private int[] data;

    public SelectionSort(int[] data) {
        this.data = data;
    }

    @Override
    public void execute() {
        selectionSort(data);
    }

    private void selectionSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;

            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }

            if (minIndex != i) {
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
            }
        }
    }

    public int[] getResult() {
        return data;
    }
}
