package model.expiration;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import model.Stickers.IVignette;
import model.Stickers.Insurance;
import model.Vehicle.Car;
import model.Vehicle.Vehicle;
import model.taxes.Tax;

public class ExpirationManager {
    // A list expiration data objects for all vehicles
    private final List<VehicleExpirationData> expirationData;

    // A queue of expiration messages
    // Used to store messages for a single vehicle.
    // In the future it could be used to store data for more vehicles which would
    // reduce the number of spawned notifications
    private final Queue<ExpirationMessage> expMessagesForVehicle;

    public ExpirationManager() {
        expirationData = new ArrayList<>();
        expMessagesForVehicle = new LinkedList<>();
    }

    public void addExpirationData(Vehicle v, String owner, int notificationInterval) {
        expirationData.add(new VehicleExpirationData(v, owner, notificationInterval));
    }

    public List<VehicleExpirationData> getVehicleExpirationData() {
        return expirationData;
    }

    public void addExpirationMessage(ExpirationMessage expMessage) {
        expMessagesForVehicle.add(expMessage);
    }

    /**
     * Constructs a single string message from all the NotificationMessage objects
     * in the expMessagesForVehicle queue. The messages in the queue are removed and
     * compacted into a single string.
     * @return a single string message combining all the expiration messages for the current vehicle
     */
    public String constructNotificationMessage() {
        StringBuilder builder = new StringBuilder();
        int messageCount = expMessagesForVehicle.size();
        for (int i = 0; i < messageCount; i++) {
            ExpirationMessage currExpMessage = expMessagesForVehicle.poll();
            builder.append(i+1);
            builder.append(". ");
            builder.append(currExpMessage.getExpirationMessage());
            builder.append('\n');
            builder.append('\n');
        }

        Log.d("Test", builder.toString().trim());
        return builder.toString().trim();
    }

    public class VehicleExpirationData {
        private final Vehicle vehicle; // Which vehicle
        private final String owner; // Whose vehicle
        private final int notificationInterval; // Start notifying this many days before expiration

        private final Map<ExpiringTypes, Pair<Integer, Integer>> expiringStuff; // What's expiring

        // Prevents instantiation
        private VehicleExpirationData(Vehicle v, String owner, int notInterval) {
            this.vehicle = v;
            this.owner = owner;
            this.notificationInterval = notInterval;
            this.expiringStuff = new EnumMap<>(ExpiringTypes.class);
        }

        public Vehicle getVehicle() {
            return vehicle;
        }
        public String getOwner() {
            return owner;
        }

        /**
         * Gets a map of all expiring stuff for the vehicle.
         * The ExpiringTypes key can have the values ExpiringTypes.Tax,
         * ExpiringTypes.Insurance, ExpiringTypes.Vignette.
         *
         * The value stored in the is a Pair of two Integers
         * The first value in the pair represents the days until the
         * given tax, insurance or vignette expires.
         * The second value represents the current active period of an insurance object.
         * Vignettes and Taxes have a single period, so this field is ignored and null is passed.
         * @return Map of ExpiringTypes and Pair of two Integers
         */
        public Map<ExpiringTypes, Pair<Integer, Integer>> getExpiringStuff() {
            Tax tax = vehicle.getTax();

            // TODO warn users that the tax, vignette, insurance expired (handle -1 case of getRemainingDays())
            // TODO The above is related to TODO 2. in ExpirationMessage
            int taxExpDays = tax.getRemainingDays(notificationInterval);
            if(taxExpDays >= 0) {
                expiringStuff.put(ExpiringTypes.TAX, new Pair<>(taxExpDays, null));
            }

            Log.d("Test", "Exp days " + taxExpDays);

            Insurance insurance = vehicle.getInsurance();
            Pair<Integer, Integer> remainingDaysPeriodPair =
                    insurance.getRemainingDaysAndPeriodNumber(notificationInterval);
            if(remainingDaysPeriodPair.first >= 0) {
                expiringStuff.put(ExpiringTypes.INSURANCE, remainingDaysPeriodPair);
            }

            if(vehicle instanceof Car) {
                IVignette vignette = ((Car) (vehicle)).getVignette();
                int vignetteExpDays = vignette.getRemainingDays(notificationInterval);

                if (vignetteExpDays >= 0) {
                    expiringStuff.put(ExpiringTypes.VIGNETTE, new Pair<>(vignetteExpDays, null));
                }
            }

            return expiringStuff;
        }
    }
}