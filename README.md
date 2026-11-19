# Car Race Simulation

JavaFX car race using the MVC pattern. Assignment 02, Program Development in a Graphical Environment.
Author: Justin Lachapelle

## How to run
Open the project in NetBeans and run it, or use `mvn clean javafx:run`.

## What it does
- Slideshow of 4 cars with a greeting and music, then the race starts.
- Start, Pause and Reset buttons and a speed slider.
- The winner and the final results are shown at the bottom right.

## Structure (MVC)
- Model: `CarData`, `CarRaceModel`
- View: `CarRaceFXML.fxml`, `CarShape`, `style.css`
- Controller: `CarRaceController`
