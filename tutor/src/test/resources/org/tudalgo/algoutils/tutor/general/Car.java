package org.tudalgo.algoutils.tutor.general;

public class Car {
    private String brand;
    private String model;
    private int year;

    public Car(String brand, String model, int year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

    public void startEngine() {
        System.out.println("Starting the engine of " + brand + " " + model);
    }

    public void accelerate() {
        System.out.println("Accelerating " + brand + " " + model);
    }

    public void brake() {
        System.out.println("Applying brakes to " + brand + " " + model);
    }
}
