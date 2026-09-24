# Smart Pantry Manager - Mobile App Development 700

A Java Android application that helps reduce food waste by tracking pantry ingredients and suggesting recipes that can be made **strictly** from ingredients already available.

## Core rule
A recipe appears in Suggested Recipes only when every required ingredient is present in the pantry and the available quantity is at least the required quantity. Simple ingredient-name normalization (for example tomato/tomatoes) and compatible unit conversion are applied before comparison.

## Technology
- Java
- Android Studio
- SQLite using `SQLiteOpenHelper`
- `ListView` + custom `BaseAdapter`
- Multiple Activities + explicit Intents
- `SharedPreferences` for simple settings
- No maps, GPS, location permissions, payment processing, or external recipe APIs

## Main screens
1. Pantry List
2. Add/Edit Ingredient
3. Suggested Recipes
4. Recipe Detail
5. Settings

## Database
SQLite is used because the application needs reliable on-device persistence and does not require cloud synchronisation. `DatabaseHelper` owns CRUD and seed data; `RecipeMatcher` owns the strict business rule.

## Setup / Run
1. Open the `Smart-Pantry-Manager-MAD700` folder in Android Studio.
2. Use an installed Android SDK (API 35 or a newer compatible SDK) and JDK 17+ as supported by your Android Studio/Gradle setup.
3. Let Gradle sync and accept any SDK component prompts.
4. Run on an Android emulator or physical Android device.

## Testing the strict rule
A simple demonstration sequence is:
- Add eggs, onion, tomato, cheese and oil.
- Open Suggested Recipes and confirm Omelette appears.
- Delete oil.
- Reopen Suggested Recipes and confirm Omelette disappears.
- Add oil again and confirm Omelette reappears.
- Edit a quantity down below the recipe requirement and confirm it disappears.

## Git history
This project is structured to be developed incrementally. The submission package includes a local Git history with meaningful staged milestones.

## GitHub
The final public repository link must be set to the actual student-owned GitHub repository before submission. A placeholder is intentionally not presented as a working remote URL.
