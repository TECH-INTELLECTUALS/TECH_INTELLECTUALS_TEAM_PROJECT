package tests;
 
import algorithms.BinarySearch;
import algorithms.InsertionSort;
import algorithms.LinearSearch;
import algorithms.MergeSort;
import algorithms.QuickSort;
import algorithms.SelectionSort;
import java.util.Arrays;
 
public class AlgorithmTests {
 
   public static void main(String[] args) {
      new AlgorithmTests().runTests();
   }
 
   public void runTests() {
      this.testBinarySearch();
      this.testLinearSearch();
      this.testSelectionSort();
      this.testInsertionSort();
      this.testMergeSort();
      this.testQuickSort();
      System.out.println("Algorithm tests passed.");
   }
 
   private void testBinarySearch() {
      // Normal case: sorted array with duplicates.
      // Sorted input is required, since it's a precondition of the algorithm
      // (see invalid case below).
      int[] arr1 = {1, 3, 3, 5, 7, 9, 9, 11};
      BinarySearch bs1 = new BinarySearch(arr1, 9);
      bs1.execute();
      int r1 = bs1.getResult();
      this.assertTrue(r1 == 5 || r1 == 6, "BinarySearch should find value 9 at index 5 or 6");
 
      // Boundary case: empty array
      BinarySearch bs2 = new BinarySearch(new int[]{}, 5);
      bs2.execute();
      this.assertEquals(-1, bs2.getResult(), "BinarySearch on empty array returns -1");
 
      // Boundary case: single element array, found
      BinarySearch bs3 = new BinarySearch(new int[]{42}, 42);
      bs3.execute();
      this.assertEquals(0, bs3.getResult(), "BinarySearch single-element array, value present");
 
      // Boundary case: single element array, not found
      BinarySearch bs4 = new BinarySearch(new int[]{42}, 7);
      bs4.execute();
      this.assertEquals(-1, bs4.getResult(), "BinarySearch single-element array, value absent");
 
      // Invalid case / required counterexample: unsorted array.
      // binarySearch assumes sorted input as a precondition. Running it on
      // an unsorted array can silently return a wrong/inconsistent result
      // rather than throwing an error -- this is exactly why the
      // precondition matters.
      int[] unsorted = {50, 10, 40, 20, 5, 30};
      BinarySearch bs5 = new BinarySearch(unsorted, 20); // 20 is actually at index 3
      bs5.execute();
      this.assertTrue(bs5.getResult() != 3,
            "Counterexample: BinarySearch should NOT reliably find index 3 in an "
            + "unsorted array, proving sorted input is a required precondition");
   }
 
   private void testLinearSearch() {
      // Normal case: unsorted array with duplicates.
      // LinearSearch has no sorted-input precondition, so unsorted data is fine.
      LinearSearch ls1 = new LinearSearch(new int[]{8, 3, 5, 3, 9, 1, 5}, 5);
      ls1.execute();
      this.assertEquals(2, ls1.getResult(), "LinearSearch returns first match at index 2");
 
      // Boundary case: empty array
      LinearSearch ls2 = new LinearSearch(new int[]{}, 5);
      ls2.execute();
      this.assertEquals(-1, ls2.getResult(), "LinearSearch on empty array returns -1");
 
      // Boundary case: single element array, found
      LinearSearch ls3 = new LinearSearch(new int[]{7}, 7);
      ls3.execute();
      this.assertEquals(0, ls3.getResult(), "LinearSearch single-element array, value present");
 
      // Boundary case: single element array, not found
      LinearSearch ls4 = new LinearSearch(new int[]{7}, 99);
      ls4.execute();
      this.assertEquals(-1, ls4.getResult(), "LinearSearch single-element array, value absent");
 
      // Boundary case: already-sorted array (order doesn't matter for LinearSearch)
      LinearSearch ls5 = new LinearSearch(new int[]{1, 2, 3, 4, 5}, 4);
      ls5.execute();
      this.assertEquals(3, ls5.getResult(), "LinearSearch on already-sorted array");
   }
 
   private void testSelectionSort() {
      // Normal case: unsorted array with duplicates
      SelectionSort ss1 = new SelectionSort(new int[]{29, 10, 14, 37, 10, 5, 29});
      ss1.execute();
      this.assertArrayEquals(new int[]{5, 10, 10, 14, 29, 29, 37}, ss1.getResult(),
            "SelectionSort on unsorted array with duplicates");
 
      // Boundary case: empty array
      SelectionSort ss2 = new SelectionSort(new int[]{});
      ss2.execute();
      this.assertArrayEquals(new int[]{}, ss2.getResult(), "SelectionSort on empty array");
 
      // Boundary case: single element array
      SelectionSort ss3 = new SelectionSort(new int[]{42});
      ss3.execute();
      this.assertArrayEquals(new int[]{42}, ss3.getResult(), "SelectionSort on single-element array");
 
      // Boundary case: already-sorted array
      SelectionSort ss4 = new SelectionSort(new int[]{1, 2, 3, 4, 5});
      ss4.execute();
      this.assertArrayEquals(new int[]{1, 2, 3, 4, 5}, ss4.getResult(),
            "SelectionSort on already-sorted array");
 
      // Note: this implementation is NOT stable (direct swap of arr[i] and
      // arr[minIndex] can reorder equal elements). Correctness still holds --
      // stability is documented in the report/trace table, not asserted here,
      // since plain ints carry no identity to track original positions.
      SelectionSort ss5 = new SelectionSort(new int[]{4, 2, 2, 1});
      ss5.execute();
      this.assertArrayEquals(new int[]{1, 2, 2, 4}, ss5.getResult(),
            "SelectionSort correctness check relevant to stability discussion");
   }
 
   private void testInsertionSort() {
      // Normal case: unsorted array with duplicates
      InsertionSort is1 = new InsertionSort(new int[]{29, 10, 14, 37, 10, 5, 29});
      is1.execute();
      this.assertArrayEquals(new int[]{5, 10, 10, 14, 29, 29, 37}, is1.getResult(),
            "InsertionSort on unsorted array with duplicates");
 
      // Boundary case: empty array
      InsertionSort is2 = new InsertionSort(new int[]{});
      is2.execute();
      this.assertArrayEquals(new int[]{}, is2.getResult(), "InsertionSort on empty array");
 
      // Boundary case: single element array
      InsertionSort is3 = new InsertionSort(new int[]{42});
      is3.execute();
      this.assertArrayEquals(new int[]{42}, is3.getResult(), "InsertionSort on single-element array");
 
      // Boundary case: already-sorted array (best case, O(n) -- inner while
      // loop never triggers a shift)
      InsertionSort is4 = new InsertionSort(new int[]{1, 2, 3, 4, 5});
      is4.execute();
      this.assertArrayEquals(new int[]{1, 2, 3, 4, 5}, is4.getResult(),
            "InsertionSort on already-sorted array (best case)");
   }
 
   private void testMergeSort() {
      // Normal case: unsorted array with duplicates
      MergeSort ms1 = new MergeSort(new int[]{29, 10, 14, 37, 10, 5, 29});
      ms1.execute();
      this.assertArrayEquals(new int[]{5, 10, 10, 14, 29, 29, 37}, ms1.getResult(),
            "MergeSort on unsorted array with duplicates");
 
      // Boundary case: empty array.
      // left=0, right=data.length-1=-1, so left >= right triggers the base
      // case immediately -- no crash, no-op.
      MergeSort ms2 = new MergeSort(new int[]{});
      ms2.execute();
      this.assertArrayEquals(new int[]{}, ms2.getResult(), "MergeSort on empty array");
 
      // Boundary case: single element array
      MergeSort ms3 = new MergeSort(new int[]{42});
      ms3.execute();
      this.assertArrayEquals(new int[]{42}, ms3.getResult(), "MergeSort on single-element array");
 
      // Boundary case: already-sorted array
      MergeSort ms4 = new MergeSort(new int[]{1, 2, 3, 4, 5});
      ms4.execute();
      this.assertArrayEquals(new int[]{1, 2, 3, 4, 5}, ms4.getResult(),
            "MergeSort on already-sorted array");
   }
 
   private void testQuickSort() {
      // Normal case: unsorted array with duplicates
      QuickSort qs1 = new QuickSort(new int[]{29, 10, 14, 37, 10, 5, 29});
      qs1.execute();
      this.assertArrayEquals(new int[]{5, 10, 10, 14, 29, 29, 37}, qs1.getResult(),
            "QuickSort on unsorted array with duplicates");
 
      // Boundary case: empty array.
      // low=0, high=data.length-1=-1, so low < high is false -- returns
      // immediately without touching the array.
      QuickSort qs2 = new QuickSort(new int[]{});
      qs2.execute();
      this.assertArrayEquals(new int[]{}, qs2.getResult(), "QuickSort on empty array");
 
      // Boundary case: single element array
      QuickSort qs3 = new QuickSort(new int[]{42});
      qs3.execute();
      this.assertArrayEquals(new int[]{42}, qs3.getResult(), "QuickSort on single-element array");
 
      // Boundary case: already-sorted array.
      // This is QuickSort's WORST case with a last-element pivot: every
      // partition puts all elements on one side, degrading to O(n^2).
      // Correctness still holds -- the report/trace table should flag the
      // O(n^2) worst-case timing risk on sorted/reverse-sorted input.
      QuickSort qs4 = new QuickSort(new int[]{1, 2, 3, 4, 5});
      qs4.execute();
      this.assertArrayEquals(new int[]{1, 2, 3, 4, 5}, qs4.getResult(),
            "QuickSort on already-sorted array (worst-case timing, correctness ok)");
   }
 
   private void assertTrue(boolean condition, String message) {
      if (!condition) {
         throw new AssertionError("Assertion failed: " + message);
      }
   }
 
   private void assertEquals(Object expected, Object actual, String message) {
      if (expected == null) {
         if (actual != null) {
            throw new AssertionError(message + ": expected null but got " + actual);
         }
      } else if (!expected.equals(actual)) {
         throw new AssertionError(message + ": expected " + expected + " but got " + actual);
      }
   }
 
   private void assertArrayEquals(int[] expected, int[] actual, String message) {
      if (!Arrays.equals(expected, actual)) {
         throw new AssertionError(message + ": expected " + Arrays.toString(expected)
               + " but got " + Arrays.toString(actual));
      }
   }
}
 