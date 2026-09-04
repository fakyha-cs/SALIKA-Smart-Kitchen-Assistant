/**
 * Class: NutritionTracker
 * 
 * Purpose:
 * Manages daily nutritional intake logging and calculates goal progress.
 * Integrates directly with cooked recipes, dashboard metrics, and smart assistant messages.
 *
 * Why this class exists:
 * Fulfills Feature #6 (Nutrition Tracker) & Feature #7 (Smart Nutrition Assistant).
 * Tracks calories, protein, carbs, and fat, and recommends foods when deficiencies are detected.
 *
 * Student Code Style & Viva Notes:
 * - Simple fields and getter/setter logic.
 * - Simple calculations without external libraries or streams.
 * - Time Complexity: O(1) for adding/logging nutrient intake.
 * - Space Complexity: O(1) memory footprint.
 */
public class NutritionTracker {
    // Current accumulated daily totals
    private int currentCalories;
    private double currentProtein;
    private double currentCarbs;
    private double currentFat;

    // Standard recommended daily targets for average adult
    private final int targetCalories = 2000;
    private final double targetProtein = 75.0; // grams
    private final double targetCarbs = 250.0;   // grams
    private final double targetFat = 65.0;      // grams

    public NutritionTracker() {
        resetDailyTotals();
    }

    /**
     * Resets the daily tracked totals to zero.
     * Time Complexity: O(1).
     */
    public void resetDailyTotals() {
        this.currentCalories = 0;
        this.currentProtein = 0.0;
        this.currentCarbs = 0.0;
        this.currentFat = 0.0;
    }

    /**
     * Logs a cooked recipe and adds its nutritional values to daily totals.
     * Time Complexity: O(1).
     */
    public void logCookedRecipe(Recipe recipe) {
        if (recipe != null) {
            this.currentCalories += recipe.calories;
            this.currentProtein += recipe.protein;
            this.currentCarbs += recipe.carbs;
            this.currentFat += recipe.fat;
        }
    }

    /**
     * Removes a cooked recipe from daily totals (used for UNDO operation).
     * Time Complexity: O(1).
     */
    public void unlogCookedRecipe(Recipe recipe) {
        if (recipe != null) {
            this.currentCalories = Math.max(0, this.currentCalories - recipe.calories);
            this.currentProtein = Math.max(0.0, this.currentProtein - recipe.protein);
            this.currentCarbs = Math.max(0.0, this.currentCarbs - recipe.carbs);
            this.currentFat = Math.max(0.0, this.currentFat - recipe.fat);
        }
    }

    // Getters for Current Intake
    public int getCurrentCalories() { return currentCalories; }
    public double getCurrentProtein() { return currentProtein; }
    public double getCurrentCarbs() { return currentCarbs; }
    public double getCurrentFat() { return currentFat; }

    // Getters for Target Goals
    public int getTargetCalories() { return targetCalories; }
    public double getTargetProtein() { return targetProtein; }
    public double getTargetCarbs() { return targetCarbs; }
    public double getTargetFat() { return targetFat; }

    // Percentage Calculation Helpers for Progress Bars (0 to 100%)
    public int getCaloriePercentage() {
        return Math.min(100, (int) ((double) currentCalories / targetCalories * 100));
    }
    public int getProteinPercentage() {
        return Math.min(100, (int) (currentProtein / targetProtein * 100));
    }
    public int getCarbPercentage() {
        return Math.min(100, (int) (currentCarbs / targetCarbs * 100));
    }
    public int getFatPercentage() {
        return Math.min(100, (int) (currentFat / targetFat * 100));
    }

    /**
     * Smart Nutrition Assistant Advice Generator.
     * Analyzes nutrient levels and returns actionable recommendations for viva presentation.
     * Time Complexity: O(1).
     */
    public String getNutritionalAdvice() {
        if (currentCalories == 0) {
            return "No meals logged today! Cook a recipe to track your energy & macros.";
        }

        double proteinRatio = currentProtein / targetProtein;
        double carbRatio = currentCarbs / targetCarbs;
        double calorieRatio = (double) currentCalories / targetCalories;

        if (proteinRatio < 0.5) {
            return "⚠️ Protein intake is low (" + (int) currentProtein + "g / " + (int) targetProtein + "g). We recommend cooking Chicken, Eggs, Fish, or Lentil dishes!";
        } else if (carbRatio < 0.4) {
            return "⚠️ Carbohydrates are low (" + (int) currentCarbs + "g / " + (int) targetCarbs + "g). Consider adding Rice, Roti, or Pasta to replenish energy.";
        } else if (calorieRatio >= 1.0) {
            return "🎉 Daily calorie target reached (" + currentCalories + " kcal)! Balance remainder of the day with light snacks and water.";
        } else {
            return "✅ Great nutritional balance today! You are on track to meet all macro goals.";
        }
    }
}
