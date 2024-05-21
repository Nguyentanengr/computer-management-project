package model;

import java.util.Objects;

public class Supplier {
    private String supplierCode;
    private String supplierName;
    private String phone;
    private String address;

    public Supplier(String supplierCode, String supplierName, String phone, String address) {
        this.supplierCode = supplierCode;
        this.supplierName = supplierName;
        this.phone = phone;
        this.address = address;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Supplier supplier = (Supplier) o;
        return Objects.equals(supplierCode, supplier.supplierCode) && Objects.equals(supplierName, supplier.supplierName) && Objects.equals(phone, supplier.phone) && Objects.equals(address, supplier.address);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.supplierCode);
        hash = 29 * hash + Objects.hashCode(this.supplierName);
        hash = 29 * hash + Objects.hashCode(this.phone);
        hash = 29 * hash + Objects.hashCode(this.address);
        return hash;
    }

    public String toString() {
        return "NhaCungCap{" + "maNhaCungCap=" + supplierCode + ", tenNhaCungCap=" + supplierName + ", sdt=" + phone + ", diaChi=" + address + '}';
    }
}
