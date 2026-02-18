package datasorter;

import java.util.Scanner;

public class DataSorter {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n=== DATA SORTER MENU ===");
            System.out.println("1. Enter numbers manually");
            System.out.println("2. Generate random numbers");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();

            if (choice == 3) {
                System.out.println("Exiting...");
                break;
            }

            int[] arr = DataGenerator.getArray(choice, sc);
            if (arr == null) continue;

            int[] bubbleArr = arr.clone();
            int[] mergeArr = arr.clone();
            int[] quickArr = arr.clone();

            // Bubble Sort timing
            long start = System.nanoTime();
            SortingAlgorithms.bubbleSort(bubbleArr);
            long bubbleTime = System.nanoTime() - start;

            // Merge Sort timing
            start = System.nanoTime();
            SortingAlgorithms.mergeSort(mergeArr, 0, mergeArr.length - 1);
            long mergeTime = System.nanoTime() - start;

            // Quick Sort timing
            start = System.nanoTime();
            SortingAlgorithms.quickSort(quickArr, 0, quickArr.length - 1);
            long quickTime = System.nanoTime() - start;

            System.out.println("\nSorted Output (Quick Sort):");
            printArray(quickArr);

            System.out.println("\n=== Performance Comparison ===");
            System.out.printf("%-15s %-15s\n", "Algorithm", "Time (ns)");
            System.out.printf("%-15s %-15d\n", "Bubble Sort", bubbleTime);
            System.out.printf("%-15s %-15d\n", "Merge Sort", mergeTime);
            System.out.printf("%-15s %-15d\n", "Quick Sort", quickTime);
        }
    }

    public static void printArray(int[] arr) {
        for (int num : arr)
            System.out.print(num + " ");
        System.out.println();
    }
}
