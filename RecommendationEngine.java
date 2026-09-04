/**
 * Class: RecommendationEngine
 *
 * Purpose:
 * Core engine that matches kitchen inventory against recipes in RecipeBST.
 * Ranks recipes by calculated match percentage using a custom Bubble Sort algorithm.
 *
 * Student Code Style & Viva Notes:
 * - DSA Hero: Uses custom RecipeBST traversal and custom RecipeIngredientLinkedList iteration.
 * - Core Ingredient Validation: Strictly enforces that a recipe cannot be 100% ready to cook
 *   unless all core ingredients (e.g. Lentils and Rice for Daal Chawal) are in stock.
 * - Time Complexity:
 *   - Traversal & Matching: O(N * M) where N = total recipes, M = ingredients per recipe.
 *   - Sorting: O(K^2) custom Bubble Sort where K = valid recommendation count.
 * - Space Complexity: O(K) temporary arrays.
 */
public class RecommendationEngine {

    /**
     * Matches the kitchen inventory ingredients against all recipes in the BST and returns
     * an array of ranked Recommendations (highest match percentage first).
     */
    public static Recommendation[] generateRecommendations(RecipeBST recipeTree, IngredientLinkedList inventory) {
        if (recipeTree == null || inventory == null) {
            return new Recommendation[0];
        }

        // Retrieve all recipes from BST in sorted alphabetical order
        Recipe[] allRecipes = recipeTree.getAllRecipesSorted();
        int recipeCount = allRecipes.length;

        Recommendation[] tempRecs = new Recommendation[recipeCount];
        int validCount = 0;

        for (int i = 0; i < recipeCount; i++) {
            Recipe recipe = allRecipes[i];
            RecipeIngredientLinkedList reqList = recipe.requiredIngredients;

            if (reqList == null || reqList.isEmpty()) {
                tempRecs[validCount++] = new Recommendation(recipe, 100.0, new RecipeIngredientLinkedList());
                continue;
            }

            int totalRequired = 0;
            int matchedCount = 0;
            boolean missingCoreIngredient = false;
            RecipeIngredientLinkedList missingList = new RecipeIngredientLinkedList();

            RecipeIngredientNode temp = reqList.getFirst();
            while (temp != null) {
                RecipeIngredient req = temp.data;
                totalRequired++;

                // Check if user owns this ingredient in their kitchen inventory with sufficient quantity
                Ingredient owned = inventory.find(req.ingredientName);
                if (owned != null && owned.isOwned && owned.quantity >= req.quantity) {
                    matchedCount++;
                } else {
                    missingList.add(req);
                    if (req.isCoreIngredient) {
                        missingCoreIngredient = true; // Core ingredient is missing!
                    }
                }
                temp = temp.next;
            }

            double matchPercent = totalRequired > 0 ? ((double) matchedCount / totalRequired * 100.0) : 100.0;

            // If a core ingredient is missing, cap match percentage to max 85% to indicate not fully cookable
            if (missingCoreIngredient && matchPercent == 100.0) {
                matchPercent = 85.0;
            }

            // Only recommend recipes matching 25% or higher
            if (matchPercent >= 25.0) {
                tempRecs[validCount++] = new Recommendation(recipe, matchPercent, missingList);
            }
        }

        // Copy valid recommendations to clean sized array
        Recommendation[] recommendations = new Recommendation[validCount];
        System.arraycopy(tempRecs, 0, recommendations, 0, validCount);

        // Sort using custom Bubble Sort (ranking highest match first)
        sortRecommendations(recommendations);

        return recommendations;
    }

    /**
     * Filters recommendations by specific health goal tag (e.g. "High Protein", "Low Carb", "Keto").
     * Time Complexity: O(R) linear scan.
     */
    public static Recommendation[] filterByHealthGoal(Recommendation[] recs, String healthGoal) {
        if (recs == null || healthGoal == null || healthGoal.equalsIgnoreCase("All")) {
            return recs;
        }

        Recommendation[] temp = new Recommendation[recs.length];
        int count = 0;
        for (int i = 0; i < recs.length; i++) {
            if (recs[i].recipe.hasHealthTag(healthGoal) || recs[i].recipe.category.equalsIgnoreCase(healthGoal)) {
                temp[count++] = recs[i];
            }
        }

        Recommendation[] filtered = new Recommendation[count];
        System.arraycopy(temp, 0, filtered, 0, count);
        return filtered;
    }

    /**
     * Filters recommendations for Quick Snacks page (recipes with prep time <= 15 mins or isQuickSnack = true).
     * Time Complexity: O(R).
     */
    public static Recommendation[] filterQuickSnacks(Recommendation[] recs) {
        if (recs == null) return new Recommendation[0];

        Recommendation[] temp = new Recommendation[recs.length];
        int count = 0;
        for (int i = 0; i < recs.length; i++) {
            if (recs[i].recipe.isQuickSnack || recs[i].recipe.prepTimeMinutes <= 15) {
                temp[count++] = recs[i];
            }
        }

        Recommendation[] filtered = new Recommendation[count];
        System.arraycopy(temp, 0, filtered, 0, count);
        return filtered;
    }

    /**
     * Custom Bubble Sort to sort recommendations in descending order.
     * Ranks recipes with higher match percentages first.
     * Time Complexity: O(N^2) worst case.
     */
    private static void sortRecommendations(Recommendation[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].matchPercentage < arr[j + 1].matchPercentage) {
                    // Swap
                    Recommendation temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }
}
