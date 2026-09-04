/**
 * Node class for the custom RecipeIngredientLinkedList.
 */
public class RecipeIngredientNode {
    public RecipeIngredient data;
    public RecipeIngredientNode next;

    public RecipeIngredientNode(RecipeIngredient data) {
        this.data = data;
        this.next = null;
    }
}
