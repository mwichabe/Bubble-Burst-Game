## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

Bubble Burst Game

Description:
Bubble Burst Game is a simple Java application that simulates a bubble bursting game. Players interact with the graphical user interface (GUI) to burst bubbles appearing on the playing field. The game includes multiple rounds, with an increasing level of difficulty and a countdown timer for each round.

Features:
1. Multiple Rounds: The game consists of multiple rounds, with each round increasing in difficulty.
2. Dynamic Bubbles: Bubbles appear on the playing field and can be burst by the player's clicks.
3. Countdown Timer: Each round has a countdown timer that decreases with each passing second.
4. Collision Avoidance: Bubbles avoid overlapping with each other to maintain gameplay clarity.
5. Game Over Condition: The game ends when the player completes all rounds or runs out of time in a round.

File Structure:
- Main.java: Contains the main method to start the application.
- FirstGUI.java: Represents the first GUI screen where players can start or restart the game and select the difficulty level.
- SecondGUI.java: Represents the main game screen where players interact with bubbles and play the game.
- README.md: Provides an overview of the Bubble Burst Game, its features, and file structure.

How to Run:
1. Compile all Java files using `javac *.java`.
2. Run the application using `java Main`.

Screenshots:
    [!First Screen ](javaGame/assests/firstGui.png)


Credits:
- Developed by Collins Mwichabe

License:
This project is licensed under the [License Name].
