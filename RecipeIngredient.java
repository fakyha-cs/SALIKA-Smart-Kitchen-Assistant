/**
 * Represents an ingredient relationship requirement inside a Recipe.
 * Stores the required ingredient's database ID, name, quantity, unit,
 * and whether it is a core ingredient (or optional/spice).
 */
public class RecipeIngredient {
    public int ingredientId;
    public String ingredientName;
    public double quantity;
    public String unit;
    public boolean isCoreIngredient;

    public RecipeIngredient(int ingredientId, String ingredientName, double quantity, String unit, boolean isCoreIngredient) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.unit = unit;
        this.isCoreIngredient = isCoreIngredient;
    }

    public String toString() {
        return ingredientName + " (" + quantity + " " + unit + ")" + (isCoreIngredient ? " [Core]" : " [Spice/Optional]");
    }
}
