package model;

import java.util.Objects;

public class statisticProducts {
    private String machineCode;
    private String machineName;
    private int receiptQuantity;
    private int deliveryQuantity;

    public statisticProducts(String machineCode, String machineName, int receiptQuantity, int deliveryQuantity) {
        this.machineCode = machineCode;
        this.machineName = machineName;
        this.receiptQuantity = receiptQuantity;
        this.deliveryQuantity = deliveryQuantity;
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

    public int getReceiptQuantity() {
        return receiptQuantity;
    }

    public void setReceiptQuantity(int receiptQuantity) {
        this.receiptQuantity = receiptQuantity;
    }

    public int getDeliveryQuantity() {
        return deliveryQuantity;
    }

    public void setDeliveryQuantity(int deliveryQuantity) {
        this.deliveryQuantity = deliveryQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        statisticProducts that = (statisticProducts) o;
        return receiptQuantity == that.receiptQuantity && deliveryQuantity == that.deliveryQuantity && Objects.equals(machineCode, that.machineCode) && Objects.equals(machineName, that.machineName);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.machineCode);
        hash = 53 * hash + Objects.hashCode(this.machineName);
        hash = 53 * hash + this.receiptQuantity;
        hash = 53 * hash + this.deliveryQuantity;
        return hash;
    }
}
