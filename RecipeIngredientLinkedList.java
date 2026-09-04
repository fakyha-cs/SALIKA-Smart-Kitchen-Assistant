/**
 * Custom singly linked list for storing RecipeIngredient objects.
 * Aligns with instructor's pointer naming conventions.
 */
public class RecipeIngredientLinkedList {
    private RecipeIngredientNode first; // matches sir's style
    private int size;

    public RecipeIngredientLinkedList() {
        this.first = null;
        this.size = 0;
    }

    public RecipeIngredientNode getFirst() {
        return first;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Adds a recipe ingredient to the list.
     */
    public void add(RecipeIngredient data) {
        RecipeIngredientNode cur = new RecipeIngredientNode(data);
        cur.next = null;

        if (first == null) {
            first = cur;
        } else {
            RecipeIngredientNode temp = first;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = cur;
        }
        size++;
    }

    /**
     * Searches for a recipe ingredient by name.
     */
    public RecipeIngredient find(String ingredientName) {
        RecipeIngredientNode temp = first;
        while (temp != null) {
            if (temp.data.ingredientName.equalsIgnoreCase(ingredientName)) {
                return temp.data;
            }
            temp = temp.next;
        }
        return null;
    }

    /**
     * Searches for a recipe ingredient by ID.
     */
    public RecipeIngredient findById(int id) {
        RecipeIngredientNode temp = first;
        while (temp != null) {
            if (temp.data.ingredientId == id) {
                return temp.data;
            }
            temp = temp.next;
        }
        return null;
    }
}
