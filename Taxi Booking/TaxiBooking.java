import java.util.ArrayList;

public class TaxiBooking {
    private static ArrayList<Taxi> taxiList = new ArrayList<>();
    private static int taxiListLimit = 4, idGenerator = 1;
    private static ArrayList<Taxi> taxiBookedHistory = new ArrayList<>();

    public static String booking(char pickupLocation, char dropLocation, int pickupTime) /*throws CloneNotSupportedException */{
        if (taxiList.size() < taxiListLimit) {
            taxiList.add(new Taxi());
        }

        int min = Integer.MAX_VALUE;
        Taxi taxiReady = null;

        for (Taxi t : taxiList) {
            if (t.getDropTime() <= pickupTime && Math.abs(pickupLocation - t.getCurrentLocation()) <= min) {
                if (Math.abs(pickupLocation - t.getCurrentLocation()) == min) {
                    if (taxiReady != null && t.getEarnings() < taxiReady.getEarnings()) {
                        taxiReady = t;
                    }
                } else {
                    taxiReady = t;
                    min = Math.abs(pickupLocation - t.getCurrentLocation());
                }
            }
        }

        if (taxiReady != null) {
            taxiReady.setCustomerId(idGenerator++);
            taxiReady.setPickupTime(pickupTime);
            taxiReady.setPickupLocation(pickupLocation);
            taxiReady.setDropLocation(dropLocation);
            taxiReady.setCurrentLocation(dropLocation);
            taxiReady.setDropTime(pickupTime + Math.abs(dropLocation - pickupLocation));
            taxiReady.setEarnings(taxiReady.getEarnings() + (Math.abs(dropLocation - pickupLocation)* 15 - 5) * 10 + 100);
            taxiReady.setTaxiId(taxiList.indexOf(taxiReady) + 1);
            // taxiBookedHistory.add((Taxi)taxiReady.clone()); // cloneObject
            taxiBookedHistory.add(taxiReady);
        }

        return taxiReady != null ? "taxi number : " + taxiReady.getTaxiId() + " is booked!" :"Taxis not available";
    }

    public static void display() {
        System.out.println("----------------------------------");

        for (Taxi t : taxiList) {
            System.out.println(t.toString());
            System.out.println("-----------------------------------");
        }
    }
}