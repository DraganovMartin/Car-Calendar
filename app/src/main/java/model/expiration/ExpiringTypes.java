package model.expiration;

/**
 * Created by dimcho on 23.02.18.
 */

/**
 * Models all the stuff from the model that can expire
 */
public enum ExpiringTypes {
    TAX("Tax"),
    INSURANCE("Insurance"),
    VIGNETTE("Vignette");

    private String value;
    ExpiringTypes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
