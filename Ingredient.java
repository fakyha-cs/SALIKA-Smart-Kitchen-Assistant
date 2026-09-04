/**
 * Represents an ingredient with an ID, name, quantity, and unit of measurement.
 * This class is used for tracking ingredients in the kitchen inventory.
 */
public class Ingredient {
    public int id;
    public String name;
    public double quantity;
    public String unit;
    public boolean isOwned; // Added to track whether this ingredient is in the kitchen inventory

    /**
     * Constructor to initialize an ingredient with database ID.
     */
    public Ingredient(int id, String name, double quantity, String unit) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.isOwned = false; // Default to not owned until checked
    }

    /**
     * Constructor for creating ingredients before database assignment.
     */
    public Ingredient(String name, double quantity, String unit) {
        this.id = -1;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.isOwned = false; // Default to not owned until checked
    }

    public String toString() {
        return name + " (" + quantity + " " + unit + ")";
    }
}
