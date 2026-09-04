import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class: MainFrame
 *
 * Purpose:
 * The primary application window for SALIKA (Smart Kitchen Management System).
 * Controls layout (CardLayout), navigation sidebar, interactive card views, modal dialogs,
 * and state synchronization across all modules.
 *
 * Design & Architecture:
 * - 100% Pure Java Swing with custom Graphics2D components (RoundedPanel, RecipeCard, IngredientCard).
 * - ZERO plain text / notepad views.
 * - Integrates custom DSA: RecipeBST, IngredientLinkedList, RecipeIngredientLinkedList,
 *   FavoriteLinkedList, ActionStack, NutritionTracker, SmartAdvisor, MealPlanner.
 * - Step-by-step inline comments provided for viva presentation.
 */
public class MainFrame extends JFrame {

    // Custom Data Structures & Memory Storage
    private IngredientLinkedList inventory;
    private RecipeBST recipeTree;
    private ActionStack actionStack;
    private FavoriteLinkedList favorites;
    private NutritionTracker nutritionTracker;
    private MealPlanner.DayPlan[] weeklyPlan;

    // Swing Layout & Card Navigation
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private String currentCardName = "Dashboard";

    // Active Category & Search Filters
    private String currentCategoryFilter = "All";
    private String currentHealthFilter = "All";
    private String currentExplorerQuery = "";

    public MainFrame() {
        // 1. Initialize DB & Data Structures
        DatabaseManager.initializeDatabase();

        inventory = new IngredientLinkedList();
        recipeTree = new RecipeBST();
        actionStack = new ActionStack();
        favorites = new FavoriteLinkedList();
        nutritionTracker = new NutritionTracker();

        DatabaseManager.loadIngredients(inventory);
        DatabaseManager.loadRecipes(recipeTree);
        DatabaseManager.loadFavorites(favorites, recipeTree);
        weeklyPlan = MealPlanner.generateWeeklyPlan(recipeTree, inventory);

        // 2. Setup Window Properties
        setTitle("SALIKA - Smart Kitchen Management System (DSA Semester Project)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1240, 760);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UIUtils.COLOR_BG);

        // 3. Create Navigation Sidebar (Left)
        JPanel sidebarPanel = createSidebar();
        add(sidebarPanel, BorderLayout.WEST);

        // 4. Create Main Content Card Area (Right)
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setOpaque(false);

        // Build & register panels
        refreshAllPanels();

        add(contentPanel, BorderLayout.CENTER);

        // 5. Create Status Footer Bar
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(0xE8, 0xE2, 0xD4));
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xD0, 0xC8, 0xB8)));
        JLabel statusLabel = new JLabel("  🟢 Backend Linked | Custom Data Structures Active (BST, Linked Lists, Stack) | Semester Project");
        statusLabel.setFont(UIUtils.FONT_SMALL);
        statusLabel.setForeground(UIUtils.COLOR_TEXT_DARK);
        statusBar.add(statusLabel, BorderLayout.WEST);
        add(statusBar, BorderLayout.SOUTH);

        // Show Dashboard on startup
        showPanel("Dashboard");
    }

    /**
     * Navigation Sidebar Builder
     */
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIUtils.COLOR_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(240, 760));
        sidebar.setBorder(new EmptyBorder(16, 12, 16, 12));

        // Brand Logo & Title Header
        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        brandPanel.setOpaque(false);
        JLabel logoLbl = new JLabel(UIUtils.getSafeIcon("app_logo.png", "SALIKA", 36, 36));
        brandPanel.add(logoLbl);

        JPanel textStack = new JPanel(new GridLayout(2, 1, 0, 1));
        textStack.setOpaque(false);
        JLabel titleLbl = new JLabel("SALIKA");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLbl.setForeground(Color.WHITE);
        textStack.add(titleLbl);

        JLabel subLbl = new JLabel("Smart Kitchen Assistant");
        subLbl.setFont(UIUtils.FONT_SMALL);
        subLbl.setForeground(new Color(0xAB, 0xC4, 0xB3));
        textStack.add(subLbl);

        brandPanel.add(textStack);
        sidebar.add(brandPanel);
        sidebar.add(Box.createVerticalStrut(20));

        // Sidebar Nav Buttons
        addNavButton(sidebar, " Dashboard", "Dashboard");
        addNavButton(sidebar, " Pantry Inventory", "Inventory");
        addNavButton(sidebar, " Ingredient Explorer", "Explorer");
        addNavButton(sidebar, " Recipe Catalog", "Catalog");
        addNavButton(sidebar, " Smart Advisor", "SmartAdvisor");
        addNavButton(sidebar, " Nutrition Tracker", "Nutrition");
        addNavButton(sidebar, " Weekly Meal Planner", "MealPlanner");
        addNavButton(sidebar, " Shopping List", "ShoppingList");
        addNavButton(sidebar, " Pantry Analytics", "Analytics");
        addNavButton(sidebar, " Favorites", "Favorites");
        addNavButton(sidebar, " Action History (Undo)", "Undo");

        sidebar.add(Box.createVerticalGlue());

        // Footer info in sidebar
        JLabel infoLbl = new JLabel("DSA Hero: Pure Java");
        infoLbl.setFont(UIUtils.FONT_SMALL);
        infoLbl.setForeground(new Color(0x8A, 0xA6, 0x93));
        infoLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(infoLbl);

        return sidebar;
    }

    private void addNavButton(JPanel sidebar, String label, String cardName) {
        JButton btn = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (currentCardName.equalsIgnoreCase(cardName)) {
                    g2.setColor(UIUtils.COLOR_PRIMARY);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(0x2F, 0x54, 0x3B));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(UIUtils.FONT_BODY_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(216, 40));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(8, 14, 8, 14));

        btn.addActionListener(e -> showPanel(cardName));

        sidebar.add(btn);
        sidebar.add(Box.createVerticalStrut(6));
    }

    private void showPanel(String name) {
        this.currentCardName = name;
        refreshAllPanels();
        cardLayout.show(contentPanel, name);
        repaint();
    }

    private void refreshAllPanels() {
        contentPanel.removeAll();
        contentPanel.add(createDashboardView(), "Dashboard");
        contentPanel.add(createInventoryView(), "Inventory");
        contentPanel.add(createExplorerView(), "Explorer");
        contentPanel.add(createCatalogView(), "Catalog");
        contentPanel.add(createSmartAdvisorView(), "SmartAdvisor");
        contentPanel.add(createNutritionView(), "Nutrition");
        contentPanel.add(createMealPlannerView(), "MealPlanner");
        contentPanel.add(createShoppingView(), "ShoppingList");
        contentPanel.add(createAnalyticsView(), "Analytics");
        contentPanel.add(createFavoritesView(), "Favorites");
        contentPanel.add(createUndoView(), "Undo");
        contentPanel.revalidate();
    }

    /**
     * Refreshes data but keeps the user on the same screen.
     * This prevents CardLayout from returning to the first card (Dashboard).
     */
    private void refreshCurrentPanel() {
        refreshAllPanels();
        cardLayout.show(contentPanel, currentCardName);
        contentPanel.repaint();
    }

    // =========================================================================
    // VIEW 1: DASHBOARD VIEW
    // =========================================================================
    private JPanel createDashboardView() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(16, 18, 16, 18));

        // Welcome Header Banner
        RoundedPanel headerCard = new RoundedPanel(16, UIUtils.COLOR_PRIMARY);
        headerCard.setLayout(new BorderLayout(12, 0));
        headerCard.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel headerText = new JPanel(new GridLayout(2, 1, 0, 4));
        headerText.setOpaque(false);
        JLabel title = new JLabel("Welcome to SALIKA Kitchen Dashboard ");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(Color.WHITE);
        headerText.add(title);

        JLabel sub = new JLabel("Real-time Kitchen Analytics, Recipe Recommendations & DSA Intelligence System");
        sub.setFont(UIUtils.FONT_BODY);
        sub.setForeground(new Color(0xE0, 0xF2, 0xE5));
        headerText.add(sub);
        headerCard.add(headerText, BorderLayout.WEST);

        panel.add(headerCard, BorderLayout.NORTH);

        // Main Dashboard Grid
        JPanel centerGrid = new JPanel(new GridLayout(2, 1, 16, 16));
        centerGrid.setOpaque(false);

        // Top Row: 4 Metric Cards + Kitchen Score Gauge
        JPanel topMetricsRow = new JPanel(new GridLayout(1, 5, 12, 0));
        topMetricsRow.setOpaque(false);

        Recommendation[] recs = RecommendationEngine.generateRecommendations(recipeTree, inventory);
        int readyCount = 0;
        for (int i = 0; i < recs.length; i++) {
            if (SmartAdvisor.isRecipeCookable(recs[i].recipe, inventory, null)) {
                readyCount++;
            }
        }

        int score = SmartAdvisor.calculateKitchenScore(recipeTree, inventory, nutritionTracker);

        topMetricsRow.add(createMetricCard("Kitchen Score", score + " / 100", "Overall Efficiency", UIUtils.COLOR_PRIMARY));
        topMetricsRow.add(createMetricCard("Ready Recipes", readyCount + " Cookable", "100% Core Items Owned", UIUtils.COLOR_ACCENT_ORANGE));
        topMetricsRow.add(createMetricCard("Pantry Items", inventory.size() + " Ingredients", "In Inventory List", UIUtils.COLOR_ACCENT_AMBER));
        topMetricsRow.add(createMetricCard("Favorites", favorites.size() + " Saved", "Custom Linked List", UIUtils.COLOR_PRIMARY_HOVER));
        topMetricsRow.add(createMetricCard("Calories Today", nutritionTracker.getCurrentCalories() + " / " + nutritionTracker.getTargetCalories(), "kcal Intake", UIUtils.COLOR_TEXT_DARK));

        centerGrid.add(topMetricsRow);

        // Bottom Row: Kitchen Assistant Advice + Featured Recipe Card
        JPanel bottomRow = new JPanel(new GridLayout(1, 2, 16, 0));
        bottomRow.setOpaque(false);

        // Kitchen Assistant Panel
        RoundedPanel assistantCard = new RoundedPanel(16, UIUtils.COLOR_CARD_BG);
        assistantCard.setLayout(new BorderLayout(12, 10));
        assistantCard.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel asstHeader = new JLabel("Kitchen Assistant Insights");
        asstHeader.setFont(UIUtils.FONT_HEADER);
        asstHeader.setForeground(UIUtils.COLOR_PRIMARY);
        assistantCard.add(asstHeader, BorderLayout.NORTH);

        JPanel msgBox = new JPanel(new GridLayout(3, 1, 0, 8));
        msgBox.setOpaque(false);

        msgBox.add(createAssistantBullet(" You can cook " + readyCount + " recipes right now with your pantry stock."));

        SmartAdvisor.UnlockInsight[] insights = SmartAdvisor.getTopUnlockInsights(recipeTree, inventory);
        if (insights.length > 0) {
            msgBox.add(createAssistantBullet(" Buying " + insights[0].ingredientName + " unlocks " + insights[0].unlockedCount + " recipes (" + insights[0].unlockedRecipeNames + ")!"));
        } else {
            msgBox.add(createAssistantBullet(" Pantry is well stocked! Try exploring new recipes in the catalog."));
        }

        msgBox.add(createAssistantBullet(nutritionTracker.getNutritionalAdvice()));
        assistantCard.add(msgBox, BorderLayout.CENTER);

        bottomRow.add(assistantCard);

        // Featured Recipe Card
        Recipe[] allRecipes = recipeTree.getAllRecipesSorted();
        Recipe featured = allRecipes.length > 0 ? allRecipes[0] : null;
        if (featured != null) {
            boolean isCookable = SmartAdvisor.isRecipeCookable(featured, inventory, null);
            RecipeCard featuredCard = new RecipeCard(featured, isCookable, 100.0, 
                favorites.find(featured.name) != null,
                e -> openRecipeDetailsDialog(featured),
                e -> toggleFavorite(featured),
                e -> cookRecipeAction(featured)
            );
            bottomRow.add(featuredCard);
        }

        centerGrid.add(bottomRow);
        panel.add(centerGrid, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMetricCard(String title, String value, String sub, Color accent) {
        RoundedPanel card = new RoundedPanel(14, UIUtils.COLOR_CARD_BG);
        card.setLayout(new GridLayout(3, 1, 0, 2));
        card.setBorder(new EmptyBorder(12, 14, 12, 14));

        JLabel tLbl = new JLabel(title);
        tLbl.setFont(UIUtils.FONT_SMALL);
        tLbl.setForeground(UIUtils.COLOR_TEXT_MUTED);

        JLabel vLbl = new JLabel(value);
        vLbl.setFont(UIUtils.FONT_HEADER);
        vLbl.setForeground(accent);

        JLabel sLbl = new JLabel(sub);
        sLbl.setFont(UIUtils.FONT_BADGE);
        sLbl.setForeground(UIUtils.COLOR_TEXT_DARK);

        card.add(tLbl);
        card.add(vLbl);
        card.add(sLbl);
        return card;
    }

    private JPanel createAssistantBullet(String text) {
        JPanel bullet = new JPanel(new BorderLayout(6, 0));
        bullet.setOpaque(false);
        JLabel lbl = new JLabel(text);
        lbl.setFont(UIUtils.FONT_BODY);
        lbl.setForeground(UIUtils.COLOR_TEXT_DARK);
        bullet.add(lbl, BorderLayout.CENTER);
        return bullet;
    }

    // =========================================================================
    // VIEW 2: PANTRY INVENTORY VIEW
    // =========================================================================
    private JPanel createInventoryView() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(16, 18, 16, 18));

        // Header bar with Add Ingredient Button
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JLabel title = new JLabel(" Pantry Inventory Management");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_DARK);
        topBar.add(title, BorderLayout.WEST);

        JButton addBtn = UIUtils.createStyledButton("+ Add New Ingredient", UIUtils.COLOR_PRIMARY, Color.WHITE);
        addBtn.addActionListener(e -> openAddIngredientDialog());
        topBar.add(addBtn, BorderLayout.EAST);

        panel.add(topBar, BorderLayout.NORTH);

        // Ingredient Cards Grid Container
        JPanel grid = new JPanel(new GridLayout(0, 2, 12, 12));
        grid.setOpaque(false);

        IngredientNode curr = inventory.getFirst();
        while (curr != null) {
            final Ingredient ing = curr.data;
            IngredientCard card = new IngredientCard(ing, true,
                e -> {
                    ing.isOwned = !ing.isOwned;
                    actionStack.push(new Action("TOGGLE_OWNED", ing.name, ing.quantity, ing.unit));
                    refreshCurrentPanel();
                },
                e -> {
                    ing.quantity += 1.0;
                    refreshCurrentPanel();
                },
                e -> {
                    if (ing.quantity > 1.0) {
                        ing.quantity -= 1.0;
                        refreshCurrentPanel();
                    }
                },
                e -> {
                    inventory.remove(ing.name);
                    actionStack.push(new Action("REMOVE_INGREDIENT", ing.name, ing.quantity, ing.unit));
                    refreshCurrentPanel();
                }
            );
            grid.add(card);
            curr = curr.next;
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // =========================================================================
    // VIEW 3: INGREDIENT EXPLORER VIEW (FEATURE 1)
    // =========================================================================
    private JPanel createExplorerView() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(16, 18, 16, 18));

        // Header & Search Bar
        JPanel searchBar = new JPanel(new BorderLayout(10, 0));
        searchBar.setOpaque(false);

        JLabel title = new JLabel("🔍 Ingredient Explorer (BST Search)");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_DARK);
        searchBar.add(title, BorderLayout.WEST);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        inputPanel.setOpaque(false);

        JTextField searchField = new JTextField(currentExplorerQuery, 16);
        searchField.setFont(UIUtils.FONT_BODY);
        inputPanel.add(searchField);

        JButton searchBtn = UIUtils.createStyledButton("Search Recipes", UIUtils.COLOR_PRIMARY, Color.WHITE);
        searchBtn.addActionListener(e -> {
            currentExplorerQuery = searchField.getText();
            refreshCurrentPanel();
        });
        inputPanel.add(searchBtn);

        searchBar.add(inputPanel, BorderLayout.EAST);
        panel.add(searchBar, BorderLayout.NORTH);

        // Search Results Cards Grid
        JPanel grid = new JPanel(new GridLayout(0, 2, 12, 12));
        grid.setOpaque(false);

        if (!currentExplorerQuery.trim().isEmpty()) {
            Recipe[] matches = SmartAdvisor.exploreIngredient(recipeTree, currentExplorerQuery);
            if (matches.length > 0) {
                for (int i = 0; i < matches.length; i++) {
                    final Recipe r = matches[i];
                    boolean isCookable = SmartAdvisor.isRecipeCookable(r, inventory, null);
                    RecipeCard card = new RecipeCard(r, isCookable, 100.0,
                        favorites.find(r.name) != null,
                        e -> openRecipeDetailsDialog(r),
                        e -> toggleFavorite(r),
                        e -> cookRecipeAction(r)
                    );
                    grid.add(card);
                }
            } else {
                JLabel noMatch = new JLabel("No recipes found containing ingredient: " + currentExplorerQuery);
                noMatch.setFont(UIUtils.FONT_HEADER);
                noMatch.setForeground(UIUtils.COLOR_TEXT_MUTED);
                grid.add(noMatch);
            }
        } else {
            JLabel prompt = new JLabel("Type an ingredient above (e.g. Chicken, Rice, Tomato, Eggs) to explore recipes containing it.");
            prompt.setFont(UIUtils.FONT_HEADER);
            prompt.setForeground(UIUtils.COLOR_TEXT_MUTED);
            grid.add(prompt);
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // =========================================================================
    // VIEW 4: RECIPE CATALOG & RECOMMENDATIONS
    // =========================================================================
    private JPanel createCatalogView() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(16, 18, 16, 18));

        // Header & Category Pills Bar
        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.setOpaque(false);

        JLabel title = new JLabel(" Recipe Catalog & Recommendation Engine");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_DARK);
        topPanel.add(title, BorderLayout.NORTH);

        JPanel pillsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pillsRow.setOpaque(false);

        String[] categories = {"All", "Main Course", "Breakfast", "Quick Snacks", "Healthy", "High Protein", "Low Carb", "Keto", "Desserts", "Drinks"};
        for (int c = 0; c < categories.length; c++) {
            final String cat = categories[c];
            JButton pillBtn = UIUtils.createStyledButton(cat, 
                currentCategoryFilter.equalsIgnoreCase(cat) ? UIUtils.COLOR_PRIMARY : new Color(0xEAE6DC),
                currentCategoryFilter.equalsIgnoreCase(cat) ? Color.WHITE : UIUtils.COLOR_TEXT_DARK
            );
            pillBtn.addActionListener(e -> {
                currentCategoryFilter = cat;
                refreshCurrentPanel();
            });
            pillsRow.add(pillBtn);
        }

        topPanel.add(pillsRow, BorderLayout.SOUTH);
        panel.add(topPanel, BorderLayout.NORTH);

        // Recipe Cards Grid
        JPanel grid = new JPanel(new GridLayout(0, 2, 12, 12));
        grid.setOpaque(false);

        Recommendation[] recs = RecommendationEngine.generateRecommendations(recipeTree, inventory);
        recs = RecommendationEngine.filterByHealthGoal(recs, currentCategoryFilter);

        if (recs.length == 0) {
            JLabel empty = new JLabel("No recipes found for the selected category.");
            empty.setFont(UIUtils.FONT_HEADER);
            empty.setForeground(UIUtils.COLOR_TEXT_MUTED);
            grid.add(empty);
        }

        for (int i = 0; i < recs.length; i++) {
            final Recommendation rec = recs[i];
            final Recipe r = rec.recipe;
            boolean isCookable = SmartAdvisor.isRecipeCookable(r, inventory, null);
            RecipeCard card = new RecipeCard(r, isCookable, rec.matchPercentage,
                favorites.find(r.name) != null,
                e -> openRecipeDetailsDialog(r),
                e -> toggleFavorite(r),
                e -> cookRecipeAction(r)
            );
            grid.add(card);
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // =========================================================================
    // VIEW 5: SMART ADVISOR VIEW (FEATURE 2)
    // =========================================================================
    private JPanel createSmartAdvisorView() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel title = new JLabel(" Smart Shopping Advisor Insights");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_DARK);
        panel.add(title, BorderLayout.NORTH);

        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setOpaque(false);

        SmartAdvisor.UnlockInsight[] insights = SmartAdvisor.getTopUnlockInsights(recipeTree, inventory);

        if (insights.length > 0) {
            for (int i = 0; i < insights.length; i++) {
                final SmartAdvisor.UnlockInsight insight = insights[i];
                RoundedPanel card = new RoundedPanel(14, UIUtils.COLOR_CARD_BG, true);
                card.setLayout(new BorderLayout(12, 0));
                card.setBorder(new EmptyBorder(12, 16, 12, 16));
                card.setMaximumSize(new Dimension(1000, 70));

                JPanel left = new JPanel(new GridLayout(2, 1, 0, 2));
                left.setOpaque(false);
                JLabel ingName = new JLabel(" Buying " + insight.ingredientName + " Unlocks " + insight.unlockedCount + " Recipes!");
                ingName.setFont(UIUtils.FONT_HEADER);
                ingName.setForeground(UIUtils.COLOR_PRIMARY);
                left.add(ingName);

                JLabel recList = new JLabel("Unlocked Recipes: " + insight.unlockedRecipeNames);
                recList.setFont(UIUtils.FONT_BODY);
                recList.setForeground(UIUtils.COLOR_TEXT_MUTED);
                left.add(recList);

                card.add(left, BorderLayout.CENTER);

                JButton addShopBtn = UIUtils.createStyledButton("+ Add to Shopping List", UIUtils.COLOR_ACCENT_ORANGE, Color.WHITE);
                addShopBtn.addActionListener(e -> {
                    Ingredient item = inventory.find(insight.ingredientName);
                    if (item == null) {
                        inventory.add(new Ingredient(insight.ingredientName, 1, "kg"));
                    }
                    actionStack.push(new Action("ADD_SHOPPING", insight.ingredientName, 1, "kg"));
                    JOptionPane.showMessageDialog(this, insight.ingredientName + " added to your Shopping List!");
                    refreshCurrentPanel();
                });
                card.add(addShopBtn, BorderLayout.EAST);

                listContainer.add(card);
                listContainer.add(Box.createVerticalStrut(10));
            }
        } else {
            JLabel empty = new JLabel("All key core ingredients are in stock! You can cook all recipes.");
            empty.setFont(UIUtils.FONT_HEADER);
            empty.setForeground(UIUtils.COLOR_TEXT_MUTED);
            listContainer.add(empty);
        }

        JScrollPane scroll = new JScrollPane(listContainer);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // =========================================================================
    // VIEW 6: NUTRITION TRACKER VIEW (FEATURE 6 & 7)
    // =========================================================================
    private JPanel createNutritionView() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(16, 18, 16, 18));

        // Header
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JLabel title = new JLabel("Daily Nutrition Tracker & Macro Progress");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_DARK);
        topBar.add(title, BorderLayout.WEST);

        JButton resetBtn = UIUtils.createStyledButton("Reset Daily Totals", UIUtils.COLOR_ACCENT_ORANGE, Color.WHITE);
        resetBtn.addActionListener(e -> {
            nutritionTracker.resetDailyTotals();
            refreshCurrentPanel();
        });
        topBar.add(resetBtn, BorderLayout.EAST);

        panel.add(topBar, BorderLayout.NORTH);

        // Progress Bars & Deficiency Advice Grid
        JPanel mainGrid = new JPanel(new GridLayout(2, 1, 14, 14));
        mainGrid.setOpaque(false);

        // 4 Macro Progress Bars Card
        RoundedPanel progressCard = new RoundedPanel(16, UIUtils.COLOR_CARD_BG);
        progressCard.setLayout(new GridLayout(4, 1, 0, 10));
        progressCard.setBorder(new EmptyBorder(14, 18, 14, 18));

        progressCard.add(createProgressBarRow("Calories Intake", nutritionTracker.getCurrentCalories(), nutritionTracker.getTargetCalories(), "kcal", nutritionTracker.getCaloriePercentage(), UIUtils.COLOR_ACCENT_ORANGE));
        progressCard.add(createProgressBarRow("Protein Intake", (int) nutritionTracker.getCurrentProtein(), (int) nutritionTracker.getTargetProtein(), "g", nutritionTracker.getProteinPercentage(), UIUtils.COLOR_PRIMARY));
        progressCard.add(createProgressBarRow("Carbohydrates", (int) nutritionTracker.getCurrentCarbs(), (int) nutritionTracker.getTargetCarbs(), "g", nutritionTracker.getCarbPercentage(), UIUtils.COLOR_ACCENT_AMBER));
        progressCard.add(createProgressBarRow("Fats Intake", (int) nutritionTracker.getCurrentFat(), (int) nutritionTracker.getTargetFat(), "g", nutritionTracker.getFatPercentage(), UIUtils.COLOR_TEXT_MUTED));

        mainGrid.add(progressCard);

        // Smart Assistant Advice Card
        RoundedPanel adviceCard = new RoundedPanel(16, UIUtils.COLOR_SAGE_LIGHT);
        adviceCard.setLayout(new BorderLayout(12, 8));
        adviceCard.setBorder(new EmptyBorder(14, 18, 14, 18));

        JLabel advHeader = new JLabel("Smart Nutrition Assistant Recommendation");
        advHeader.setFont(UIUtils.FONT_HEADER);
        advHeader.setForeground(UIUtils.COLOR_PRIMARY);
        adviceCard.add(advHeader, BorderLayout.NORTH);

        JLabel advText = new JLabel(nutritionTracker.getNutritionalAdvice());
        advText.setFont(UIUtils.FONT_BODY_BOLD);
        advText.setForeground(UIUtils.COLOR_TEXT_DARK);
        adviceCard.add(advText, BorderLayout.CENTER);

        mainGrid.add(adviceCard);

        panel.add(mainGrid, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProgressBarRow(String label, int current, int target, String unit, int percent, Color barColor) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);

        JLabel name = new JLabel(label + ": " + current + " / " + target + " " + unit);
        name.setFont(UIUtils.FONT_BODY_BOLD);
        name.setForeground(UIUtils.COLOR_TEXT_DARK);
        name.setPreferredSize(new Dimension(240, 24));
        row.add(name, BorderLayout.WEST);

        JProgressBar pBar = new JProgressBar(0, 100);
        pBar.setValue(percent);
        pBar.setForeground(barColor);
        pBar.setBackground(new Color(0xEE, 0xEA, 0xE0));
        pBar.setStringPainted(true);
        pBar.setFont(UIUtils.FONT_BADGE);
        row.add(pBar, BorderLayout.CENTER);

        return row;
    }

    // =========================================================================
    // VIEW 7: WEEKLY MEAL PLANNER (BONUS FEATURE)
    // =========================================================================
    private JPanel createMealPlannerView() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(16, 18, 16, 18));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        JLabel title = new JLabel("Editable Weekly Meal Planner");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_DARK);
        topBar.add(title, BorderLayout.WEST);

        JButton regenerateBtn = UIUtils.createStyledButton("Generate New Week", UIUtils.COLOR_PRIMARY, Color.WHITE);
        regenerateBtn.addActionListener(e -> {
            weeklyPlan = MealPlanner.generateWeeklyPlan(recipeTree, inventory);
            refreshCurrentPanel();
        });
        topBar.add(regenerateBtn, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 2, 12, 12));
        grid.setOpaque(false);

        for (int i = 0; i < weeklyPlan.length; i++) {
            final int dayIndex = i;
            MealPlanner.DayPlan dp = weeklyPlan[i];
            RoundedPanel card = new RoundedPanel(14, UIUtils.COLOR_CARD_BG);
            card.setLayout(new BorderLayout(0, 8));
            card.setBorder(new EmptyBorder(12, 14, 12, 14));

            JLabel dayLbl = new JLabel( " Menu (" + dp.getTotalCalories() + " kcal)");
            dayLbl.setFont(UIUtils.FONT_HEADER);
            dayLbl.setForeground(UIUtils.COLOR_PRIMARY);
            card.add(dayLbl, BorderLayout.NORTH);

            JPanel meals = new JPanel(new GridLayout(4, 1, 0, 5));
            meals.setOpaque(false);
            meals.add(createMealRow(dayIndex, "Breakfast", dp.breakfast));
            meals.add(createMealRow(dayIndex, "Lunch", dp.lunch));
            meals.add(createMealRow(dayIndex, "Dinner", dp.dinner));
            meals.add(createMealRow(dayIndex, "Snack", dp.snack));
            card.add(meals, BorderLayout.CENTER);
            grid.add(card);
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMealRow(int dayIndex, String mealType, Recipe recipe) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        JLabel label = new JLabel(mealType + ": " + (recipe != null ? recipe.name : "Not selected"));
        label.setFont(UIUtils.FONT_BODY);
        row.add(label, BorderLayout.CENTER);

        JButton changeBtn = UIUtils.createStyledButton("Change", new Color(0xEAE6DC), UIUtils.COLOR_TEXT_DARK);
        changeBtn.addActionListener(e -> changeMeal(dayIndex, mealType));
        row.add(changeBtn, BorderLayout.EAST);
        return row;
    }

    private void changeMeal(int dayIndex, String mealType) {
        Recipe[] recipes = recipeTree.getAllRecipesSorted();
        String[] names = new String[recipes.length + 1];
        names[0] = "Clear Meal";
        for (int i = 0; i < recipes.length; i++) names[i + 1] = recipes[i].name;

        String selected = (String) JOptionPane.showInputDialog(this, "Choose a recipe:",
                "Change " + mealType, JOptionPane.PLAIN_MESSAGE, null, names, names[0]);
        if (selected == null) return;

        Recipe chosen = null;
        if (!selected.equals("Clear Meal")) chosen = recipeTree.searchRecipe(selected);
        MealPlanner.DayPlan day = weeklyPlan[dayIndex];
        if (mealType.equals("Breakfast")) day.breakfast = chosen;
        else if (mealType.equals("Lunch")) day.lunch = chosen;
        else if (mealType.equals("Dinner")) day.dinner = chosen;
        else day.snack = chosen;
        refreshCurrentPanel();
    }

    // =========================================================================
    // VIEW 8: SHOPPING LIST VIEW
    // =========================================================================
    private JPanel createShoppingView() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel title = new JLabel(" Smart Shopping List");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_DARK);
        panel.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 2, 12, 12));
        grid.setOpaque(false);

        IngredientNode curr = inventory.getFirst();
        while (curr != null) {
            final Ingredient ing = curr.data;
            if (!ing.isOwned) {
                IngredientCard card = new IngredientCard(ing, false, null, null, null,
                    e -> {
                        ing.isOwned = true; // Mark bought
                        actionStack.push(new Action("MARK_BOUGHT", ing.name, ing.quantity, ing.unit));
                        refreshCurrentPanel();
                    }
                );
                grid.add(card);
            }
            curr = curr.next;
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // =========================================================================
    // VIEW 9: PANTRY ANALYTICS VIEW
    // =========================================================================
    private JPanel createAnalyticsView() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel title = new JLabel("Pantry & Recipe Analytics");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_DARK);
        panel.add(title, BorderLayout.NORTH);

        String[] analytics = calculatePantryAnalytics();
        JPanel grid = new JPanel(new GridLayout(2, 3, 14, 14));
        grid.setOpaque(false);
        grid.add(createMetricCard("Most Used Ingredient", analytics[0], analytics[1], UIUtils.COLOR_PRIMARY));
        grid.add(createMetricCard("Most Missing Ingredient", analytics[2], analytics[3], UIUtils.COLOR_ACCENT_ORANGE));
        grid.add(createMetricCard("Recipes Ready", analytics[4], "Can be cooked now", UIUtils.COLOR_PRIMARY_HOVER));
        grid.add(createMetricCard("Owned Pantry Items", analytics[5], "Custom linked list traversal", UIUtils.COLOR_ACCENT_AMBER));
        grid.add(createMetricCard("Total Recipes in BST", recipeTree.getCount() + "", "In-order BST storage", UIUtils.COLOR_PRIMARY));
        grid.add(createMetricCard("Best Item to Buy", analytics[6], analytics[7], UIUtils.COLOR_ACCENT_ORANGE));
        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    private String[] calculatePantryAnalytics() {
        Recipe[] recipes = recipeTree.getAllRecipesSorted();
        String[] names = new String[200];
        int[] usedCount = new int[200];
        int[] missingCount = new int[200];
        int totalNames = 0;
        int readyRecipes = 0;

        for (int i = 0; i < recipes.length; i++) {
            if (SmartAdvisor.isRecipeCookable(recipes[i], inventory, null)) readyRecipes++;
            RecipeIngredientNode node = recipes[i].requiredIngredients.getFirst();
            while (node != null) {
                String ingredientName = node.data.ingredientName;
                int position = -1;
                for (int j = 0; j < totalNames; j++) {
                    if (names[j].equalsIgnoreCase(ingredientName)) position = j;
                }
                if (position == -1 && totalNames < names.length) {
                    position = totalNames;
                    names[totalNames] = ingredientName;
                    totalNames++;
                }
                if (position != -1) {
                    usedCount[position]++;
                    Ingredient item = inventory.find(ingredientName);
                    if (item == null || !item.isOwned || item.quantity < node.data.quantity) missingCount[position]++;
                }
                node = node.next;
            }
        }

        int mostUsed = 0;
        int mostMissing = 0;
        for (int i = 1; i < totalNames; i++) {
            if (usedCount[i] > usedCount[mostUsed]) mostUsed = i;
            if (missingCount[i] > missingCount[mostMissing]) mostMissing = i;
        }

        int owned = 0;
        IngredientNode ing = inventory.getFirst();
        while (ing != null) {
            if (ing.data.isOwned) owned++;
            ing = ing.next;
        }

        String usedName = totalNames > 0 ? names[mostUsed] : "No data";
        String missingName = totalNames > 0 ? names[mostMissing] : "No data";
        int usedValue = totalNames > 0 ? usedCount[mostUsed] : 0;
        int missingValue = totalNames > 0 ? missingCount[mostMissing] : 0;
        return new String[]{usedName, "Used in " + usedValue + " recipes", missingName,
                "Missing from " + missingValue + " recipes", readyRecipes + "", owned + "",
                missingName, "Could help " + missingValue + " recipes"};
    }

    // =========================================================================
    // VIEW 10: FAVORITES VIEW
    // =========================================================================
    private JPanel createFavoritesView() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel title = new JLabel("Favorite Saved Recipes");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_DARK);
        panel.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 2, 12, 12));
        grid.setOpaque(false);

        FavoriteNode curr = favorites.getFirst();
        if (curr == null) {
            JLabel empty = new JLabel("No favorite recipes yet. Add favorites from the Recipe Catalog.");
            empty.setFont(UIUtils.FONT_HEADER);
            empty.setForeground(UIUtils.COLOR_TEXT_MUTED);
            grid.add(empty);
        }
        while (curr != null) {
            final Recipe r = curr.data;
            boolean isCookable = SmartAdvisor.isRecipeCookable(r, inventory, null);
            RecipeCard card = new RecipeCard(r, isCookable, 100.0, true,
                e -> openRecipeDetailsDialog(r),
                e -> toggleFavorite(r),
                e -> cookRecipeAction(r)
            );
            grid.add(card);
            curr = curr.next;
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // =========================================================================
    // VIEW 11: ACTION HISTORY & UNDO VIEW (STACK DSA)
    // =========================================================================
    private JPanel createUndoView() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(16, 18, 16, 18));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JLabel title = new JLabel(" Action Stack & Step-by-Step Undo");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_DARK);
        topBar.add(title, BorderLayout.WEST);

        JButton undoBtn = UIUtils.createStyledButton("Undo Last Action", UIUtils.COLOR_ACCENT_ORANGE, Color.WHITE);
        undoBtn.addActionListener(e -> performUndo());
        topBar.add(undoBtn, BorderLayout.EAST);

        panel.add(topBar, BorderLayout.NORTH);

        // Action Stack Items Timeline Container
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);

        Action[] actions = actionStack.getAllActions();
        if (actions.length > 0) {
            for (int i = 0; i < actions.length; i++) {
                Action act = actions[i];
                RoundedPanel card = new RoundedPanel(12, UIUtils.COLOR_CARD_BG);
                card.setLayout(new BorderLayout());
                card.setBorder(new EmptyBorder(10, 14, 10, 14));
                card.setMaximumSize(new Dimension(1000, 48));

                JLabel lbl = new JLabel("Action #" + (actions.length - i) + ": " + act.description);
                lbl.setFont(UIUtils.FONT_BODY_BOLD);
                lbl.setForeground(UIUtils.COLOR_TEXT_DARK);
                card.add(lbl, BorderLayout.WEST);

                list.add(card);
                list.add(Box.createVerticalStrut(8));
            }
        } else {
            JLabel empty = new JLabel("Stack is currently empty. Perform actions (cooking, toggling items) to populate undo history.");
            empty.setFont(UIUtils.FONT_HEADER);
            empty.setForeground(UIUtils.COLOR_TEXT_MUTED);
            list.add(empty);
        }

        JScrollPane scroll = new JScrollPane(list);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // =========================================================================
    // HELPER DIALOGS & ACTIONS
    // =========================================================================
    private void openRecipeDetailsDialog(Recipe recipe) {
        JDialog dialog = new JDialog(this, recipe.name + " - Recipe Details", true);
        dialog.setSize(720, 620);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(UIUtils.COLOR_BG);

        RoundedPanel header = new RoundedPanel(0, UIUtils.COLOR_PRIMARY);
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(18, 22, 18, 22));
        JLabel nameLbl = new JLabel(recipe.name);
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 25));
        nameLbl.setForeground(Color.WHITE);
        header.add(nameLbl, BorderLayout.NORTH);
        JLabel metaLbl = new JLabel(recipe.category + "   •   " + recipe.prepTimeMinutes + " mins   •   " + recipe.difficulty);
        metaLbl.setFont(UIUtils.FONT_BODY_BOLD);
        metaLbl.setForeground(new Color(0xE0, 0xF2, 0xE5));
        header.add(metaLbl, BorderLayout.SOUTH);
        dialog.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(16, 18, 16, 18));
        content.setBackground(UIUtils.COLOR_BG);

        JPanel nutrition = new JPanel(new GridLayout(1, 4, 10, 0));
        nutrition.setOpaque(false);
        nutrition.add(createSmallInfoCard("Calories", recipe.calories + " kcal"));
        nutrition.add(createSmallInfoCard("Protein", recipe.protein + " g"));
        nutrition.add(createSmallInfoCard("Carbs", recipe.carbs + " g"));
        nutrition.add(createSmallInfoCard("Fat", recipe.fat + " g"));
        nutrition.setMaximumSize(new Dimension(680, 78));
        content.add(nutrition);
        content.add(Box.createVerticalStrut(14));

        RoundedPanel ingredientCard = new RoundedPanel(14, UIUtils.COLOR_CARD_BG);
        ingredientCard.setLayout(new BoxLayout(ingredientCard, BoxLayout.Y_AXIS));
        ingredientCard.setBorder(new EmptyBorder(14, 16, 14, 16));
        JLabel reqHeader = new JLabel("Required Ingredients");
        reqHeader.setFont(UIUtils.FONT_HEADER);
        reqHeader.setForeground(UIUtils.COLOR_TEXT_DARK);
        ingredientCard.add(reqHeader);
        ingredientCard.add(Box.createVerticalStrut(8));

        RecipeIngredientNode temp = recipe.requiredIngredients.getFirst();
        while (temp != null) {
            Ingredient invItem = inventory.find(temp.data.ingredientName);
            boolean owned = invItem != null && invItem.isOwned && invItem.quantity >= temp.data.quantity;
            JLabel ingLbl = new JLabel((owned ? "✓ " : "✕ ") + temp.data.toString() + (owned ? "  In stock" : "  Missing"));
            ingLbl.setFont(UIUtils.FONT_BODY);
            ingLbl.setForeground(owned ? UIUtils.COLOR_PRIMARY : UIUtils.COLOR_ACCENT_ORANGE);
            ingredientCard.add(ingLbl);
            ingredientCard.add(Box.createVerticalStrut(4));
            temp = temp.next;
        }
        ingredientCard.setMaximumSize(new Dimension(680, ingredientCard.getPreferredSize().height));
        content.add(ingredientCard);
        content.add(Box.createVerticalStrut(14));

        RoundedPanel instructionCard = new RoundedPanel(14, UIUtils.COLOR_CARD_BG);
        instructionCard.setLayout(new BorderLayout(0, 8));
        instructionCard.setBorder(new EmptyBorder(14, 16, 14, 16));
        JLabel instHeader = new JLabel("Cooking Instructions");
        instHeader.setFont(UIUtils.FONT_HEADER);
        instHeader.setForeground(UIUtils.COLOR_TEXT_DARK);
        instructionCard.add(instHeader, BorderLayout.NORTH);
        JTextArea instArea = new JTextArea(recipe.instructions);
        instArea.setFont(UIUtils.FONT_BODY);
        instArea.setWrapStyleWord(true);
        instArea.setLineWrap(true);
        instArea.setEditable(false);
        instArea.setOpaque(false);
        instructionCard.add(instArea, BorderLayout.CENTER);
        instructionCard.setMaximumSize(new Dimension(680, Math.max(150, instructionCard.getPreferredSize().height)));
        content.add(instructionCard);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(UIUtils.COLOR_BG);
        dialog.add(scroll, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        actions.setBackground(UIUtils.COLOR_BG);
        JButton favoriteBtn = UIUtils.createStyledButton(favorites.find(recipe.name) != null ? "Remove Favorite" : "Add Favorite", UIUtils.COLOR_ACCENT_AMBER, Color.WHITE);
        favoriteBtn.addActionListener(e -> { toggleFavorite(recipe); dialog.dispose(); });
        JButton cookBtn = UIUtils.createStyledButton("Cook Recipe", UIUtils.COLOR_PRIMARY, Color.WHITE);
        cookBtn.addActionListener(e -> { cookRecipeAction(recipe); dialog.dispose(); });
        JButton closeBtn = UIUtils.createStyledButton("Close", new Color(0x777777), Color.WHITE);
        closeBtn.addActionListener(e -> dialog.dispose());
        actions.add(favoriteBtn);
        actions.add(cookBtn);
        actions.add(closeBtn);
        dialog.add(actions, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private JPanel createSmallInfoCard(String label, String value) {
        RoundedPanel card = new RoundedPanel(12, UIUtils.COLOR_CARD_BG);
        card.setLayout(new GridLayout(2, 1, 0, 2));
        card.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(UIUtils.FONT_HEADER);
        valueLabel.setForeground(UIUtils.COLOR_PRIMARY);
        JLabel nameLabel = new JLabel(label, SwingConstants.CENTER);
        nameLabel.setFont(UIUtils.FONT_SMALL);
        nameLabel.setForeground(UIUtils.COLOR_TEXT_MUTED);
        card.add(valueLabel);
        card.add(nameLabel);
        return card;
    }

    private void openAddIngredientDialog() {
        JDialog dialog = new JDialog(this, "Add New Pantry Ingredient", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));

        JTextField nameF = new JTextField();
        JTextField qtyF = new JTextField("1.0");
        JTextField unitF = new JTextField("kg");
        JCheckBox ownedC = new JCheckBox("In Pantry Stock", true);

        dialog.add(new JLabel(" Ingredient Name:"));
        dialog.add(nameF);
        dialog.add(new JLabel(" Quantity:"));
        dialog.add(qtyF);
        dialog.add(new JLabel(" Unit (kg, g, pcs, liter):"));
        dialog.add(unitF);
        dialog.add(new JLabel(" Status:"));
        dialog.add(ownedC);

        JButton saveBtn = UIUtils.createStyledButton("Save Ingredient", UIUtils.COLOR_PRIMARY, Color.WHITE);
        saveBtn.addActionListener(e -> {
            String name = nameF.getText().trim();
            if (!name.isEmpty()) {
                double q = Double.parseDouble(qtyF.getText());
                Ingredient newIng = new Ingredient(name, q, unitF.getText().trim());
                newIng.isOwned = ownedC.isSelected();
                inventory.add(newIng);
                actionStack.push(new Action("ADD_INGREDIENT", name, q, unitF.getText().trim()));
                dialog.dispose();
                refreshCurrentPanel();
            }
        });
        dialog.add(saveBtn);
        dialog.setVisible(true);
    }

    private void toggleFavorite(Recipe recipe) {
        if (favorites.find(recipe.name) != null) {
            favorites.remove(recipe.name);
        } else {
            favorites.add(recipe);
        }
        refreshCurrentPanel();
    }

    private void cookRecipeAction(Recipe recipe) {
        nutritionTracker.logCookedRecipe(recipe);
        actionStack.push(new Action("COOK_RECIPE", recipe.name, recipe.calories, "kcal"));
        JOptionPane.showMessageDialog(this, "🎉 Cooked " + recipe.name + "! Logged " + recipe.calories + " kcal & " + recipe.protein + "g protein to your daily tracker.");
        refreshCurrentPanel();
    }

    private void performUndo() {
        if (actionStack.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Stack is empty! No actions to undo.");
            return;
        }

        Action lastAction = actionStack.pop();
        if (lastAction.type.equalsIgnoreCase("COOK_RECIPE")) {
            Recipe r = recipeTree.searchRecipe(lastAction.targetName);
            if (r != null) {
                nutritionTracker.unlogCookedRecipe(r);
            }
        } else if (lastAction.type.equalsIgnoreCase("TOGGLE_OWNED")) {
            Ingredient item = inventory.find(lastAction.targetName);
            if (item != null) item.isOwned = !item.isOwned;
        } else if (lastAction.type.equalsIgnoreCase("MARK_BOUGHT")) {
            Ingredient item = inventory.find(lastAction.targetName);
            if (item != null) item.isOwned = false;
        } else if (lastAction.type.equalsIgnoreCase("ADD_INGREDIENT")) {
            inventory.remove(lastAction.targetName);
        }

        JOptionPane.showMessageDialog(this, "Undid Action: " + lastAction.description);
        refreshCurrentPanel();
    }

    // Application Launch Entry Point
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new MainFrame().setVisible(true);
        });
    }
}
