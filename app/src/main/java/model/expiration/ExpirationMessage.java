package model.expiration;

/**
 * Created by dimcho on 25.02.18.
 */

public class ExpirationMessage {
    private String expirationMessage;

    private ExpirationMessage(String expMessage){
        this.expirationMessage = expMessage;
    }

    /**
     *
     * @return a String representing the ExpirationMessage
     */
    public String getExpirationMessage() {
        return expirationMessage;
    }

    public static class Builder {
        private String vehicleType;
        private String vehicleRegNum;
        private String expiringTypeName;
        private int remainingDaysToExpiration;
        private int insuranceIntervalNumber;

        /**
         *
         * @param number the period number 1(first period), 2(second period), etc ...
         * @return A human readable insurance period for the notification
         */
        private String getHumanReadablePeriod(int number) {
            switch (++number) {
                case 1: return "1st";
                case 2: return "2nd";
                case 3: return "3d";
                case 4: return "4th";
            }

            try {
                throw new Exception("No such insurance period");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        public Builder(){
            // If no interval is set then the interval value is ignored
            // by assigning it -1
            insuranceIntervalNumber = -1;
        }

        public Builder setVehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
            return this;
        }

        public Builder setVehicleRegNum(String vehicleRegNum) {
            this.vehicleRegNum = vehicleRegNum;
            return this;
        }

        public Builder setExpiringTypeName(String expiringTypeName) {
            this.expiringTypeName = expiringTypeName;
            return this;
        }

        public Builder setRemainingDaysToExpiration(int remainingDaysToExpiration) {
            this.remainingDaysToExpiration = remainingDaysToExpiration;
            return this;
        }

        public Builder setInsuranceIntervalNumber(int insuranceIntervalNumber) {
            this.insuranceIntervalNumber = insuranceIntervalNumber;
            return this;
        }

        /**
         * Builds a human readable ExpirationMessage
         * with the provided data from the Builder' s setters.
         *
         * Example messages:
         *
         * 1. Message for Tax: Tax for <vehicleType>: <vehicleRegNum> is expiring in <num> day(s)!
         * 2. Message for vignette is the same as above
         * 3. Message for insurance : Insurance for for <vehicleType>: <vehicleRegNum>,
         * <period> is expiring in <num> day(s)!
         *
         * If expiration expires today the end of the message says:
         * <first part>... expires today! Please renew it. Where the first part can be:
         *
         * 1. Tax: Tax for <vehicleType>: <vehicleRegNum> or
         * 2. Insurance for for <vehicleType>: <vehicleRegNum>, <period>
         *
         * @return an ExpirationMessage object
         */
        public ExpirationMessage build() {
            // TODO 1.use string resources
            // TODO 2.handle already expired stuff, also look at ExpirationManager.getExpiringStuff()
            String fullMessage;

            String expiringText;
            String remainingDaysText;

            if(remainingDaysToExpiration == 0) {
                remainingDaysText = " expires today! Please renew it.";
            } else {
                remainingDaysText = " is expiring in " + remainingDaysToExpiration + " day(s)!";
            }

            // if insuranceIntervalNumber == -1 then the insurance has only one period
            if(insuranceIntervalNumber != -1) {
                expiringText = ", " + getHumanReadablePeriod(insuranceIntervalNumber) +
                        " period " + remainingDaysText;
            } else {
                expiringText = remainingDaysText;
            }

            fullMessage = expiringTypeName + " for " + vehicleType +
                    ": " + vehicleRegNum + expiringText;

            return new ExpirationMessage(fullMessage);
        }
    }
}