package model;

import java.util.Objects;

public class DesktopComputer extends Computer{
    private String mainboard;
    private int powerSupply;

    public DesktopComputer(String machineCode, String machineName, int quantity, String CPUName, String RAM, String graphicsCard, String ROM, double price, String origin, int status, String mainboard, int powerSupply) {
        super(machineCode, machineName, quantity, CPUName, RAM, graphicsCard, ROM, price, origin, status);
        this.mainboard = mainboard;
        this.powerSupply = powerSupply;
    }

    public String getMainboard() {
        return mainboard;
    }

    public void setMainboard(String mainboard) {
        this.mainboard = mainboard;
    }

    public int getPowerSupply() {
        return powerSupply;
    }

    public void setPowerSupply(int powerSupply) {
        this.powerSupply = powerSupply;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.mainboard);
        hash = 43 * hash + Objects.hashCode(this.powerSupply);
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DesktopComputer that = (DesktopComputer) o;
        return powerSupply == that.powerSupply && Objects.equals(mainboard, that.mainboard);
    }
}
