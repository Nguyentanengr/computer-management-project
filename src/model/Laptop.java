package model;

import java.util.Objects;

public class Laptop extends Computer{
    private double screenSize;
    private String batteryCapacity;

    public Laptop(String machineCode, String machineName, int quantity, String CPUName, String RAM, String graphicsCard, String ROM, double price, String origin, int status, double screenSize, String batteryCapacity) {
        super(machineCode, machineName, quantity, CPUName, RAM, graphicsCard, ROM, price, origin, status);
        this.screenSize = screenSize;
        this.batteryCapacity = batteryCapacity;
    }

    public double getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(double screenSize) {
        this.screenSize = screenSize;
    }

    public String getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(String batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Laptop laptop = (Laptop) o;
        return Double.compare(screenSize, laptop.screenSize) == 0 && Objects.equals(batteryCapacity, laptop.batteryCapacity);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.screenSize);
        hash = 67 * hash + Objects.hashCode(this.batteryCapacity);
        return hash;
    }
}
