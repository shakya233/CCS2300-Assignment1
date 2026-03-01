package datasorter;

import java.util.Random;
import java.util.Scanner;

public class DataGenerator {

    public static int[] getArray(int choice, Scanner sc) {

        System.out.print("Enter number of elements: ");
        int n = sc.nextInt();

        int[] arr = new int[n];

        if (choice == 1) {
            System.out.println("Enter numbers:");

            for (int i = 0; i < n; i++)
                arr[i] = sc.nextInt();

        } else if (choice == 2) {

            Random rand = new Random();

            for (int i = 0; i < n; i++)
                arr[i] = rand.nextInt(1000);

            System.out.println("Generated numbers:");

            for (int num : arr)
                System.out.print(num + " ");

            System.out.println();

        } else {
            System.out.println("Invalid choice!");
                  return null;
        }
        return arr;
    }
}

