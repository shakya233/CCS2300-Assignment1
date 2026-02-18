package datasorter;

import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SortingAlgorithms sorter = new SortingAlgorithms();
        int[] numbers = null;
        
        while (true) {
            System.out.println("\n=== DATA SORTER MENU ===");
            System.out.println("1. Enter numbers manually");
            System.out.println("2. Generate random numbers");
            System.out.println("3. Bubble Sort");
            System.out.println("4. Merge Sort");
            System.out.println("5. Quick Sort");
            System.out.println("6. 📊 SHOW COMPARISON TABLE");
            System.out.println("7. Exit");
            System.out.print("Choose option: ");
            
            int choice = scanner.nextInt();
            
            switch(choice) {
                case 1:
                    System.out.print("How many numbers? ");
                    int count = scanner.nextInt();
                    numbers = new int[count];
                    System.out.println("Enter " + count + " numbers:");
                    for (int i = 0; i < count; i++) {
                        numbers[i] = scanner.nextInt();
                    }
                    System.out.println("Numbers saved!");
                    break;
                    
                case 2:
                    System.out.print("How many random numbers? ");
                    int size = scanner.nextInt();
                    numbers = new int[size];
                    Random rand = new Random();
                    for (int i = 0; i < size; i++) {
                        numbers[i] = rand.nextInt(1000);
                    }
                    System.out.println("Generated " + size + " random numbers!");
                    System.out.print("Numbers: ");
                    printArray(numbers);
                    break;
                    
                case 3:
                    if (numbers == null) {
                        System.out.println("Please enter/generate numbers first!");
                    } else {
                        int[] arr = numbers.clone();
                        long start = System.nanoTime();
                        sorter.bubbleSort(arr);
                        long end = System.nanoTime();
                        System.out.println("✅ Bubble Sort completed!");
                        System.out.println("⏱️ Time: " + (end-start)/1e6 + " ms");
                        System.out.print("Sorted array: ");
                        printArray(arr);
                    }
                    break;
                    
                case 4:
                    if (numbers == null) {
                        System.out.println("Please enter/generate numbers first!");
                    } else {
                        int[] arr = numbers.clone();
                        long start = System.nanoTime();
                        sorter.mergeSort(arr, 0, arr.length-1);
                        long end = System.nanoTime();
                        System.out.println("✅ Merge Sort completed!");
                        System.out.println("⏱️ Time: " + (end-start)/1e6 + " ms");
                        System.out.print("Sorted array: ");
                        printArray(arr);
                    }
                    break;
                    
                case 5:
                    if (numbers == null) {
                        System.out.println("Please enter/generate numbers first!");
                    } else {
                        int[] arr = numbers.clone();
                        long start = System.nanoTime();
                        sorter.quickSort(arr, 0, arr.length-1);
                        long end = System.nanoTime();
                        System.out.println("✅ Quick Sort completed!");
                        System.out.println("⏱️ Time: " + (end-start)/1e6 + " ms");
                        System.out.print("Sorted array: ");
                        printArray(arr);
                    }
                    break;
                    
                case 6:
                    if (numbers == null) {
                        System.out.println("Please enter/generate numbers first!");
                    } else {
                        sorter.displayComparisonTable(numbers);
                    }
                    break;
                    
                case 7:
                    System.out.println("👋 Goodbye! Thanks for using Data Sorter!");
                    System.exit(0);
                    break;
                    
                default:
                    System.out.println("❌ Invalid option! Please try again.");
            }
        }
    }
    
    public static void printArray(int[] arr) {
        for (int num : arr) {
            System.out.print(num + " ");
        }
        System.out.println();
    }
}
