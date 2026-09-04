/**
 * A custom Binary Search Tree (BST) implementation for storing Recipe objects.
 * Sorted alphabetically by recipe name.
 * Follows the instructor's recursive patterns and naming conventions (ptr, left, right).
 */
public class RecipeBST {
    private RecipeNode root;
    private int count;

    public RecipeBST() {
        this.root = null;
        this.count = 0;
    }

    public RecipeNode getRoot() {
        return root;
    }

    public int getCount() {
        return count;
    }

    /**
     * Inserts a new recipe.
     */
    public void insertRecipe(Recipe recipe) {
        root = insertRec(root, recipe);
        count++;
    }

    // Alias to support legacy code in the workspace
    public void insert(Recipe recipe) {
        insertRecipe(recipe);
    }

    private RecipeNode insertRec(RecipeNode root, Recipe recipe) {
        if (root == null) {
            root = new RecipeNode(recipe);
            return root;
        }

        int compareResult = recipe.name.compareToIgnoreCase(root.data.name);

        if (compareResult < 0) {
            root.left = insertRec(root.left, recipe);
        } else {
            root.right = insertRec(root.right, recipe);
        }

        return root;
    }

    /**
     * Searches for a recipe by name.
     */
    public Recipe searchRecipe(String name) {
        return searchRec(root, name);
    }

    // Alias to support legacy code in the workspace
    public Recipe search(String name) {
        return searchRecipe(name);
    }

    private Recipe searchRec(RecipeNode root, String name) {
        if (root == null) {
            return null;
        }
        if (root.data.name.equalsIgnoreCase(name)) {
            return root.data;
        }

        int compareResult = name.compareToIgnoreCase(root.data.name);

        if (compareResult < 0) {
            return searchRec(root.left, name);
        } else {
            return searchRec(root.right, name);
        }
    }

    /**
     * Standard BST Node deletion.
     */
    public void deleteRecipe(String name) {
        root = deleteRec(root, name);
    }

    private RecipeNode deleteRec(RecipeNode root, String name) {
        if (root == null) {
            return null;
        }

        int compareResult = name.compareToIgnoreCase(root.data.name);

        if (compareResult < 0) {
            root.left = deleteRec(root.left, name);
        } else if (compareResult > 0) {
            root.right = deleteRec(root.right, name);
        } else {
            // Node with only one child or no child
            if (root.left == null) {
                count--;
                return root.right;
            } else if (root.right == null) {
                count--;
                return root.left;
            }

            // Node with two children: Get successor and copy its value
            root.data = minValue(root.right);

            // Delete the successor
            root.right = deleteRec(root.right, root.data.name);
        }

        return root;
    }

    private Recipe minValue(RecipeNode node) {
        Recipe minv = node.data;
        while (node.left != null) {
            minv = node.left.data;
            node = node.left;
        }
        return minv;
    }

    /**
     * BST In-order Traversal (prints sorted recipes to console).
     */
    public void inorderTraversal() {
        inorder(root);
    }

    private void inorder(RecipeNode ptr) {
        if (ptr != null) {
            inorder(ptr.left);
            System.out.println(ptr.data.name + "\t");
            inorder(ptr.right);
        }
    }

    /**
     * BST Pre-order Traversal.
     */
    public void preorderTraversal() {
        preorder(root);
    }

    private void preorder(RecipeNode ptr) {
        if (ptr != null) {
            System.out.println(ptr.data.name + "\t");
            preorder(ptr.left);
            preorder(ptr.right);
        }
    }

    /**
     * BST Post-order Traversal.
     */
    public void postorderTraversal() {
        postorder(root);
    }

    private void postorder(RecipeNode ptr) {
        if (ptr != null) {
            postorder(ptr.left);
            postorder(ptr.right);
            System.out.println(ptr.data.name + "\t");
        }
    }

    /**
     * In-order traversal helper to gather all recipes into a sorted array.
     */
    public Recipe[] getAllRecipesSorted() {
        Recipe[] sortedList = new Recipe[count];
        int[] index = new int[1];
        index[0] = 0;
        inOrderRec(root, sortedList, index);
        return sortedList;
    }

    private void inOrderRec(RecipeNode root, Recipe[] list, int[] index) {
        if (root != null) {
            inOrderRec(root.left, list, index);
            list[index[0]] = root.data;
            index[0]++;
            inOrderRec(root.right, list, index);
        }
    }
}
