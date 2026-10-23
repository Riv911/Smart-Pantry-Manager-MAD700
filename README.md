# Smart Pantry Manager — Mobile App Development 700

**Java Android app • Local SQLite • ListView + custom adapters • Five screens • 18 seeded recipes**

Smart Pantry Manager helps households reduce food waste by tracking food at home and suggesting only recipes they can prepare with every required ingredient **and sufficient quantities**.

## Screens and functions
- Pantry List — loads persisted ingredients from SQLite; Edit and Delete controls on each row.
- Add/Edit Ingredient — validates a nonempty name and finite positive quantity; supports unit, optional expiry date, and clearing an existing expiry date.
- Suggested Recipes — uses `StrictMatchEngine` to reject a recipe if any ingredient or quantity is missing, accounting for compatible units, singular/plural names, and repeated requirements.
- Recipe Detail — lists all recipe ingredients with quantities and preparation steps.
- Settings — stores preferred units and expiry-warning settings in `SharedPreferences`; expiry warnings appear in the pantry when relevant.

## Database design
`DatabaseHelper` extends `SQLiteOpenHelper`, creates `pantry_items`, `recipes`, and `recipe_ingredients`, and seeds **18 recipes on first installation only**. Add, list/read, edit/update, and delete use genuine SQLite operations. Pantry data persists after the app is closed and reopened. SQLite was chosen for reliable, offline, device-local persistence; no account or server is needed.

## Setup on Windows — Android Studio
1. Install **Android Studio** from https://developer.android.com/studio and use its JDK 17 (or a compatible JDK 17 installation). Install **Android SDK Platform 35** and the Android emulator / platform tools from SDK Manager.
2. Extract the ZIP. Open the `Smart-Pantry-Manager-MAD700` directory in Android Studio (`File` > `Open`). **Do not open the ZIP directly.**
3. Ensure internet access for the first Gradle sync. The original project had missing Gradle wrapper files. The updated `gradlew.bat` automatically downloads **Gradle 8.9's official wrapper JAR**, checks its published SHA-256 hash, and then downloads Gradle 8.9 when needed. If Android Studio reports a missing wrapper, open a terminal in this directory, run `gradlew.bat --version`, then click Sync Project with Gradle Files.
4. Use **Build** > **Make Project** to validate compilation. Choose an Android emulator or USB-debug-enabled device and click **Run**.
5. In PowerShell from this project folder you can also run `./gradlew.bat assembleDebug` once the SDK, network and wrapper are ready. The resulting APK is under `app/build/outputs/apk/debug/`.

## Desktop core logic checks (no Android SDK needed)
Run `RUN_CORE_TESTS_WINDOWS.bat` using JDK 17 or newer. This compiles/test-runs the matching engine on the JVM and checks complete matches, missing items, quantities, unit conversions, duplicates, incompatible units, invalid values and basic normalisation.

## Live functional acceptance walkthrough
1. Start with no pantry items; confirm the empty-state message is visible.
2. Attempt a blank ingredient name and quantity `0`, verifying validation messages.
3. Add `eggs 2 pieces`, `onion 1 piece`, `tomato 1 piece`, `cheese 40 g`, and `oil 10 ml`.
4. Open Suggestions; the **Omelette** should be present. Open its detail to review all five required ingredients and the method.
5. Delete `oil` from the pantry and revisit Suggestions: **Omelette must disappear**.
6. Add `oil 10 ml` back; **Omelette must reappear**. Edit `cheese` to `20 g` and confirm **Omelette disappears**; restore `40 g` and confirm it returns.
7. Close and reopen the app: pantry entries remain. Set a future expiry date and show the Settings screen.

## Scope and submission proof
- No Maps SDK, location/GPS, payments, cloud login or recipe API.
- Record a **real, voiced, continuous 5–7 minute Android walkthrough** including GitHub history, CRUD, strict matching, persistence, three code concepts and database justification. The video preview in the package is **not a replacement for recorded app evidence**.
- Replace the labelled report preview images with **screenshots captured from your running app**.
- Create a GitHub repository and push this real local history; do not claim retroactive development dates. See `GITHUB_PUBLISHING.md`.

## Limitations of automated verification
Core Java tests and source/resource audits were run, but no emulator or Android SDK was available in the repair environment. App build, installation and live screen recording must be verified on a real Android toolchain before final submission.
