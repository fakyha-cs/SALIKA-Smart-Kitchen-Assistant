/**
 * Represents a recipe with a database ID, name, category, cooking instructions,
 * required ingredients (via RecipeIngredientLinkedList), and nutritional information.
 *
 * DSA Note for Viva:
 * - Uses RecipeIngredientLinkedList (custom singly linked list) to store required ingredients.
 * - Stored inside RecipeBST (custom Binary Search Tree) ordered by recipe name.
 * - Time Complexity for accessing fields: O(1).
 */
public class Recipe {
    public int id;
    public String name;
    public String category; // Main Course, Breakfast, Quick Snacks, Healthy, Desserts, etc.
    public String instructions;
    public RecipeIngredientLinkedList requiredIngredients;

    // Nutritional & Meta Information for SALIKA Modern Features
    public int calories;            // total calories in kcal
    public double protein;          // protein in grams (g)
    public double carbs;            // carbohydrates in grams (g)
    public double fat;              // fats in grams (g)
    public double fiber;            // dietary fiber in grams (g)
    public int prepTimeMinutes;     // preparation & cooking time in minutes
    public String difficulty;       // Easy, Medium, Hard
    public String[] healthTags;     // e.g., ["High Protein", "Low Carb", "Keto", "Desi Special"]
    public boolean isQuickSnack;    // true if fast 5-15 minute meal

    /**
     * Legacy Constructor for backwards compatibility with database loading.
     */
    public Recipe(int id, String name, String category, String instructions, RecipeIngredientLinkedList requiredIngredients) {
        this(id, name, category, instructions, requiredIngredients, 350, 15.0, 40.0, 12.0, 3.0, 20, "Medium", new String[]{"Classic"}, false);
    }

    /**
     * Constructor for creating recipes before database assignment.
     */
    public Recipe(String name, String category, String instructions, RecipeIngredientLinkedList requiredIngredients) {
        this(-1, name, category, instructions, requiredIngredients, 350, 15.0, 40.0, 12.0, 3.0, 20, "Medium", new String[]{"Classic"}, false);
    }

    /**
     * Full Enriched Constructor for SALIKA Modern Features.
     */
    public Recipe(int id, String name, String category, String instructions, 
                  RecipeIngredientLinkedList requiredIngredients,
                  int calories, double protein, double carbs, double fat, double fiber,
                  int prepTimeMinutes, String difficulty, String[] healthTags, boolean isQuickSnack) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.instructions = instructions;
        this.requiredIngredients = requiredIngredients;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.fiber = fiber;
        this.prepTimeMinutes = prepTimeMinutes;
        this.difficulty = difficulty;
        this.healthTags = healthTags;
        this.isQuickSnack = isQuickSnack;
    }

    /**
     * Checks if this recipe matches a given health goal tag (e.g. "High Protein", "Keto").
     * Uses a simple linear loop for beginner-friendly Java style.
     * Time Complexity: O(T) where T is number of tags.
     */
    public boolean hasHealthTag(String tag) {
        if (healthTags == null) return false;
        for (int i = 0; i < healthTags.length; i++) {
            if (healthTags[i].equalsIgnoreCase(tag)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return name + " [" + category + "] - " + calories + " kcal (" + prepTimeMinutes + " mins)";
    }
}
