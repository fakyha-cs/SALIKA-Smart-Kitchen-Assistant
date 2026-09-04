/**
 * Represents a historical action performed by the user that can be undone.
 * It acts as a snapshot of the modified state.
 */
public class Action {
    public String type; // e.g., "ADD_INVENTORY", "UPDATE_INVENTORY", "DELETE_INVENTORY", "COOK"
    public String targetName;
    public String description;
    public double value;
    public String unit;
    
    // For single-ingredient operations
    public Ingredient singleIngredient;
    public double prevQuantity; // For UPDATE_INVENTORY
    
    // For multi-ingredient operations (like cooking)
    public IngredientLinkedList multipleIngredients;


    /**
     * Simple constructor used by the current Swing screens.
     * Stores only the small amount of information needed for Undo history.
     */
    public Action(String type, String targetName, double value, String unit) {
        this.type = type;
        this.targetName = targetName;
        this.value = value;
        this.unit = unit;
        this.description = type.replace("_", " ") + " - " + targetName;
        this.singleIngredient = null;
        this.multipleIngredients = null;
        this.targetName = singleIngredient != null ? singleIngredient.name : "";
        this.description = type.replace("_", " ") + " - " + this.targetName;
    }

    /**
     * Constructor for single ingredient actions (Add, Delete).
     *
     * @param type             The type of action (e.g., "ADD_INVENTORY", "DELETE_INVENTORY")
     * @param singleIngredient The ingredient that was modified/added/deleted.
     */
    public Action(String type, Ingredient singleIngredient) {
        this.type = type;
        this.singleIngredient = singleIngredient;
        this.multipleIngredients = null;
    }

    /**
     * Constructor for updating a single ingredient quantity.
     *
     * @param type             Should be "UPDATE_INVENTORY"
     * @param singleIngredient The ingredient that was updated.
     * @param prevQuantity     The quantity of the ingredient before it was updated.
     */
    public Action(String type, Ingredient singleIngredient, double prevQuantity) {
        this.type = type;
        this.singleIngredient = singleIngredient;
        this.prevQuantity = prevQuantity;
        this.multipleIngredients = null;
    }

    /**
     * Constructor for multi-ingredient actions (like Cooking).
     *
     * @param type                Should be "COOK"
     * @param multipleIngredients A linked list containing snapshots of ingredients before cooking.
     */
    public Action(String type, IngredientLinkedList multipleIngredients) {
        this.type = type;
        this.singleIngredient = null;
        this.multipleIngredients = multipleIngredients;
    }
}
