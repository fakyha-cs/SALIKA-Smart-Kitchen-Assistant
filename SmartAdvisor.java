/**
 * Class: SmartAdvisor
 *
 * Purpose:
 * Central intelligence module for SALIKA. Implements:
 * 1. Ingredient Explorer (traverses BST to find recipes with specific ingredient)
 * 2. Smart Shopping Advisor ("Buying X unlocks Y recipes")
 * 3. Pantry Analytics (Most used ingredient, Most missing ingredient)
 * 4. Kitchen Score Gauge computation (0-100%)
 *
 * Student Code Style & Viva Notes:
 * - Uses simple arrays, loops, and custom BST traversals.
 * - Explains time & space complexities explicitly for viva examination.
 */
public class SmartAdvisor {

    /**
     * FEATURE 1: INGREDIENT EXPLORER
     * Traverses all recipes in the RecipeBST and returns an array of recipes
     * that contain the given ingredient.
     *
     * DSA Rationale:
     * - Uses recipeTree.getAllRecipesSorted() (in-order BST traversal -> O(N)).
     * - Iterates through each recipe's RecipeIngredientLinkedList -> O(M per recipe).
     * Time Complexity: O(N * M) where N = total recipes, M = average ingredients per recipe.
     * Space Complexity: O(K) where K is number of matching recipes.
     */
    public static Recipe[] exploreIngredient(RecipeBST recipeTree, String ingredientName) {
        if (recipeTree == null || ingredientName == null || ingredientName.trim().isEmpty()) {
            return new Recipe[0];
        }

        Recipe[] allRecipes = recipeTree.getAllRecipesSorted();
        Recipe[] tempMatches = new Recipe[allRecipes.length];
        int count = 0;

        for (int i = 0; i < allRecipes.length; i++) {
            Recipe r = allRecipes[i];
            RecipeIngredientLinkedList reqList = r.requiredIngredients;
            if (reqList != null) {
                RecipeIngredientNode curr = reqList.getFirst();
                while (curr != null) {
                    if (curr.data.ingredientName.equalsIgnoreCase(ingredientName.trim())) {
                        tempMatches[count++] = r;
                        break; // Recipe matches, no need to check remaining ingredients of this recipe
                    }
                    curr = curr.next;
                }
            }
        }

        // Copy matching recipes to exact size array
        Recipe[] matches = new Recipe[count];
        System.arraycopy(tempMatches, 0, matches, 0, count);
        return matches;
    }

    /**
     * FEATURE 2: SMART SHOPPING ADVISOR
     * Class to hold advice on buying an unowned ingredient and what recipes it unlocks.
     */
    public static class UnlockInsight {
        public String ingredientName;
        public int unlockedCount;
        public String unlockedRecipeNames; // Comma separated list of recipes unlocked

        public UnlockInsight(String ingredientName, int unlockedCount, String unlockedRecipeNames) {
            this.ingredientName = ingredientName;
            this.unlockedCount = unlockedCount;
            this.unlockedRecipeNames = unlockedRecipeNames;
        }
    }

    /**
     * Analyzes all unowned ingredients in inventory to find which purchase yields the maximum
     * number of newly cookable recipes ("Buying Butter unlocks 3 recipes!").
     *
     * Time Complexity: O(I * N * M) where I = unowned ingredients, N = recipes, M = ingredients per recipe.
     */
    public static UnlockInsight[] getTopUnlockInsights(RecipeBST recipeTree, IngredientLinkedList inventory) {
        if (recipeTree == null || inventory == null) return new UnlockInsight[0];

        Recipe[] allRecipes = recipeTree.getAllRecipesSorted();

        // 1. Identify unowned ingredients
        IngredientLinkedList unownedList = new IngredientLinkedList();
        IngredientNode currIng = inventory.getFirst();
        while (currIng != null) {
            if (!currIng.data.isOwned) {
                unownedList.add(currIng.data);
            }
            currIng = currIng.next;
        }

        if (unownedList.isEmpty()) {
            return new UnlockInsight[0];
        }

        UnlockInsight[] insights = new UnlockInsight[unownedList.size()];
        int insightIndex = 0;

        IngredientNode unownedCurr = unownedList.getFirst();
        while (unownedCurr != null) {
            String testIngName = unownedCurr.data.name;
            int unlockedCount = 0;
            String recipeNames = "";

            for (int i = 0; i < allRecipes.length; i++) {
                Recipe r = allRecipes[i];

                // Check if recipe is currently cookable WITHOUT testIngName
                boolean cookableNow = isRecipeCookable(r, inventory, null);
                // Check if recipe becomes cookable WITH testIngName simulated as owned
                boolean cookableWithTest = isRecipeCookable(r, inventory, testIngName);

                if (!cookableNow && cookableWithTest) {
                    unlockedCount++;
                    if (recipeNames.length() > 0) recipeNames += ", ";
                    recipeNames += r.name;
                }
            }

            if (unlockedCount > 0) {
                insights[insightIndex++] = new UnlockInsight(testIngName, unlockedCount, recipeNames);
            }

            unownedCurr = unownedCurr.next;
        }

        // Copy valid insights
        UnlockInsight[] validInsights = new UnlockInsight[insightIndex];
        System.arraycopy(insights, 0, validInsights, 0, insightIndex);

        // Sort descending by unlockedCount using standard Bubble Sort
        for (int i = 0; i < validInsights.length - 1; i++) {
            for (int j = 0; j < validInsights.length - i - 1; j++) {
                if (validInsights[j].unlockedCount < validInsights[j + 1].unlockedCount) {
                    UnlockInsight temp = validInsights[j];
                    validInsights[j] = validInsights[j + 1];
                    validInsights[j + 1] = temp;
                }
            }
        }

        return validInsights;
    }

    /**
     * Core Ingredient Cookability Helper.
     * Evaluates if 100% of a recipe's core ingredients are owned.
     * Optionally simulates possessing 'simulatedOwnedIngredient'.
     *
     * Time Complexity: O(M) per recipe.
     */
    public static boolean isRecipeCookable(Recipe recipe, IngredientLinkedList inventory, String simulatedOwnedIngredient) {
        if (recipe == null || recipe.requiredIngredients == null) return true;

        RecipeIngredientNode node = recipe.requiredIngredients.getFirst();
        while (node != null) {
            RecipeIngredient req = node.data;
            if (req.isCoreIngredient) {
                boolean owned = false;
                if (simulatedOwnedIngredient != null && req.ingredientName.equalsIgnoreCase(simulatedOwnedIngredient)) {
                    owned = true;
                } else {
                    Ingredient invItem = inventory.find(req.ingredientName);
                    if (invItem != null && invItem.isOwned && invItem.quantity >= req.quantity) {
                        owned = true;
                    }
                }
                if (!owned) {
                    return false; // Core ingredient missing -> cannot cook
                }
            }
            node = node.next;
        }
        return true;
    }

    /**
     * FEATURE 11: KITCHEN SCORE GAUGE (0 to 100)
     * Calculates kitchen efficiency based on owned pantry items, cookable recipes, and daily macro progress.
     */
    public static int calculateKitchenScore(RecipeBST recipeTree, IngredientLinkedList inventory, NutritionTracker nutrition) {
        if (inventory == null || inventory.isEmpty()) return 0;

        int totalIngredients = inventory.size();
        int ownedCount = 0;
        IngredientNode curr = inventory.getFirst();
        while (curr != null) {
            if (curr.data.isOwned) ownedCount++;
            curr = curr.next;
        }
        double pantryRatio = (double) ownedCount / totalIngredients;

        Recipe[] allRecipes = recipeTree != null ? recipeTree.getAllRecipesSorted() : new Recipe[0];
        int cookableCount = 0;
        for (int i = 0; i < allRecipes.length; i++) {
            if (isRecipeCookable(allRecipes[i], inventory, null)) {
                cookableCount++;
            }
        }
        double recipeRatio = allRecipes.length > 0 ? (double) cookableCount / allRecipes.length : 0.0;

        double nutritionRatio = nutrition != null ? (nutrition.getCaloriePercentage() / 100.0) : 0.0;

        // Weighted Score: 40% Pantry + 40% Cookable Recipes + 20% Nutrition
        double finalScore = (pantryRatio * 40.0) + (recipeRatio * 40.0) + (nutritionRatio * 20.0);
        return Math.min(100, Math.max(0, (int) finalScore));
    }
}
