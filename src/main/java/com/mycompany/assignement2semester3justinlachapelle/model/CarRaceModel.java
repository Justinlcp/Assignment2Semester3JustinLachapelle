package com.mycompany.assignement2semester3justinlachapelle.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Model part of the MVC. It keeps the race data: the cars, their positions
 * and speeds, and who finished in which order. It knows nothing about the
 * buttons or the shapes.
 *
 * @author Justin Lachapelle
 */
public class CarRaceModel {

    // Track sizes in pixels. The controller uses these too.
    public static final double START_X = 50;       // where the cars start
    public static final double FINISH_X = 860;     // finish line
    public static final double CAR_WIDTH = 100;
    public static final double TRACK_TOP = 10;
    public static final double LANE_HEIGHT = 70;
    public static final double CAR_OFFSET_Y = 14;  // puts the car in the middle of its lane

    private static final double MIN_SPEED = 110;
    private static final double MAX_SPEED = 170;

    private final List<CarData> cars = new ArrayList<>();
    private final List<CarData> finishOrder = new ArrayList<>();
    private final Random random = new Random();

    /**
     * Creates the 4 cars and sends them to the start.
     */
    public CarRaceModel() {
        cars.add(new CarData("Sports Coupe", 88, "Yellow", "#FFD600", "/images/car88.png"));
        cars.add(new CarData("Classic Sedan", 45, "Red", "#E53935", "/images/car45.png"));
        cars.add(new CarData("Hatchback", 78, "Green", "#43A047", "/images/car78.png"));
        cars.add(new CarData("Rally Car", 23, "Blue", "#1E88E5", "/images/car23.png"));
        resetRace();
    }

    /**
     * Puts the cars back at the start with new random speeds, so the winner
     * is not always the same.
     */
    public void resetRace() {
        finishOrder.clear();
        for (int i = 0; i < cars.size(); i++) {
            CarData car = cars.get(i);
            car.setX(START_X);
            car.setY(TRACK_TOP + i * LANE_HEIGHT + CAR_OFFSET_Y);
            car.setSpeed(MIN_SPEED + random.nextDouble() * (MAX_SPEED - MIN_SPEED));
            car.setFinished(false);
        }
    }

    /**
     * How far a car has to go until its front touches the finish line.
     *
     * @return the distance in pixels
     */
    public double getRaceDistance() {
        return FINISH_X - START_X - CAR_WIDTH;
    }

    /**
     * Updates the x position of a car.
     *
     * @param car     the car
     * @param movedBy how many pixels it moved since the start
     */
    public void updateCarPosition(CarData car, double movedBy) {
        car.setX(START_X + movedBy);
    }

    /**
     * Saves that a car crossed the finish line. The first one saved is the winner.
     *
     * @param car the car that finished
     */
    public void recordFinish(CarData car) {
        if (!car.isFinished()) {
            car.setFinished(true);
            finishOrder.add(car);
        }
    }

    public boolean isRaceOver() {
        return finishOrder.size() == cars.size();
    }

    /**
     * Gives the winner.
     *
     * @return the first car to finish, or null if none finished yet
     */
    public CarData getWinner() {
        return finishOrder.isEmpty() ? null : finishOrder.get(0);
    }

    public List<CarData> getFinishOrder() {
        return Collections.unmodifiableList(finishOrder);
    }

    public List<CarData> getCars() {
        return Collections.unmodifiableList(cars);
    }
}