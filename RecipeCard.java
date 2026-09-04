import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Class: RecipeCard
 *
 * Purpose:
 * Visual component representing a single Recipe in card layout.
 * Replaces old text-area/list views with rounded card containers, nutrition macro bars,
 * health badges, and action buttons.
 *
 * Fulfills Feature #12 (Recipe Cards) and GUI Requirements.
 */
public class RecipeCard extends RoundedPanel {
    private Recipe recipe;
    private boolean isCookable;
    private double matchPercentage;

    public RecipeCard(Recipe recipe, boolean isCookable, double matchPercentage, 
                      boolean isFavorite, ActionListener onViewDetails, 
                      ActionListener onToggleFavorite, ActionListener onCookRecipe) {
        super(16, UIUtils.COLOR_CARD_BG, true);
        this.recipe = recipe;
        this.isCookable = isCookable;
        this.matchPercentage = matchPercentage;

        setLayout(new BorderLayout(12, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        // -------------------------------------------------------------
        // TOP HEADER: Icon + Recipe Title + Match/Category Badges
        // -------------------------------------------------------------
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setOpaque(false);

        // Recipe Icon Loader
        JLabel iconLabel = new JLabel(UIUtils.getSafeIcon(recipe.name.toLowerCase().replace(" ", "_") + ".png", recipe.name, 48, 48));
        headerPanel.add(iconLabel, BorderLayout.WEST);

        // Title and Category Stack
        JPanel titleStack = new JPanel(new GridLayout(2, 1, 0, 2));
        titleStack.setOpaque(false);

        JLabel nameLabel = new JLabel(recipe.name);
        nameLabel.setFont(UIUtils.FONT_HEADER);
        nameLabel.setForeground(UIUtils.COLOR_TEXT_DARK);
        titleStack.add(nameLabel);

        JPanel badgeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        badgeRow.setOpaque(false);
        badgeRow.add(UIUtils.createBadge(recipe.category, UIUtils.COLOR_SAGE_LIGHT, UIUtils.COLOR_PRIMARY));

        if (matchPercentage > 0) {
            Color matchBg = matchPercentage >= 100.0 ? UIUtils.COLOR_PRIMARY : UIUtils.COLOR_ACCENT_AMBER;
            badgeRow.add(UIUtils.createBadge((int) matchPercentage + "% Match", matchBg, Color.WHITE));
        }

        if (recipe.isQuickSnack) {
            badgeRow.add(UIUtils.createBadge("⚡ " + recipe.prepTimeMinutes + "m Snack", UIUtils.COLOR_ACCENT_ORANGE, Color.WHITE));
        }

        titleStack.add(badgeRow);
        headerPanel.add(titleStack, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // -------------------------------------------------------------
        // CENTER: Nutrition Macro Pill Bar & Health Tags
        // -------------------------------------------------------------
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 6));
        centerPanel.setOpaque(false);

        // Macro Nutrients Pill Bar
        JPanel macroBar = new JPanel(new GridLayout(1, 4, 6, 0));
        macroBar.setOpaque(false);
        macroBar.add(createMacroPill("Calories", recipe.calories + " kcal", UIUtils.COLOR_ACCENT_ORANGE));
        macroBar.add(createMacroPill("Protein", recipe.protein + "g", UIUtils.COLOR_PRIMARY));
        macroBar.add(createMacroPill("Carbs", recipe.carbs + "g", UIUtils.COLOR_ACCENT_AMBER));
        macroBar.add(createMacroPill("Fat", recipe.fat + "g", UIUtils.COLOR_TEXT_MUTED));
        centerPanel.add(macroBar);

        // Health Goal Badges
        JPanel healthTagRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        healthTagRow.setOpaque(false);
        if (recipe.healthTags != null) {
            for (String tag : recipe.healthTags) {
                healthTagRow.add(UIUtils.createBadge("🌱 " + tag, new Color(0xF0, 0xEE, 0xE6), UIUtils.COLOR_TEXT_DARK));
            }
        }
        centerPanel.add(healthTagRow);

        add(centerPanel, BorderLayout.CENTER);

        // -------------------------------------------------------------
        // BOTTOM: Action Buttons (View, Favorite, Cook)
        // -------------------------------------------------------------
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonRow.setOpaque(false);

        JButton viewBtn = UIUtils.createStyledButton("View Recipe", new Color(0xEAE6DC), UIUtils.COLOR_TEXT_DARK);
        viewBtn.addActionListener(onViewDetails);
        buttonRow.add(viewBtn);

        String favText = isFavorite ? "★ Favorited" : "☆ Favorite";
        Color favColor = isFavorite ? UIUtils.COLOR_ACCENT_AMBER : new Color(0xEAE6DC);
        Color favFg = isFavorite ? Color.WHITE : UIUtils.COLOR_TEXT_DARK;
        JButton favBtn = UIUtils.createStyledButton(favText, favColor, favFg);
        favBtn.addActionListener(onToggleFavorite);
        buttonRow.add(favBtn);

        String cookText = isCookable ? "🍳 Cook Recipe" : "🔒 Missing Items";
        Color cookColor = isCookable ? UIUtils.COLOR_PRIMARY : new Color(0xC0, 0xBC, 0xB0);
        JButton cookBtn = UIUtils.createStyledButton(cookText, cookColor, Color.WHITE);
        cookBtn.setEnabled(isCookable);
        cookBtn.addActionListener(onCookRecipe);
        buttonRow.add(cookBtn);

        add(buttonRow, BorderLayout.SOUTH);
    }

    private JPanel createMacroPill(String title, String value, Color color) {
        JPanel pill = new JPanel(new GridLayout(2, 1, 0, 1));
        pill.setOpaque(true);
        pill.setBackground(new Color(0xF7, 0xF5, 0xEF));
        pill.setBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, color));

        JLabel tLbl = new JLabel("  " + title);
        tLbl.setFont(UIUtils.FONT_SMALL);
        tLbl.setForeground(UIUtils.COLOR_TEXT_MUTED);

        JLabel vLbl = new JLabel("  " + value);
        vLbl.setFont(UIUtils.FONT_BADGE);
        vLbl.setForeground(UIUtils.COLOR_TEXT_DARK);

        pill.add(tLbl);
        pill.add(vLbl);
        return pill;
    }

    public Recipe getRecipe() { return recipe; }
}
