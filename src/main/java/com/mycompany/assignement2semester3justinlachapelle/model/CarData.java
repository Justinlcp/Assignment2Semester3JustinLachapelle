package com.mycompany.assignement2semester3justinlachapelle.model;

/**
 * Holds the info for one car (type, number, color, position, speed).
 * No JavaFX in here, it is only data.
 *
 * @author Justin Lachapelle
 */
public class CarData {

    private final String type;       // like "Sports Coupe"
    private final int number;        // number on the car
    private final String colorName;  // like "Yellow", used in the messages
    private final String colorHex;   // like "#FFD600", used to color the car
    private final String imagePath;  // image for the slideshow

    private double x;                // current position (left side of the car)
    private double y;
    private double speed;            // pixels per second
    private boolean finished;        // true once it crosses the finish line

    /**
     * Makes a new car.
     *
     * @param type      type of car
     * @param number    car number
     * @param colorName name of the color
     * @param colorHex  color code used to paint it
     * @param imagePath path of the slideshow image
     */
    public CarData(String type, int number, String colorName, String colorHex, String imagePath) {
        this.type = type;
        this.number = number;
        this.colorName = colorName;
        this.colorHex = colorHex;
        this.imagePath = imagePath;
    }

    public String getType() {
        return type;
    }

    public int getNumber() {
        return number;
    }

    public String getColorName() {
        return colorName;
    }

    public String getColorHex() {
        return colorHex;
    }

    public String getImagePath() {
        return imagePath;
    }
    

    /**
     * Short name for the messages, like "Car #88 (Yellow)".
     *
     * @return the label
     */
    public String getLabel() {
        return "Car #" + number + " (" + colorName + ")";
    }
}