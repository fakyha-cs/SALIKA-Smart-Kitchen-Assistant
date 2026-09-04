/**
 * A node class for the custom IngredientLinkedList.
 * Represents a single element in the singly linked list of ingredients.
 */
public class IngredientNode {
    public Ingredient data;
    public IngredientNode next;

    /**
     * Constructor to create a new node with the specified ingredient data.
     *
     * @param data The ingredient data to store in this node.
     */
    public IngredientNode(Ingredient data) {
        this.data = data;
        this.next = null;
    }
}
