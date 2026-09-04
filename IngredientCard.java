import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Class: IngredientCard
 *
 * Purpose:
 * Visual component representing an Ingredient in Pantry Inventory or Shopping List.
 * Replaces plain text notepad displays with interactive cards featuring owned status toggles,
 * quantity adjusters (+ / -), and unit labels.
 */
public class IngredientCard extends RoundedPanel {
    private Ingredient ingredient;

    public IngredientCard(Ingredient ingredient, boolean showOwnedToggle, 
                          ActionListener onToggleOwned, 
                          ActionListener onIncrementQty, 
                          ActionListener onDecrementQty, 
                          ActionListener onDelete) {
        super(12, UIUtils.COLOR_CARD_BG, true);
        this.ingredient = ingredient;

        setLayout(new BorderLayout(10, 0));
        setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        // LEFT: Icon + Name
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        leftPanel.setOpaque(false);

        JLabel iconLabel = new JLabel(UIUtils.getSafeIcon(ingredient.name.toLowerCase().replace(" ", "_") + ".png", ingredient.name, 36, 36));
        leftPanel.add(iconLabel);

        JPanel textStack = new JPanel(new GridLayout(2, 1, 0, 1));
        textStack.setOpaque(false);

        JLabel nameLabel = new JLabel(ingredient.name);
        nameLabel.setFont(UIUtils.FONT_SUBHEADER);
        nameLabel.setForeground(UIUtils.COLOR_TEXT_DARK);
        textStack.add(nameLabel);

        JLabel qtyLabel = new JLabel(ingredient.quantity + " " + ingredient.unit);
        qtyLabel.setFont(UIUtils.FONT_BODY);
        qtyLabel.setForeground(UIUtils.COLOR_TEXT_MUTED);
        textStack.add(qtyLabel);

        leftPanel.add(textStack);
        add(leftPanel, BorderLayout.WEST);

        // RIGHT: Controls (Owned Checkbox, Qty +, -, Delete)
        JPanel rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        rightControls.setOpaque(false);

        if (showOwnedToggle) {
            JCheckBox ownedBox = new JCheckBox("In Pantry", ingredient.isOwned);
            ownedBox.setFont(UIUtils.FONT_BODY_BOLD);
            ownedBox.setForeground(ingredient.isOwned ? UIUtils.COLOR_PRIMARY : UIUtils.COLOR_TEXT_MUTED);
            ownedBox.setOpaque(false);
            ownedBox.setFocusPainted(false);
            ownedBox.addActionListener(onToggleOwned);
            rightControls.add(ownedBox);
        }

        if (onDecrementQty != null) {
            JButton decBtn = UIUtils.createStyledButton("-", new Color(0xEE, 0xEA, 0xE0), UIUtils.COLOR_TEXT_DARK);
            decBtn.setMargin(new Insets(2, 6, 2, 6));
            decBtn.addActionListener(onDecrementQty);
            rightControls.add(decBtn);
        }

        if (onIncrementQty != null) {
            JButton incBtn = UIUtils.createStyledButton("+", new Color(0xEE, 0xEA, 0xE0), UIUtils.COLOR_TEXT_DARK);
            incBtn.setMargin(new Insets(2, 6, 2, 6));
            incBtn.addActionListener(onIncrementQty);
            rightControls.add(incBtn);
        }

        if (onDelete != null) {
            JButton delBtn = UIUtils.createStyledButton("✕", UIUtils.COLOR_ACCENT_ORANGE, Color.WHITE);
            delBtn.setMargin(new Insets(2, 6, 2, 6));
            delBtn.addActionListener(onDelete);
            rightControls.add(delBtn);
        }

        add(rightControls, BorderLayout.EAST);
    }

    public Ingredient getIngredient() { return ingredient; }
}
