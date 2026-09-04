/**
 * Node class for the custom FavoriteLinkedList.
 */
public class FavoriteNode {
    public Recipe data;
    public FavoriteNode next;

    public FavoriteNode(Recipe data) {
        this.data = data;
        this.next = null;
    }
}
