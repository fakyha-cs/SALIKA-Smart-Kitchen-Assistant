/**
 * Container class representing a recipe recommendation.
 * Holds the recipe, calculated match percentage, and list of missing ingredients.
 */
public class Recommendation {
    public Recipe recipe;
    public double matchPercentage;
    public RecipeIngredientLinkedList missingIngredients;

    public Recommendation(Recipe recipe, double matchPercentage, RecipeIngredientLinkedList missingIngredients) {
        this.recipe = recipe;
        this.matchPercentage = matchPercentage;
        this.missingIngredients = missingIngredients;
    }

    public String toString() {
        return recipe.name + " (" + (int) matchPercentage + "% Match)";
    }
}
