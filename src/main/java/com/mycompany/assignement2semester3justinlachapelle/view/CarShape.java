package com.mycompany.assignement2semester3justinlachapelle.view;

import com.mycompany.assignement2semester3justinlachapelle.model.CarData;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * One car made only with JavaFX shapes (no images or gifs).
 * It extends Group, so when the group moves all the shapes move with it.
 * The car is about 100 pixels wide and 42 tall.
 *
 * @author Justin Lachapelle
 */
public class CarShape extends Group {

    public static final double WHEEL_RADIUS = 8;

    private final Group rearWheel;
    private final Group frontWheel;

    /**
     * Builds the car.
     *
     * @param car the car data (we use its color and number)
     */
    public CarShape(CarData car) {
        Color bodyColor = Color.web(car.getColorHex());

        // Polygon for the body
        Polygon body = new Polygon(0, 34, 0, 22, 10, 16, 90, 16, 100, 24, 100, 34);
        body.setFill(bodyColor);

        // Polygon for the roof
        Polygon roof = new Polygon(25, 16, 38, 2, 70, 2, 82, 16);
        roof.setFill(bodyColor.darker());

        // two Rectangles for the windows
        Rectangle window1 = new Rectangle(40, 5, 14, 9);
        Rectangle window2 = new Rectangle(57, 5, 14, 9);
        window1.setFill(Color.LIGHTSKYBLUE);
        window2.setFill(Color.LIGHTSKYBLUE);

        // white plate with the number (Rectangle + Text)
        Rectangle plate = new Rectangle(38, 20, 26, 11);
        plate.setFill(Color.WHITE);
        plate.setArcWidth(4);
        plate.setArcHeight(4);

        Text numberText = new Text(String.valueOf(car.getNumber()));
        numberText.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        numberText.setX(51 - numberText.getLayoutBounds().getWidth() / 2);
        numberText.setY(29.5);

        // little lights, front and back
        Rectangle headlight = new Rectangle(95, 22, 5, 4);
        headlight.setFill(Color.LIGHTYELLOW);
        Rectangle tailLight = new Rectangle(0, 22, 4, 4);
        tailLight.setFill(Color.ORANGERED);

        // the two wheels (each one is a small Group so it can spin)
        rearWheel = createWheel(22);
        frontWheel = createWheel(78);

        getChildren().addAll(body, roof, window1, window2, plate, numberText,
                headlight, tailLight, rearWheel, frontWheel);
    }

    /**
     * Makes one wheel: black tire, gray center and a small line from the
     * center to the edge. The line is there so you can see the wheel turning.
     *
     * @param centerX x position of the wheel on the car
     * @return the wheel
     */
    private Group createWheel(double centerX) {
        Circle tire = new Circle(0, 0, WHEEL_RADIUS, Color.rgb(30, 30, 30));
        Circle hub = new Circle(0, 0, WHEEL_RADIUS - 3, Color.LIGHTGRAY);
        Line spoke = new Line(0, 0, 0, -(WHEEL_RADIUS - 3));
        spoke.setStroke(Color.DARKSLATEGRAY);
        spoke.setStrokeWidth(1.5);

        Group wheel = new Group(tire, hub, spoke);
        wheel.setLayoutX(centerX);
        wheel.setLayoutY(34);
        return wheel;
    }

    public Group getRearWheel() {
        return rearWheel;
    }

    public Group getFrontWheel() {
        return frontWheel;
    }

    /**
     * Puts the car and the wheels back like at the start.
     */
    public void resetLook() {
        setTranslateX(0);
        rearWheel.setRotate(0);
        frontWheel.setRotate(0);
    }
}