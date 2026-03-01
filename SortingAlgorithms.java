package datasorter;

public class SortingAlgorithms {
    
    // ========== BUBBLE SORT ==========
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n-i-1; j++) {
                if (arr[j] > arr[j+1]) {
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    }
    
    // ========== MERGE SORT ==========
    public static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }
    
    public static void merge(int[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        
        int[] L = new int[n1];
        int[] R = new int[n2];
        
        for (int i = 0; i < n1; i++)
            L[i] = arr[left + i];
        for (int j = 0; j < n2; j++)
            R[j] = arr[mid + 1 + j];
        
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }
        
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }
        
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }
    
    // ========== QUICK SORT ==========
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = 0;
			try {
				pi = partition(arr, low, high);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }
    
    public static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        
        return i + 1;
    }
    
    // ========== COMPARISON TABLE ==========
    public void displayComparisonTable(int[] data) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("           📊 SORTING ALGORITHMS PERFORMANCE COMPARISON");
        System.out.println("=".repeat(80));
        
        // Create copies for each algorithm
        int[] bubbleArray = data.clone();
        int[] mergeArray = data.clone();
        int[] quickArray = data.clone();
        
        // Table header
        System.out.printf("| %-15s | %-15s | %-15s | %-15s | %-10s |\n", 
                          "Algorithm", "Time (ms)", "Comparisons", "Swaps", "Size");
        System.out.println("=".repeat(80));
        
        // Test Bubble Sort
        long startTime = System.nanoTime();
        int bubbleComparisons = bubbleSortWithCount(bubbleArray);
        long endTime = System.nanoTime();
        double bubbleTime = (endTime - startTime) / 1_000_000.0;
        System.out.printf("| %-15s | %-15.3f | %-15d | %-15s | %-10d |\n", 
                          "Bubble Sort", bubbleTime, bubbleComparisons, "N/A", data.length);
        
        // Test Merge Sort
        startTime = System.nanoTime();
        int mergeComparisons = mergeSortWithCount(mergeArray, 0, mergeArray.length-1);
        endTime = System.nanoTime();
        double mergeTime = (endTime - startTime) / 1_000_000.0;
        System.out.printf("| %-15s | %-15.3f | %-15d | %-15s | %-10d |\n", 
                          "Merge Sort", mergeTime, mergeComparisons, "N/A", data.length);
        
        // Test Quick Sort
        startTime = System.nanoTime();
        int quickComparisons = quickSortWithCount(quickArray, 0, quickArray.length-1);
        endTime = System.nanoTime();
        double quickTime = (endTime - startTime) / 1_000_000.0;
        System.out.printf("| %-15s | %-15.3f | %-15d | %-15s | %-10d |\n", 
                          "Quick Sort", quickTime, quickComparisons, "N/A", data.length);
        
        System.out.println("=".repeat(80));
        
        // Summary
        System.out.println("\n📈 PERFORMANCE SUMMARY:");
        System.out.println("   • Fastest: " + getFastest(bubbleTime, mergeTime, quickTime));
        System.out.println("   • Slowest: " + getSlowest(bubbleTime, mergeTime, quickTime));
        System.out.println("   • Bubble Sort: O(n²) - Quadratic time");
        System.out.println("   • Merge Sort: O(n log n) - Linearithmic time");
        System.out.println("   • Quick Sort: O(n log n) - Linearithmic time (average)");
    }
    
    // Bubble Sort with comparison count
    private int bubbleSortWithCount(int[] arr) {
        int n = arr.length;
        int comparisons = 0;
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n-i-1; j++) {
                comparisons++;
                if (arr[j] > arr[j+1]) {
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
        return comparisons;
    }
    
    // Merge Sort with comparison count
    private int mergeSortWithCount(int[] arr, int left, int right) {
        int comparisons = 0;
        if (left < right) {
            int mid = left + (right - left) / 2;
            comparisons += mergeSortWithCount(arr, left, mid);
            comparisons += mergeSortWithCount(arr, mid + 1, right);
            comparisons += mergeWithCount(arr, left, mid, right);
        }
        return comparisons;
    }
    
    private int mergeWithCount(int[] arr, int left, int mid, int right) {
        int comparisons = 0;
        int n1 = mid - left + 1;
        int n2 = right - mid;
        
        int[] L = new int[n1];
        int[] R = new int[n2];
        
        for (int i = 0; i < n1; i++)
            L[i] = arr[left + i];
        for (int j = 0; j < n2; j++)
            R[j] = arr[mid + 1 + j];
        
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            comparisons++;
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }
        
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }
        
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
        
        return comparisons;
    }
    
    // Quick Sort with comparison count
    private int quickSortWithCount(int[] arr, int low, int high) {
        int comparisons = 0;
        if (low < high) {
            int[] pi = partitionWithCount(arr, low, high);
            int pivotIndex = pi[0];
            comparisons += pi[1];
            comparisons += quickSortWithCount(arr, low, pivotIndex - 1);
            comparisons += quickSortWithCount(arr, pivotIndex + 1, high);
        }
        return comparisons;
    }
    
    private int[] partitionWithCount(int[] arr, int low, int high) {
        int comparisons = 0;
        int pivot = arr[high];
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            comparisons++;
            if (arr[j] <= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        
        return new int[]{i + 1, comparisons};
    }
    
    // Helper method to find fastest
    private String getFastest(double b, double m, double q) {
        if (b <= m && b <= q) return "Bubble Sort (but inefficient for large data)";
        if (m <= b && m <= q) return "Merge Sort";
        return "Quick Sort";
    }
    
    // Helper method to find slowest
    private String getSlowest(double b, double m, double q) {
        if (b >= m && b >= q) return "Bubble Sort";
        if (m >= b && m >= q) return "Merge Sort";
        return "Quick Sort";
    }
}

