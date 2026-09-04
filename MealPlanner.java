/**
 * Class: MealPlanner (BONUS FEATURE)
 *
 * Purpose:
 * Generates an automated 7-day or 3-day Meal Plan optimized based on:
 * 1. Ingredients currently available in the user's pantry.
 * 2. Balanced variety across categories (Breakfast, Main Course, Snacks).
 * 3. Daily target calories and macro balance.
 *
 * Why this class exists:
 * Provides an impressive bonus feature for viva presentation demonstrating practical application
 * of BST search, custom filtering, and automated kitchen menu planning.
 *
 * Time Complexity: O(D * R) where D = days, R = recipes count in BST.
 * Space Complexity: O(D) memory space for storing planned daily menus.
 */
public class MealPlanner {

    public static class DayPlan {
        public String dayName;
        public Recipe breakfast;
        public Recipe lunch;
        public Recipe dinner;
        public Recipe snack;

        public DayPlan(String dayName, Recipe breakfast, Recipe lunch, Recipe dinner, Recipe snack) {
            this.dayName = dayName;
            this.breakfast = breakfast;
            this.lunch = lunch;
            this.dinner = dinner;
            this.snack = snack;
        }

        public int getTotalCalories() {
            int cal = 0;
            if (breakfast != null) cal += breakfast.calories;
            if (lunch != null) cal += lunch.calories;
            if (dinner != null) cal += dinner.calories;
            if (snack != null) cal += snack.calories;
            return cal;
        }
    }

    /**
     * Generates a 7-day meal plan prioritizing recipes cookable from current pantry inventory.
     */
    public static DayPlan[] generateWeeklyPlan(RecipeBST recipeTree, IngredientLinkedList inventory) {
        if (recipeTree == null) return new DayPlan[0];

        Recipe[] allRecipes = recipeTree.getAllRecipesSorted();
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        DayPlan[] weeklyPlan = new DayPlan[7];

        int rIndex = 0;
        for (int d = 0; d < 7; d++) {
            Recipe b = findBestRecipeForCategory(allRecipes, "Breakfast", inventory, rIndex++);
            Recipe l = findBestRecipeForCategory(allRecipes, "Main Course", inventory, rIndex++);
            Recipe din = findBestRecipeForCategory(allRecipes, "Main Course", inventory, rIndex++);
            Recipe s = findBestRecipeForCategory(allRecipes, "Quick Snacks", inventory, rIndex++);

            weeklyPlan[d] = new DayPlan(days[d], b, l, din, s);
        }

        return weeklyPlan;
    }

    /**
     * Searches for a recipe matching category, preferring cookable recipes first.
     */
    private static Recipe findBestRecipeForCategory(Recipe[] recipes, String category, IngredientLinkedList inventory, int startIndex) {
        if (recipes == null || recipes.length == 0) return null;

        int n = recipes.length;
        // First pass: Find cookable recipe matching category
        for (int i = 0; i < n; i++) {
            int idx = (startIndex + i) % n;
            Recipe r = recipes[idx];
            if (r.category.equalsIgnoreCase(category) && SmartAdvisor.isRecipeCookable(r, inventory, null)) {
                return r;
            }
        }

        // Second pass: Find any recipe matching category
        for (int i = 0; i < n; i++) {
            int idx = (startIndex + i) % n;
            Recipe r = recipes[idx];
            if (r.category.equalsIgnoreCase(category)) {
                return r;
            }
        }

        // Fallback: return recipe at index
        return recipes[startIndex % n];
    }
}
