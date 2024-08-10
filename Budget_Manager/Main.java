import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static double balance = 0.0;
    private static final Scanner input = new Scanner(System.in);
    private static final String FileName = "purchases.txt";
    public static DecimalFormat format = new DecimalFormat("#0.00");
    private static final PurchaseManager manager = new PurchaseManager();

    public static void main(String[] args) throws IOException {
        int choice = 1;

        while (choice != 0) {
            switch (getChoice()) {
                case 1:
                    income();
                    break;
                case 2:
                    purchase();
                    break;
                case 3:
                    list();
                    break;
                case 4:
                    balance();
                    break;
                case 5:
                    save();
                    break;
                case 6:
                    load();
                    break;
                case 7:
                    analyse();
                    break;
                default:
                    choice = 0;
                    System.out.println("\n Done!");
            }
        }
    }

    private static void analyse() {
        System.out.println("\n How to you want to sort ? \n" +
            "1) Sort all purchases \n" + 
            "2) Sort by type \n" + 
            "3) Sort cetain type \n" +
            "4) Back");
        
        int option = input.nextInt();
        System.out.println();

        switch (option) {
            case 1:
                if (manager.isEmpty()) {
                    System.out.println("Purchase list is Empty");
                } else {
                    System.out.println("All :");
                    System.out.println(manager.getSortedList(null));
                    System.out.println("Total : $" + format.format(manager.getCurrentSum()));
                }
                break;
            case 2:
                System.out.println("Types :");
                double totalSum = 0.0;
                ArrayList<Purchase> purchases = new ArrayList<>(manager.getPurchases());
                Collections.sort(purchases);

                for (Categories category : Categories.values()) {
                    double sum = 0.0;

                    for (Purchase p : purchases) {
                        if (p.getCategory() == category) {
                            sum += p.getPrice();
                            totalSum += p.getPrice();
                        }
                    }
                    System.out.println(category.description + " - $" + format.format(sum));
                    System.out.println(manager.getList(category));
                }
                System.out.println("Total sum : $" + format.format(totalSum));
                break;
            case 3:
                System.out.println("\n Choose the type of purchase :\n" +
                    Categories.showAll());
                int option1 = input.nextInt();
                Categories category = Categories.findById(option1);

                if (manager.isEmpty() || category == null) {
                    System.out.println("\n Purchase list is empty!");
                } else {
                    System.out.println("\n" + category.description + " : ");
                    System.out.println(manager.getSortedList(category));
                    System.out.println("\n Total sum : $" + format.format(manager.getCurrentSum()));
                }

                break;
            case 4:
                return;
            default:
                break;
        }

        analyse();
    }

    private static void save() throws IOException{
        // root gives a path(directory)
        String  root = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        // System.out.println(root);

        try (FileWriter writer = new FileWriter(root + FileName)) {
            writer.write(String.valueOf(balance));
            writer.write("\n");

            for (Purchase p : manager.getPurchases()) {
                writer.write(p.getCategory().name());
                writer.write("#");
                writer.write(p.getName());
                writer.write("#");
                writer.write(String.valueOf(p.getPrice()));
                writer.write("\n");
            }
        }

        System.out.println("Uploaded!");
    }

    private static void load() throws IOException{
        // path finding
        String root = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();

        try (BufferedReader reader = new BufferedReader(new FileReader(root + FileName))){
            manager.clear();
            balance = Double.parseDouble(reader.readLine()); // file input in string format so convert to double

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] lines = line.split("#");
                manager.add(new Purchase(lines[1], Double.parseDouble(lines[2]), Categories.valueOf(lines[0])));
            }
        }

        System.out.println("\n Purchases were loaded!");
    }

    private static void list() {
        if (manager.isEmpty()) {
            System.out.println("\n Purchase list is Empty");
            return;
        }
        System.out.println("\n Choose the type of  purchase \n" + 
            Categories.showAll() +
            "5) All \n" + 
            "6) Back");
        int in = input.nextInt();

        if (in == 6) {
            return;
        }

        Categories category = Categories.findById(in);
        System.out.println(category == null ? "\n All :" : "\n" + category.description + " : ");
        System.out.println(manager.getList(category));
        System.out.println("\n Total sum : $" + format.format(manager.getCurrentSum()));
        list();
    }

    private static void purchase() {
        System.out.println("\n Choose the type of Purchase : \n" + Categories.showAll() + "5) Back");
        int in = input.nextInt();

        if (in == 5) {
            return;
        }

        Categories category = Categories.findById(in);
        addPurchase(category);

    }

    private static void addPurchase(Categories category) {
        System.out.println("\n Enter item name :");
        input.nextLine();
        String name = input.nextLine();
        System.out.println("\n Enter its price :");
        double price = input.nextDouble();
        System.out.println("Purchase was Added!");
        balance -= (balance == 0.0 ? 0.0 : price);
        manager.add(new Purchase(name, price, category));
        purchase();
    }

    private static void balance() {
        System.out.println("\n Balance : $" + format.format(balance));
    }

    private static void income() {
        System.out.println("\n Enter income : ");
        balance += input.nextInt();

        System.out.println("\n Income was added!");
    }

    private static int getChoice() {
        System.out.println("Chose your action \n" +
            "1) Add income \n" +
            "2) Add purchase \n" +
            "3) Show list of purchases \n" +
            "4) Balance \n" +
            "5) Save \n" +
            "6) Load \n" +
            "7) Analyze (Sort) \n" + 
            "8) Exit");

        return input.nextInt();     // get choice from user
    }
}
