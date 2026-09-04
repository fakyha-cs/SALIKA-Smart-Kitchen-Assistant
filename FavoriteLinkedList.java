/**
 * Custom singly linked list for storing favorite Recipe objects.
 * Aligns with the instructor's linked list pointer naming.
 */
public class FavoriteLinkedList {
    private FavoriteNode first;
    private int size;

    public FavoriteLinkedList() {
        this.first = null;
        this.size = 0;
    }

    public FavoriteNode getFirst() {
        return first;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Adds a recipe to favorites.
     */
    public void add(Recipe data) {
        if (find(data.name) != null) {
            return; // Avoid duplicates
        }
        FavoriteNode cur = new FavoriteNode(data);
        cur.next = null;

        if (first == null) {
            first = cur;
        } else {
            FavoriteNode temp = first;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = cur;
        }
        size++;
    }

    /**
     * Searches for a recipe in favorites by name.
     */
    public Recipe find(String name) {
        FavoriteNode temp = first;
        while (temp != null) {
            if (temp.data.name.equalsIgnoreCase(name)) {
                return temp.data;
            }
            temp = temp.next;
        }
        return null;
    }

    /**
     * Removes a recipe from favorites by name.
     */
    public boolean remove(String name) {
        if (first == null) {
            return false;
        }

        if (first.data.name.equalsIgnoreCase(name)) {
            FavoriteNode temp = first;
            first = first.next;
            temp.next = null;
            size--;
            return true;
        }

        FavoriteNode pre = first;
        FavoriteNode cur = first.next;
        while (cur != null) {
            if (cur.data.name.equalsIgnoreCase(name)) {
                pre.next = cur.next;
                cur.next = null;
                size--;
                return true;
            }
            pre = cur;
            cur = cur.next;
        }

        return false;
    }

    public void clear() {
        first = null;
        size = 0;
    }
}
