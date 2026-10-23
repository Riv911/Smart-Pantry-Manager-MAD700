@echo off
setlocal
cd /d "%~dp0"
if not exist "core-test-output" mkdir core-test-output
javac -d core-test-output app\src\main\java\com\example\smartpantrymanager\IngredientNormalizer.java app\src\main\java\com\example\smartpantrymanager\UnitValue.java app\src\main\java\com\example\smartpantrymanager\UnitConverter.java app\src\main\java\com\example\smartpantrymanager\PantryItem.java app\src\main\java\com\example\smartpantrymanager\RecipeIngredient.java app\src\main\java\com\example\smartpantrymanager\StrictMatchEngine.java app\src\test\java\com\example\smartpantrymanager\CoreLogicTest.java
if errorlevel 1 (echo Java compilation failed & exit /b 1)
java -cp core-test-output com.example.smartpantrymanager.CoreLogicTest
exit /b %ERRORLEVEL%
