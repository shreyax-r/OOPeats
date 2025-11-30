package Utils;

import java.util.Scanner;

public class HashPassword {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            String plain;
            if (args.length > 0) {
                plain = args[0];
            } else {
                System.out.print("Enter password to hash: ");
                plain = sc.nextLine();
            }

            String stored = PasswordUtils.generateStrongPasswordHash(plain);
            System.out.println("Hashed value (store this in DB 'password' column):");
            System.out.println(stored);
        } catch (Exception e) {
            System.err.println("Error generating hash: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
