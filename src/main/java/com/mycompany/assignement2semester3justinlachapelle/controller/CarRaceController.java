package com.mycompany.assignement2semester3justinlachapelle.controller;

import com.mycompany.assignement2semester3justinlachapelle.model.CarData;
import com.mycompany.assignement2semester3justinlachapelle.model.CarRaceModel;
import com.mycompany.assignement2semester3justinlachapelle.view.CarShape;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Controller part of the MVC. The screen is in CarRaceFXML.fxml and JavaFX
 * links the @FXML fields below to it using the fx:id names.
 * This class runs the slideshow, handles the buttons, makes the animations,
 * finds the winner and writes the messages.
 *
 * @author Justin Lachapelle
 */
public class CarRaceController {

    /** The race is always in one of these 4 states. */
    private enum State { READY, RUNNING, PAUSED, FINISHED }

    private static final double SLIDE_SECONDS = 2.5;

    // the model (race data)
    private final CarRaceModel model = new CarRaceModel();

    // Things on the screen. The names have to match the fx:id in the FXML file
    @FXML private VBox slidePane;
    @FXML private ImageView slideImage;
    @FXML private Label captionLabel;
    @FXML private Label counterLabel;
    @FXML private BorderPane racePane;
    @FXML private Pane trackPane;
    @FXML private Button startButton;
    @FXML private Button pauseButton;
    @FXML private Label speedLabel;
    @FXML private Slider speedSlider;
    @FXML private Label messageTitle;
    @FXML private Label messageText;
    @FXML private VBox resultsBox;

    private final List<CarShape> carShapes = new ArrayList<>();
    // one TranslateTransition per car, it moves the car
    private final List<TranslateTransition> moveAnimations = new ArrayList<>();
    // two RotateTransitions per car for the wheels (positions 2*i and 2*i+1)
    private final List<RotateTransition> wheelAnimations = new ArrayList<>();

    private State state = State.READY;
    private double speedFactor = 1.0;
    private Timeline slideshowTimeline;
    private MediaPlayer mediaPlayer;
    private boolean slideshowDone = false;

    // ------------------------------------------------------------ slideshow

    /**
     * First part of the program: plays the music and shows the cars one by one.
     * After the last car, the race screen comes up.
     */
    public void startSlideshow() {
        playSound();

        slideshowTimeline = new Timeline();
        int total = model.getCars().size();
        for (int i = 0; i < total; i++) {
            final int index = i;
            slideshowTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(i * SLIDE_SECONDS),
                    e -> showSlide(index)));
        }
        // after the last slide, go to the race
        slideshowTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(total * SLIDE_SECONDS),
                e -> finishSlideshow()));
        slideshowTimeline.play();
    }

    private void showSlide(int index) {
        CarData car = model.getCars().get(index);
        slideImage.setImage(new Image(getClass().getResource(car.getImagePath()).toExternalForm()));
        captionLabel.setText("Car #" + car.getNumber() + "  \u2022  " + car.getColorName()
                + "  \u2022  " + car.getType());
        counterLabel.setText("Car " + (index + 1) + " of " + model.getCars().size());

        FadeTransition fade = new FadeTransition(Duration.millis(600), slideImage);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    private void finishSlideshow() {
        if (slideshowDone) {
            return;
        }
        slideshowDone = true;
        slideshowTimeline.stop();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        slidePane.setVisible(false);
        racePane.setVisible(true);
    }

    private void playSound() {
        try {
            URL url = getClass().getResource("/sounds/race_theme.wav");
            if (url != null) {
                mediaPlayer = new MediaPlayer(new Media(url.toExternalForm()));
                mediaPlayer.setVolume(0.6);
                mediaPlayer.play();
            }
        } catch (Exception ex) {
            System.out.println("Could not play the sound: " + ex.getMessage());
        }
    }

    // ----------------------------------------------------------- animations

    /**
     * Puts the 4 cars (made with shapes) on the track.
     */
    private void drawCars() {
        for (CarData car : model.getCars()) {
            CarShape shape = new CarShape(car);
            shape.setLayoutX(car.getX());
            shape.setLayoutY(car.getY());
            carShapes.add(shape);
            trackPane.getChildren().add(shape);
        }
    }

    /**
     * Makes the animations of every car (only once).
     * The durations are set later in updateAnimationSpeeds().
     */
    private void createAnimations() {
        List<CarData> cars = model.getCars();
        for (int i = 0; i < cars.size(); i++) {
            CarData car = cars.get(i);
            CarShape shape = carShapes.get(i);

            // moves the whole car from the start to the finish line
            TranslateTransition move = new TranslateTransition(Duration.seconds(1), shape);
            move.setFromX(0);
            move.setToX(model.getRaceDistance());
            move.setInterpolator(Interpolator.LINEAR);
            move.setOnFinished(e -> carFinished(car));
            moveAnimations.add(move);

            // each time the car moves, update its position in the model
            shape.translateXProperty().addListener((obs, oldValue, newValue)
                    -> model.updateCarPosition(car, newValue.doubleValue()));

            // wheels spin until the car finishes
            wheelAnimations.add(createSpin(shape.getRearWheel()));
            wheelAnimations.add(createSpin(shape.getFrontWheel()));
        }
    }

    private RotateTransition createSpin(Node wheel) {
        RotateTransition spin = new RotateTransition(Duration.seconds(1), wheel);
        spin.setByAngle(360);
        spin.setCycleCount(Animation.INDEFINITE);
        spin.setInterpolator(Interpolator.LINEAR);
        return spin;
    }

    /**
     * Sets the duration of each animation from the car speed, and applies
     * the speed of the slider.
     */
    private void updateAnimationSpeeds() {
        List<CarData> cars = model.getCars();
        for (int i = 0; i < cars.size(); i++) {
            double speed = cars.get(i).getSpeed();
            // time = distance / speed
            moveAnimations.get(i).setDuration(Duration.seconds(model.getRaceDistance() / speed));
            // one wheel turn = the circumference of the wheel
            double turnSeconds = 2 * Math.PI * CarShape.WHEEL_RADIUS / speed;
            wheelAnimations.get(2 * i).setDuration(Duration.seconds(turnSeconds));
            wheelAnimations.get(2 * i + 1).setDuration(Duration.seconds(turnSeconds));
        }
        // the slider changes the rate (1.0 normal, 2.0 twice as fast)
        for (Animation animation : moveAnimations) {
            animation.setRate(speedFactor);
        }
        for (Animation animation : wheelAnimations) {
            animation.setRate(speedFactor);
        }
    }

    // ---------------------------------------------------------- race logic

    /**
     * Called when a car reaches the finish line.
     */
    private void carFinished(CarData car) {
        model.recordFinish(car);

        // the car stopped, so the wheels stop too
        int index = model.getCars().indexOf(car);
        wheelAnimations.get(2 * index).stop();
        wheelAnimations.get(2 * index + 1).stop();

        if (model.getFinishOrder().size() == 1) {
            setMessage("We have a leader!", car.getLabel() + " crossed the finish line first!",
                    Collections.emptyList());
        }
        if (model.isRaceOver()) {
            state = State.FINISHED;
            showFinalResults();
            updateButtons();
        }
    }

    private void showFinalResults() {
        CarData winner = model.getWinner();
        List<String> lines = new ArrayList<>();
        int place = 1;
        for (CarData car : model.getFinishOrder()) {
            lines.add(ordinal(place) + "   " + car.getLabel() + " - " + car.getType());
            place++;
        }
        setMessage("\uD83C\uDFC6 Race Finished!",
                "Congratulations! " + winner.getLabel() + " is the winner!\n"
                + "Animations used: TranslateTransition moves the cars, "
                + "RotateTransition spins the wheels.\nFinal Results:", lines);
    }

    private String ordinal(int place) {
        switch (place) {
            case 1:
                return "1st";
            case 2:
                return "2nd";
            case 3:
                return "3rd";
            default:
                return place + "th";
        }
    }

    private void showReadyMessage() {
        setMessage("Ready to race!", "Press Start. Use the slider to change the speed.",
                Collections.emptyList());
    }

    /**
     * Writes in the message area at the bottom right.
     *
     * @param title   the title
     * @param text    the main message
     * @param results lines of the final results (can be empty)
     */
    private void setMessage(String title, String text, List<String> results) {
        messageTitle.setText(title);
        messageText.setText(text);
        resultsBox.getChildren().clear();
        for (String line : results) {
            Label row = new Label(line);
            row.getStyleClass().add("result-row");
            resultsBox.getChildren().add(row);
        }
    }

    /** Only enables the buttons that make sense for the current state. */
    private void updateButtons() {
        startButton.setDisable(state == State.RUNNING || state == State.FINISHED);
        pauseButton.setDisable(state != State.RUNNING);
    }
}