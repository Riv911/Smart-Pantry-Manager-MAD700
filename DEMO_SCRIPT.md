# 5-7 minute live demonstration script

Target length: about 6 minutes 20 seconds.

## 0:00-0:55 GitHub walkthrough
Open the public GitHub repository. Show the project README and scroll through at least ten meaningful commits. Briefly explain that the commits represent incremental milestones.

## 0:55-2:45 Live app demonstration
1. Open Pantry and show existing/seed data.
2. Tap + Add, enter a test ingredient, save it, and point out that it is shown in the pantry list (Create + Read).
3. Edit the same ingredient, change the quantity, save and show the updated value (Update).
4. Delete it with the confirmation dialog and show the item disappearing (Delete).
5. Add the ingredients required for Omelette: eggs, onion, tomato, cheese and oil.
6. Open Suggested Recipes and show Omelette.
7. Delete oil. Return to Suggested Recipes and show that Omelette is gone.
8. Add oil again and show that Omelette returns.
9. Change one quantity below the requirement and confirm the recipe is excluded.
10. Close and reopen the app and show that a pantry record remains (persistence).

## 2:45-5:50 Code explanation
Point to these actual files:
- `MainActivity.java`: `onCreate()` sets up the screen; `onResume()` reloads SQLite.
- `DatabaseHelper.java`: `onCreate()` creates tables and seeds 18 recipes; insert/query/update/delete implement CRUD.
- `PantryAdapter.java`: `getView()` maps database objects into the ListView row layout.
- `RecipeMatcher.java` + `StrictMatchEngine.java`: every required recipe ingredient is checked and insufficient quantities reject the recipe.
- `IngredientNormalizer.java`: handles simple singular/plural and alias differences.
- `UnitConverter.java`: handles compatible units such as kg/g and L/ml.
- `putExtra()` / `getLongExtra()`: demonstrates Intent data transfer between Activities.

## 5:50-6:20 SQLite justification
Explain that SQLite was chosen because the data only needs local persistence, the brief accepts it, and the app does not require cloud synchronisation.
