import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean loop = true;

        while(loop) {
            System.out.println("choose any one \n 1. Book ticket \n 2. Display details \n 3. Exit ");
            Scanner s = new Scanner(System.in);
            int n = s.nextInt();
            
            switch (n) {
                case 1:
                {
                    System.out.println("Enter Pickup Location : ");
                    char pickupLocation = s.next().charAt(0);
                    System.out.println("Enter drop location ");
                    char dropLocation = s.next().charAt(0);
                    System.out.println("Enter pickup time : ");
                    int pickupTime = s.nextInt();
                    System.out.println(TaxiBooking.booking(pickupLocation, dropLocation, pickupTime));
                    break;
                }

                case 2:
                {
                    TaxiBooking.display();
                    break;
                }

                case 3:
                {
                    loop = false;
                    System.out.println("\n Thank You!!!");
                    s.close();
                    break;
                }
            }
        }
    }
}