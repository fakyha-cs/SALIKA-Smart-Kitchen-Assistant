/**
 * Custom Singly Linked List for storing Ingredients.
 */
public class IngredientLinkedList {

    private IngredientNode head;
    private int size;

    public IngredientLinkedList() {
        head = null;
        size = 0;
    }

    public IngredientNode getHead() {
        return head;
    }
    public IngredientNode getFirst() {
    return head;
}

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Add ingredient at the end.
     */
    public void add(Ingredient data) {

        IngredientNode newNode = new IngredientNode(data);

        if (head == null) {
            head = newNode;
        } else {

            IngredientNode current = head;

            while (current.next != null) {
                current = current.next;
            }

            current.next = newNode;
        }

        size++;
    }

    /**
     * Find ingredient by name.
     */
    public Ingredient find(String name) {

        IngredientNode current = head;

        while (current != null) {

            if (current.data.name.equalsIgnoreCase(name)) {
                return current.data;
            }

            current = current.next;
        }

        return null;
    }

    /**
     * Remove ingredient by name.
     */
    public boolean remove(String name) {

        if (head == null) {
            return false;
        }

        if (head.data.name.equalsIgnoreCase(name)) {
            head = head.next;
            size--;
            return true;
        }

        IngredientNode current = head;

        while (current.next != null) {

            if (current.next.data.name.equalsIgnoreCase(name)) {
                current.next = current.next.next;
                size--;
                return true;
            }

            current = current.next;
        }

        return false;
    }

    /**
     * Update ingredient quantity.
     */
    public boolean updateQuantity(String name, double newQuantity) {

        IngredientNode current = head;

        while (current != null) {

            if (current.data.name.equalsIgnoreCase(name)) {

                current.data.quantity = newQuantity;
                return true;
            }

            current = current.next;
        }

        return false;
    }

    /**
     * Remove all ingredients.
     */
    public void clear() {
        head = null;
        size = 0;
    }

}