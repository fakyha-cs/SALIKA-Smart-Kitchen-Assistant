/**
 * A custom LIFO Stack implementation for storing Action objects (for Undo functionality).
 * Emulates the instructor's linked list stack pointer style.
 */
public class ActionStack {
    private ActionNode top; // matches sir's style
    private int size;

    public ActionStack() {
        this.top = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int size() {
        return size;
    }

    /**
     * Push operation.
     */
    public void push(Action data) {
        ActionNode node = new ActionNode(data);
        if (top == null) {
            top = node;
        } else {
            node.next = top;
            top = node;
        }
        size++;
    }

    /**
     * Pop operation.
     */
    public Action pop() {
        if (top == null) {
            System.out.println("Stack Underflow.");
            return null;
        }
        Action data = top.data;
        top = top.next;
        size--;
        return data;
    }

    /**
     * Peek operation.
     */
    public Action peek() {
        if (top == null) {
            return null;
        }
        return top.data;
    }

    public void clear() {
        top = null;
        size = 0;
    }

    /**
     * Traversal (converts stack to array for UI display).
     */
    public Action[] getAllActions() {
        Action[] list = new Action[size];
        ActionNode temp = top;
        int i = 0;
        while (temp != null) {
            list[i++] = temp.data;
            temp = temp.next;
        }
        return list;
    }
}
