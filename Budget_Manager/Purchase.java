import java.text.DecimalFormat;

public class Purchase implements Comparable<Purchase> {
    
    private final String name;
    private final double price;
    private final Categories category;

    public Purchase(String name, double price, Categories category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    // getter methods
    public Categories getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    // Override method
    @Override
    public int compareTo(Purchase o) {
        Purchase o1 = this;
        Purchase o2 = o;

        if (o1.price < o2.getPrice()) {
            return 1;
        } else if (o1.price > o2.getPrice()) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return name + " $" + new DecimalFormat("#0.00").format(price);
    }
}
