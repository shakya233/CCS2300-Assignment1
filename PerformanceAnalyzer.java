import java.util.Random;

public class PerformanceAnalyzer {

    public static int[] generateArray(int size) {
        Random rand = new Random();
        int[] arr = new int[size];

        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(10000);
        }
        return arr;
    }

    public static void main(String[] args) {

        int[] sizes = {100, 500, 1000};

        System.out.println("-------------------------------------------------------------");
        System.out.println(" Size | Linear Search Time (ns) | Quick Sort Time (ns)");
        System.out.println("-------------------------------------------------------------");

        for (int size : sizes) {

            int[] arr = generateArray(size);
            int searchKey = arr[size - 1];   // worst-case for linear search

            // -------- Linear Search Timing --------
            long startSearch = System.nanoTime();
            LinearSearch.linearSearch(arr, searchKey);
            long endSearch = System.nanoTime();
            long searchTime = endSearch - startSearch;

            // -------- Quick Sort Timing --------
            int[] copyArr = arr.clone();

            long startSort = System.nanoTime();
            QuickSort.quickSort(copyArr, 0, copyArr.length - 1);
            long endSort = System.nanoTime();
            long sortTime = endSort - startSort;

            System.out.printf(" %4d | %22d | %21d \n", size, searchTime, sortTime);
        }

        System.out.println("-------------------------------------------------------------");
    }
}