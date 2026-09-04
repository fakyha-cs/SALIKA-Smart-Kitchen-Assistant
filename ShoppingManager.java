/**
 * Calculates missing ingredients and compiling shopping lists.
 * Compares required recipe ingredients against current inventory quantities.
 */
public class ShoppingManager {

    /**
     * Generates a list of ingredients that are missing or insufficient in quantity
     * for a given recipe, compared to the current kitchen inventory.
     */
    public static IngredientLinkedList generateShoppingList(Recipe recipe, IngredientLinkedList inventory) {
        IngredientLinkedList shoppingList = new IngredientLinkedList();
        RecipeIngredientLinkedList reqList = recipe.requiredIngredients;

        if (reqList == null || reqList.isEmpty()) {
            return shoppingList;
        }

        RecipeIngredientNode temp = reqList.getFirst();
        while (temp != null) {
            RecipeIngredient req = temp.data;

            // Only core ingredients are checked for the shopping list
            if (req.isCoreIngredient) {
                Ingredient owned = inventory.find(req.ingredientName);

                // If the ingredient is not in the list, or is in the list but NOT owned in our kitchen, it's missing
                if (owned == null || !owned.isOwned) {
                    // Ingredient is completely missing
                    shoppingList.add(new Ingredient(req.ingredientName, req.quantity, req.unit));
                } else if (owned.quantity < req.quantity) {
                    // Ingredient is present in kitchen but quantity is insufficient
                    double shortfall = req.quantity - owned.quantity;
                    shoppingList.add(new Ingredient(req.ingredientName, shortfall, req.unit));
                }
            }
            temp = temp.next;
        }

        return shoppingList;
    }
}
