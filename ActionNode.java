/**
 * Node class for the custom ActionStack.
 */
public class ActionNode {
    public Action data;
    public ActionNode next;

    public ActionNode(Action data) {
        this.data = data;
        this.next = null;
    }
}
