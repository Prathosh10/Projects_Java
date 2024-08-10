import java.util.*;

public class TicketCancelling extends TicketBooking{
    
    // only for RAC
    private static char preferenceTracker = '\0';
    private static int cancelledSeatNumber = 0;

    private static Map<Integer, Character> seatNUmberWithBerth = new HashMap<Integer, Character>();

    public static String cancelling(int id) {
        for (Passenger p : confirmedList) {
            if (p.getId() == id) {
                cancel(p);
                return "success";
            }
        }

        for (Passenger p : racQueue) {
            if (p.getId() == id) {
                cancel(p);
                return "Success";
            }
        }

        for (Passenger p : waitingQueue) {
            if (p.getId() == id) {
                cancel(p);
                return "Success";
            }
        }

        return "Invalid Id!!!";
    }

    public static void cancel(Passenger p) {
        if (p.getTicketType() == "berth") {
            // only for RAC 
            preferenceTracker = p.getPreference();
            cancelledSeatNumber = p.getSeatNumber();
            // Map for reference in future
            seatNUmberWithBerth.put(cancelledSeatNumber, preferenceTracker);

            deleteFromAllList(p);
            addRacToBerth(racQueue.poll());   // change candidate rac to berth
            addWaitingToRac(waitingQueue.poll()); //change candidate waiting to RAC 
        }
        else if (p.getTicketType() == "rac") {
            racQueue.remove(p);
            addWaitingToRac(waitingQueue.poll());
        }
        else {
            waitingQueue.remove(p);
        }
    }

    public static void deleteFromAllList(Passenger p) {
        confirmedList.remove(p);
        upperList.remove(p);
        middleList.remove(p);
        lowerList.remove(p);
    }

    public static void addWaitingToRac(Passenger p){
        if (p != null) {
            p.setTicketType("rac");
            racQueue.add(p);
        }
    }

    public static void addRacToBerth(Passenger p) {
        if (p != null) {
            p.setPreference(preferenceTracker);
            p.setSeatNumber(cancelledSeatNumber);
            p.setTicketType("berth");

            if (preferenceTracker == 'U') {
                upperList.add(p);
            }
            else if (preferenceTracker == 'M') {
                middleList.add(p);
            }
            else {
                lowerList.add(p);
            }

            confirmedList.add(p);
            seatNUmberWithBerth.remove(cancelledSeatNumber);
            preferenceTracker = '\0';
            cancelledSeatNumber = 0;
        }

    }

    public static Map<Integer, Character> getSeatNumberWithBerth() {
        return seatNUmberWithBerth;
    }
}
