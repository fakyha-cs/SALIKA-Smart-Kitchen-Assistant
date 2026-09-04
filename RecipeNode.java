/**
 * A node class for the custom RecipeBST (Binary Search Tree).
 * Represents a single element in the binary search tree of recipes.
 */
public class RecipeNode {
    public Recipe data;
    public RecipeNode left;
    public RecipeNode right;

    /**
     * Constructor to create a tree node with the specified recipe data.
     *
     * @param data The Recipe object to store in this node.
     */
    public RecipeNode(Recipe data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }
}
