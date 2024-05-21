package model;

import java.util.Objects;

public class Computer {
    private String machineCode;
    private String machineName;
    private int quantity;
    private String CPUName;
    private String RAM;
    private String graphicsCard;
    private String ROM;
    private double price;
    private String origin;
    private int status;

    public Computer(String machineCode, String machineName, int quantity, String CPUName, String RAM, String graphicsCard, String ROM, double price, String origin, int status) {
        this.machineCode = machineCode;
        this.machineName = machineName;
        this.quantity = quantity;
        this.CPUName = CPUName;
        this.RAM = RAM;
        this.graphicsCard = graphicsCard;
        this.ROM = ROM;
        this.price = price;
        this.origin = origin;
        this.status = status;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCPUName() {
        return CPUName;
    }

    public void setCPUName(String CPUName) {
        this.CPUName = CPUName;
    }

    public String getRAM() {
        return RAM;
    }

    public void setRAM(String RAM) {
        this.RAM = RAM;
    }

    public String getGraphicsCard() {
        return graphicsCard;
    }

    public void setGraphicsCard(String graphicsCard) {
        this.graphicsCard = graphicsCard;
    }

    public String getRom() {
        return ROM;
    }

    public void setRom(String ROM) {
        this.ROM = ROM;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void productDelivery(int quantity) {
        this.quantity -= quantity;
    }



    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.machineCode);
        hash = 37 * hash + Objects.hashCode(this.machineName);
        hash = 37 * hash + this.quantity;
        hash = 37 * hash + (int) (Double.doubleToLongBits(this.price) ^ (Double.doubleToLongBits(this.price) >>> 32));
        hash = 37 * hash + Objects.hashCode(this.CPUName);
        hash = 37 * hash + Objects.hashCode(this.RAM);
        hash = 37 * hash + Objects.hashCode(this.origin);
        hash = 37 * hash + Objects.hashCode(this.graphicsCard);
        hash = 37 * hash + Objects.hashCode(this.ROM);
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Computer computer = (Computer) o;
        return quantity == computer.quantity && Double.compare(price, computer.price) == 0 && status == computer.status && Objects.equals(machineCode, computer.machineCode) && Objects.equals(machineName, computer.machineName) && Objects.equals(CPUName, computer.CPUName) && Objects.equals(RAM, computer.RAM) && Objects.equals(graphicsCard, computer.graphicsCard) && Objects.equals(ROM, computer.ROM) && Objects.equals(origin, computer.origin);
    }
}
