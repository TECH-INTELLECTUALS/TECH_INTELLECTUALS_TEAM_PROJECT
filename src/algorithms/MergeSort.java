package algorithms;

import interfaces.Algorithm;

public class MergeSort implements Algorithm {

    private int[] data;

    public MergeSort(int[] data) {
        this.data = data;
    }

    @Override
    public void execute() {
        mergeSort(data, 0, data.length - 1);
    }

    private void mergeSort(int[] arr, int left, int right) {
        if (left >= right) {
            return;
        }

        int mid = left + (right - left) / 2;

        mergeSort(arr, left, mid);
        mergeSort(arr, mid + 1, right);
        merge(arr, left, mid, right);
    }


    private void merge(int[] arr, int left, int mid, int right) {
        int[] leftArr = new int[mid - left + 1];
        int[] rightArr = new int[right - mid];

        System.arraycopy(arr, left, leftArr, 0, leftArr.length);
        System.arraycopy(arr, mid + 1, rightArr, 0, rightArr.length);

        int i = 0, j = 0, k = left;

        while (i < leftArr.length && j < rightArr.length) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k++] = leftArr[i++];
            } else {
                arr[k++] = rightArr[j++];
            }
        }
        while (i < leftArr.length) {
            arr[k++] = leftArr[i++];
        }
        while (j < rightArr.length) {
            arr[k++] = rightArr[j++];
        }
    }

    public int[] getResult() {
        return data;
    }
}