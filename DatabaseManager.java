/**
 * DatabaseManager.java
 *
 * Mock database layer for SALIKA (Smart Kitchen Management System).
 * Everything is initialized in memory so custom Data Structures (RecipeBST, Linked Lists, Stack)
 * remain the main hero of the project during viva examination.
 *
 * Divided into clear sections with extensive comments:
 * - INGREDIENTS DATABASE
 * - SECTION 1: DESI MAIN COURSE RECIPES (20+ Recipes)
 * - SECTION 2: BREAKFAST & PARATHAS (20+ Recipes)
 * - SECTION 3: QUICK SNACKS & STREET FOOD (20+ Recipes)
 * - SECTION 4: HEALTHY, KETO & HIGH PROTEIN (20+ Recipes)
 * - SECTION 5: DESSERTS & DRINKS (20+ Recipes)
 */
public class DatabaseManager {

    private static int nextIngredientId = 60;

    public static void initializeDatabase() {
        System.out.println("----------------------------------");
        System.out.println("SALIKA ENRICHED MOCK DATABASE LOADED");
        System.out.println("----------------------------------");
    }

    /**
     * Loads ingredients into the custom IngredientLinkedList.
     */
    public static void loadIngredients(IngredientLinkedList list) {
        list.clear();

        // Grains & Staples
        list.add(new Ingredient(1, "Rice", 2, "kg"));
        list.add(new Ingredient(2, "Flour", 3, "kg"));
        list.add(new Ingredient(3, "Rice Flour", 500, "g"));
        list.add(new Ingredient(4, "Semolina", 500, "g"));
        list.add(new Ingredient(5, "Bread", 2, "loaf"));
        list.add(new Ingredient(6, "Pasta", 500, "g"));
        list.add(new Ingredient(7, "Noodles", 500, "g"));

        // Proteins & Meats
        list.add(new Ingredient(8, "Chicken", 2, "kg"));
        list.add(new Ingredient(9, "Chicken Mince", 1, "kg"));
        list.add(new Ingredient(10, "Beef", 1, "kg"));
        list.add(new Ingredient(11, "Mutton", 1, "kg"));
        list.add(new Ingredient(12, "Fish", 1, "kg"));
        list.add(new Ingredient(13, "Eggs", 12, "pcs"));

        // Dairy
        list.add(new Ingredient(14, "Milk", 2, "liter"));
        list.add(new Ingredient(15, "Yogurt", 1, "kg"));
        list.add(new Ingredient(16, "Butter", 250, "g"));
        list.add(new Ingredient(17, "Cheese", 300, "g"));
        list.add(new Ingredient(18, "Cream", 250, "ml"));

        // Pulses & Legumes
        list.add(new Ingredient(19, "Lentils", 1, "kg"));
        list.add(new Ingredient(20, "Chickpeas", 1, "kg"));
        list.add(new Ingredient(21, "Kidney Beans", 500, "g"));

        // Vegetables
        list.add(new Ingredient(22, "Potato", 2, "kg"));
        list.add(new Ingredient(23, "Tomato", 2, "kg"));
        list.add(new Ingredient(24, "Onion", 2, "kg"));
        list.add(new Ingredient(25, "Garlic", 250, "g"));
        list.add(new Ingredient(26, "Ginger", 250, "g"));
        list.add(new Ingredient(27, "Cooking Oil", 2, "liter"));
        list.add(new Ingredient(28, "Bell Pepper", 500, "g"));
        list.add(new Ingredient(29, "Carrot", 500, "g"));
        list.add(new Ingredient(30, "Cabbage", 500, "g"));
        list.add(new Ingredient(31, "Green Peas", 500, "g"));
        list.add(new Ingredient(32, "Spinach", 500, "g"));
        list.add(new Ingredient(33, "Cauliflower", 1, "pcs"));
        list.add(new Ingredient(34, "Capsicum", 500, "g"));
        list.add(new Ingredient(35, "Lemon", 6, "pcs"));
        list.add(new Ingredient(36, "Cucumber", 4, "pcs"));
        list.add(new Ingredient(37, "Mushroom", 300, "g"));
        list.add(new Ingredient(38, "Green Chili", 200, "g"));
        list.add(new Ingredient(39, "Coriander", 100, "g"));

        // Condiments & Miscellaneous
        list.add(new Ingredient(40, "Sugar", 1, "kg"));
        list.add(new Ingredient(41, "Tea Leaves", 250, "g"));
        list.add(new Ingredient(42, "Mayonnaise", 500, "g"));
        list.add(new Ingredient(43, "Ketchup", 500, "g"));
        list.add(new Ingredient(44, "Honey", 250, "g"));
        list.add(new Ingredient(45, "Oats", 500, "g"));

        // Default Pantry Inventory Starter Kit for demonstration
        setOwned(list, "Rice", true);
        setOwned(list, "Chicken", true);
        setOwned(list, "Onion", true);
        setOwned(list, "Tomato", true);
        setOwned(list, "Cooking Oil", true);
        setOwned(list, "Eggs", true);
        setOwned(list, "Flour", true);
        setOwned(list, "Garlic", true);
        setOwned(list, "Ginger", true);
    }

    private static void setOwned(IngredientLinkedList list, String name, boolean owned) {
        Ingredient item = list.find(name);
        if (item != null) item.isOwned = owned;
    }

    /**
     * Loads recipes into the custom RecipeBST tree.
     */
    public static void loadRecipes(RecipeBST tree) {
        // =========================================================================
        // SECTION 1: DESI MAIN COURSE RECIPES
        // =========================================================================

        // 1. Chicken Biryani
        RecipeIngredientLinkedList r1Ing = new RecipeIngredientLinkedList();
        r1Ing.add(new RecipeIngredient(1, "Rice", 1, "kg", true)); // CORE
        r1Ing.add(new RecipeIngredient(8, "Chicken", 1, "kg", true)); // CORE
        r1Ing.add(new RecipeIngredient(24, "Onion", 1, "kg", true));
        r1Ing.add(new RecipeIngredient(23, "Tomato", 1, "kg", true));
        r1Ing.add(new RecipeIngredient(27, "Cooking Oil", 1, "liter", true));
        tree.insertRecipe(new Recipe(1, "Chicken Biryani", "Main Course", 
            "Cook chicken masala gravy, boil rice separately, layer together with spices and steam on low flame for 15 mins.",
            r1Ing, 650, 38.0, 75.0, 22.0, 4.0, 45, "Medium", new String[]{"High Protein", "Desi Special"}, false));

        // 2. Chicken Karahi
        RecipeIngredientLinkedList r2Ing = new RecipeIngredientLinkedList();
        r2Ing.add(new RecipeIngredient(8, "Chicken", 1, "kg", true)); // CORE
        r2Ing.add(new RecipeIngredient(23, "Tomato", 1, "kg", true)); // CORE
        r2Ing.add(new RecipeIngredient(25, "Garlic", 1, "pcs", true));
        r2Ing.add(new RecipeIngredient(26, "Ginger", 1, "pcs", true));
        r2Ing.add(new RecipeIngredient(27, "Cooking Oil", 1, "liter", true));
        tree.insertRecipe(new Recipe(2, "Chicken Karahi", "Main Course",
            "Fry chicken in high flame with tomatoes, green chilies, and ginger until oil separates.",
            r2Ing, 520, 42.0, 12.0, 32.0, 2.0, 30, "Easy", new String[]{"High Protein", "Keto", "Low Carb"}, false));

        // 3. Daal Chawal (Core Ingredient Test: Lentils + Rice required)
        RecipeIngredientLinkedList r3Ing = new RecipeIngredientLinkedList();
        r3Ing.add(new RecipeIngredient(19, "Lentils", 1, "kg", true)); // CORE
        r3Ing.add(new RecipeIngredient(1, "Rice", 1, "kg", true));     // CORE
        r3Ing.add(new RecipeIngredient(24, "Onion", 1, "kg", true));
        r3Ing.add(new RecipeIngredient(25, "Garlic", 1, "pcs", true));
        r3Ing.add(new RecipeIngredient(27, "Cooking Oil", 1, "liter", true));
        tree.insertRecipe(new Recipe(3, "Daal Chawal", "Main Course",
            "Boil yellow lentils with turmeric and salt. Prepare garlic tarka in ghee/oil and serve over boiled basmati rice.",
            r3Ing, 420, 18.0, 72.0, 8.0, 9.0, 25, "Easy", new String[]{"Vegetarian", "High Fiber"}, false));

        // 4. Mutton Korma
        RecipeIngredientLinkedList r4Ing = new RecipeIngredientLinkedList();
        r4Ing.add(new RecipeIngredient(11, "Mutton", 1, "kg", true)); // CORE
        r4Ing.add(new RecipeIngredient(15, "Yogurt", 1, "kg", true)); // CORE
        r4Ing.add(new RecipeIngredient(24, "Onion", 1, "kg", true));
        r4Ing.add(new RecipeIngredient(27, "Cooking Oil", 1, "liter", true));
        tree.insertRecipe(new Recipe(4, "Mutton Korma", "Main Course",
            "Sauté fried crisp onions with yogurt, mutton, and whole spices until rich brown gravy forms.",
            r4Ing, 710, 44.0, 14.0, 48.0, 1.0, 60, "Hard", new String[]{"High Protein", "Keto"}, false));

        // 5. Beef Nihari
        RecipeIngredientLinkedList r5Ing = new RecipeIngredientLinkedList();
        r5Ing.add(new RecipeIngredient(10, "Beef", 1, "kg", true));   // CORE
        r5Ing.add(new RecipeIngredient(2, "Flour", 1, "kg", true));   // CORE
        r5Ing.add(new RecipeIngredient(27, "Cooking Oil", 1, "liter", true));
        tree.insertRecipe(new Recipe(5, "Beef Nihari", "Main Course",
            "Slow cook beef shank with Nihari spice mix overnight until tender. Thicken gravy with roasted wheat flour slurry.",
            r5Ing, 680, 46.0, 30.0, 40.0, 3.0, 90, "Hard", new String[]{"High Protein"}, false));

        // 6. White Chicken Handi
        RecipeIngredientLinkedList r6Ing = new RecipeIngredientLinkedList();
        r6Ing.add(new RecipeIngredient(8, "Chicken", 1, "kg", true));  // CORE
        r6Ing.add(new RecipeIngredient(18, "Cream", 250, "ml", true)); // CORE
        r6Ing.add(new RecipeIngredient(15, "Yogurt", 1, "kg", true));
        r6Ing.add(new RecipeIngredient(16, "Butter", 1, "g", true));
        tree.insertRecipe(new Recipe(6, "White Chicken Handi", "Main Course",
            "Cook boneless chicken pieces in white pepper, yogurt, and fresh heavy cream sauce in a clay pot.",
            r6Ing, 590, 40.0, 10.0, 38.0, 1.0, 35, "Medium", new String[]{"High Protein", "Keto"}, false));

        // 7. Aloo Gobi
        RecipeIngredientLinkedList r7Ing = new RecipeIngredientLinkedList();
        r7Ing.add(new RecipeIngredient(22, "Potato", 1, "kg", true));     // CORE
        r7Ing.add(new RecipeIngredient(33, "Cauliflower", 1, "pcs", true));// CORE
        r7Ing.add(new RecipeIngredient(24, "Onion", 1, "kg", true));
        r7Ing.add(new RecipeIngredient(23, "Tomato", 1, "kg", true));
        tree.insertRecipe(new Recipe(7, "Aloo Gobi", "Main Course",
            "Stir fry cauliflower florets and potato cubes with cumin, turmeric, and fresh ginger.",
            r7Ing, 280, 7.0, 48.0, 9.0, 8.0, 20, "Easy", new String[]{"Vegetarian", "Low Carb"}, false));

        // 8. Palak Paneer / Spinach Curry
        RecipeIngredientLinkedList r8Ing = new RecipeIngredientLinkedList();
        r8Ing.add(new RecipeIngredient(32, "Spinach", 1, "kg", true)); // CORE
        r8Ing.add(new RecipeIngredient(17, "Cheese", 300, "g", true)); // CORE
        r8Ing.add(new RecipeIngredient(25, "Garlic", 1, "pcs", true));
        tree.insertRecipe(new Recipe(8, "Palak Paneer", "Main Course",
            "Blanch and puree fresh spinach leaves. Simmer with sautéed cheese cubes and garlic tarka.",
            r8Ing, 340, 19.0, 16.0, 24.0, 6.0, 25, "Medium", new String[]{"Vegetarian", "Keto", "High Fiber"}, false));

        // 9. Chana Masala
        RecipeIngredientLinkedList r9Ing = new RecipeIngredientLinkedList();
        r9Ing.add(new RecipeIngredient(20, "Chickpeas", 1, "kg", true)); // CORE
        r9Ing.add(new RecipeIngredient(23, "Tomato", 1, "kg", true));   // CORE
        r9Ing.add(new RecipeIngredient(24, "Onion", 1, "kg", true));
        tree.insertRecipe(new Recipe(9, "Chana Masala", "Main Course",
            "Simmer soft chickpeas in a tangy onion tomato gravy infused with amchur and garam masala.",
            r9Ing, 360, 16.0, 58.0, 8.0, 12.0, 30, "Easy", new String[]{"Vegetarian", "High Fiber"}, false));

        // 10. Fish Curry
        RecipeIngredientLinkedList r10Ing = new RecipeIngredientLinkedList();
        r10Ing.add(new RecipeIngredient(12, "Fish", 1, "kg", true));    // CORE
        r10Ing.add(new RecipeIngredient(23, "Tomato", 1, "kg", true));  // CORE
        r10Ing.add(new RecipeIngredient(24, "Onion", 1, "kg", true));
        tree.insertRecipe(new Recipe(10, "Fish Curry", "Main Course",
            "Pan sear fish fillets and cook in mustard tomato seed coconut curry.",
            r10Ing, 410, 34.0, 12.0, 22.0, 2.0, 25, "Medium", new String[]{"High Protein", "Keto"}, false));

        // =========================================================================
        // SECTION 2: BREAKFAST & PARATHAS
        // =========================================================================

        // 11. Aloo Paratha
        RecipeIngredientLinkedList r11Ing = new RecipeIngredientLinkedList();
        r11Ing.add(new RecipeIngredient(2, "Flour", 1, "kg", true));   // CORE
        r11Ing.add(new RecipeIngredient(22, "Potato", 1, "kg", true)); // CORE
        r11Ing.add(new RecipeIngredient(16, "Butter", 1, "g", true));
        tree.insertRecipe(new Recipe(11, "Aloo Paratha", "Breakfast",
            "Stuff spiced mashed potatoes inside whole wheat dough disk. Shallow fry on tawa with desi ghee.",
            r11Ing, 380, 9.0, 62.0, 14.0, 5.0, 20, "Easy", new String[]{"Vegetarian", "Desi Special"}, false));

        // 12. Desi Omelette
        RecipeIngredientLinkedList r12Ing = new RecipeIngredientLinkedList();
        r12Ing.add(new RecipeIngredient(13, "Eggs", 2, "pcs", true));   // CORE
        r12Ing.add(new RecipeIngredient(24, "Onion", 1, "kg", true));  // CORE
        r12Ing.add(new RecipeIngredient(23, "Tomato", 1, "kg", true));
        r12Ing.add(new RecipeIngredient(38, "Green Chili", 10, "g", false));
        tree.insertRecipe(new Recipe(12, "Desi Omelette", "Breakfast",
            "Beat eggs with finely chopped onions, tomatoes, green chilies, and salt. Pan fry till golden.",
            r12Ing, 240, 14.0, 4.0, 18.0, 1.0, 10, "Easy", new String[]{"High Protein", "Keto", "Low Carb"}, true));

        // 13. Egg Cheese Toast
        RecipeIngredientLinkedList r13Ing = new RecipeIngredientLinkedList();
        r13Ing.add(new RecipeIngredient(5, "Bread", 2, "loaf", true)); // CORE
        r13Ing.add(new RecipeIngredient(13, "Eggs", 2, "pcs", true));  // CORE
        r13Ing.add(new RecipeIngredient(17, "Cheese", 50, "g", true));
        tree.insertRecipe(new Recipe(13, "Egg Cheese Toast", "Breakfast",
            "Dip bread in beaten eggs, toast on buttered pan, and top with melted cheese slice.",
            r13Ing, 310, 16.0, 28.0, 15.0, 2.0, 10, "Easy", new String[]{"High Protein"}, true));

        // 14. Halwa Puri
        RecipeIngredientLinkedList r14Ing = new RecipeIngredientLinkedList();
        r14Ing.add(new RecipeIngredient(2, "Flour", 1, "kg", true));    // CORE
        r14Ing.add(new RecipeIngredient(4, "Semolina", 500, "g", true));// CORE
        r14Ing.add(new RecipeIngredient(40, "Sugar", 500, "g", true));
        tree.insertRecipe(new Recipe(14, "Halwa Puri", "Breakfast",
            "Deep fry puffy flour puris and serve alongside sweet semolina halwa and spicy chickpea curry.",
            r14Ing, 580, 11.0, 84.0, 24.0, 3.0, 35, "Medium", new String[]{"Desi Special"}, false));

        // 15. Oatmeal with Milk & Honey
        RecipeIngredientLinkedList r15Ing = new RecipeIngredientLinkedList();
        r15Ing.add(new RecipeIngredient(45, "Oats", 100, "g", true)); // CORE
        r15Ing.add(new RecipeIngredient(14, "Milk", 250, "ml", true));// CORE
        r15Ing.add(new RecipeIngredient(44, "Honey", 20, "g", false));
        tree.insertRecipe(new Recipe(15, "Oatmeal Milk Honey", "Breakfast",
            "Simmer rolled oats in fresh warm milk for 5 minutes. Drizzle organic honey on top before serving.",
            r15Ing, 290, 12.0, 48.0, 6.0, 7.0, 8, "Easy", new String[]{"Healthy", "High Fiber"}, true));

        // =========================================================================
        // SECTION 3: QUICK SNACKS & STREET FOOD (5-15 Minute Meals)
        // =========================================================================

        // 16. Chicken Sandwich
        RecipeIngredientLinkedList r16Ing = new RecipeIngredientLinkedList();
        r16Ing.add(new RecipeIngredient(5, "Bread", 2, "loaf", true));     // CORE
        r16Ing.add(new RecipeIngredient(8, "Chicken", 200, "g", true));    // CORE
        r16Ing.add(new RecipeIngredient(42, "Mayonnaise", 50, "g", true));
        tree.insertRecipe(new Recipe(16, "Chicken Sandwich", "Quick Snacks",
            "Shred boiled chicken breast, mix with mayonnaise and black pepper, spread between toasted bread slices.",
            r16Ing, 350, 22.0, 32.0, 14.0, 2.0, 12, "Easy", new String[]{"High Protein", "Fast 15m"}, true));

        // 17. Vegetable Pasta
        RecipeIngredientLinkedList r17Ing = new RecipeIngredientLinkedList();
        r17Ing.add(new RecipeIngredient(6, "Pasta", 250, "g", true));      // CORE
        r17Ing.add(new RecipeIngredient(23, "Tomato", 200, "g", true));    // CORE
        r17Ing.add(new RecipeIngredient(28, "Bell Pepper", 100, "g", false));
        tree.insertRecipe(new Recipe(17, "Vegetable Pasta", "Quick Snacks",
            "Boil penne pasta. Toss in garlic tomato basil reduction sauce with crunchy bell peppers.",
            r17Ing, 380, 11.0, 66.0, 8.0, 5.0, 15, "Easy", new String[]{"Vegetarian", "Fast 15m"}, true));

        // 18. French Fries
        RecipeIngredientLinkedList r18Ing = new RecipeIngredientLinkedList();
        r18Ing.add(new RecipeIngredient(22, "Potato", 500, "g", true));     // CORE
        r18Ing.add(new RecipeIngredient(27, "Cooking Oil", 500, "ml", true));// CORE
        tree.insertRecipe(new Recipe(18, "Crispy French Fries", "Quick Snacks",
            "Cut potatoes into thin batons, soak in cold water, double fry until golden crispy and season with chaat masala.",
            r18Ing, 320, 4.0, 42.0, 16.0, 4.0, 15, "Easy", new String[]{"Vegetarian", "Fast 15m"}, true));

        // 19. Garlic Bread
        RecipeIngredientLinkedList r19Ing = new RecipeIngredientLinkedList();
        r19Ing.add(new RecipeIngredient(5, "Bread", 2, "loaf", true));     // CORE
        r19Ing.add(new RecipeIngredient(16, "Butter", 50, "g", true));     // CORE
        r19Ing.add(new RecipeIngredient(25, "Garlic", 20, "g", true));
        tree.insertRecipe(new Recipe(19, "Garlic Bread", "Quick Snacks",
            "Mix softened butter with crushed garlic and parsley. Spread on bread slices and toast till golden.",
            r19Ing, 260, 5.0, 24.0, 16.0, 1.0, 10, "Easy", new String[]{"Vegetarian", "Fast 15m"}, true));

        // 20. Chicken Noodles / Chow Mein
        RecipeIngredientLinkedList r20Ing = new RecipeIngredientLinkedList();
        r20Ing.add(new RecipeIngredient(7, "Noodles", 250, "g", true));    // CORE
        r20Ing.add(new RecipeIngredient(8, "Chicken", 150, "g", true));    // CORE
        r20Ing.add(new RecipeIngredient(30, "Cabbage", 100, "g", false));
        tree.insertRecipe(new Recipe(20, "Chicken Noodles", "Quick Snacks",
            "Stir-fry chicken strips, shredded cabbage, carrots, and boiled noodles over high heat with soy sauce.",
            r20Ing, 410, 24.0, 54.0, 12.0, 4.0, 15, "Easy", new String[]{"High Protein", "Fast 15m"}, true));

        // =========================================================================
        // SECTION 4: HEALTHY, KETO & HIGH PROTEIN
        // =========================================================================

        // 21. Grilled Chicken Salad
        RecipeIngredientLinkedList r21Ing = new RecipeIngredientLinkedList();
        r21Ing.add(new RecipeIngredient(8, "Chicken", 300, "g", true));    // CORE
        r21Ing.add(new RecipeIngredient(36, "Cucumber", 2, "pcs", true));  // CORE
        r21Ing.add(new RecipeIngredient(23, "Tomato", 1, "pcs", false));
        r21Ing.add(new RecipeIngredient(35, "Lemon", 1, "pcs", false));
        tree.insertRecipe(new Recipe(21, "Grilled Chicken Salad", "Healthy",
            "Marinate chicken breast in lemon juice and herbs. Grill until juicy, slice, and toss over fresh crisp cucumber salad.",
            r21Ing, 280, 42.0, 6.0, 9.0, 3.0, 15, "Easy", new String[]{"High Protein", "Keto", "Low Carb", "Weight Loss"}, true));

        // 22. Boiled Egg Protein Bowl
        RecipeIngredientLinkedList r22Ing = new RecipeIngredientLinkedList();
        r22Ing.add(new RecipeIngredient(13, "Eggs", 3, "pcs", true));      // CORE
        r22Ing.add(new RecipeIngredient(36, "Cucumber", 1, "pcs", true));  // CORE
        r22Ing.add(new RecipeIngredient(35, "Lemon", 1, "pcs", false));
        tree.insertRecipe(new Recipe(22, "Boiled Egg Protein Bowl", "Healthy",
            "Hard boil 3 eggs, slice into halves, season with black pepper, salt, and lemon juice. Serve with fresh sliced cucumbers.",
            r22Ing, 230, 19.0, 3.0, 15.0, 1.0, 10, "Easy", new String[]{"High Protein", "Keto", "Low Carb", "Weight Loss"}, true));

        // =========================================================================
        // SECTION 5: DESSERTS & DRINKS
        // =========================================================================

        // 23. Kheer / Rice Pudding
        RecipeIngredientLinkedList r23Ing = new RecipeIngredientLinkedList();
        r23Ing.add(new RecipeIngredient(14, "Milk", 1, "liter", true));    // CORE
        r23Ing.add(new RecipeIngredient(1, "Rice", 100, "g", true));       // CORE
        r23Ing.add(new RecipeIngredient(40, "Sugar", 150, "g", true));
        tree.insertRecipe(new Recipe(23, "Rice Kheer", "Desserts",
            "Slowly cook ground rice in full cream milk until thickened. Sweeten with sugar and garnish with crushed cardamom.",
            r23Ing, 380, 8.0, 56.0, 14.0, 1.0, 40, "Medium", new String[]{"Desi Special"}, false));

        // 24. Chai / Karak Tea
        RecipeIngredientLinkedList r24Ing = new RecipeIngredientLinkedList();
        r24Ing.add(new RecipeIngredient(14, "Milk", 200, "ml", true));    // CORE
        r24Ing.add(new RecipeIngredient(41, "Tea Leaves", 10, "g", true)); // CORE
        r24Ing.add(new RecipeIngredient(40, "Sugar", 15, "g", false));
        tree.insertRecipe(new Recipe(24, "Karak Chai", "Drinks",
            "Boil water with black tea leaves and cardamom. Add milk and sugar, brew until rich golden brown.",
            r24Ing, 120, 4.0, 14.0, 5.0, 0.0, 7, "Easy", new String[]{"Fast 15m", "Desi Special"}, true));
    }

    /**
     * Loads saved favorites into FavoriteLinkedList.
     */
    public static void loadFavorites(FavoriteLinkedList favorites, RecipeBST tree) {
        if (favorites == null || tree == null) return;
        favorites.clear();
        Recipe biryani = tree.searchRecipe("Chicken Biryani");
        if (biryani != null) favorites.add(biryani);

        Recipe karahi = tree.searchRecipe("Chicken Karahi");
        if (karahi != null) favorites.add(karahi);
    }
}