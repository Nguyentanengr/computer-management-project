package controller;

import dao.SupplierDAO;
import model.Supplier;

import java.util.ArrayList;

public class SearchSupplier {
    public static SearchSupplier getInstance() {
        return new SearchSupplier();
    }

    public ArrayList<Supplier> searchAll(String text) {
        ArrayList<Supplier> result = new ArrayList<>();
        ArrayList<Supplier> listSupplier = SupplierDAO.getInstance().selectAll();
        for (var supplier : listSupplier) {
            if (supplier.getSupplierCode().toLowerCase().contains(text.toLowerCase())
                    || supplier.getSupplierName().toLowerCase().contains(text.toLowerCase())
                    || supplier.getPhone().toLowerCase().contains(text.toLowerCase())
                    || supplier.getAddress().toLowerCase().contains(text.toLowerCase())) {
                result.add(supplier);
            }
        }
        return result;
    }

    public ArrayList<Supplier> searchSupplierName(String text) {
        ArrayList<Supplier> result = new ArrayList<>();
        ArrayList<Supplier> listSupplier = SupplierDAO.getInstance().selectAll();
        for (var supplier : listSupplier) {
            if (supplier.getSupplierName().toLowerCase().contains(text.toLowerCase())) {
                result.add(supplier);
            }
        }
        return result;
    }

    public ArrayList<Supplier> searchSupplierCode(String text) {
        ArrayList<Supplier> result = new ArrayList<>();
        ArrayList<Supplier> listSupplier = SupplierDAO.getInstance().selectAll();
        for (var supplier : listSupplier) {
            if (supplier.getSupplierCode().toLowerCase().contains(text.toLowerCase())) {
                result.add(supplier);
            }
        }
        return result;
    }

    public ArrayList<Supplier> searchAddress(String text) {
        ArrayList<Supplier> result = new ArrayList<>();
        ArrayList<Supplier> listSupplier = SupplierDAO.getInstance().selectAll();
        for (var supplier : listSupplier) {
            if (supplier.getAddress().toLowerCase().contains(text.toLowerCase())) {
                result.add(supplier);
            }
        }
        return result;
    }

    public ArrayList<Supplier> searchPhone(String text) {
        ArrayList<Supplier> result = new ArrayList<>();
        ArrayList<Supplier> listSupplier = SupplierDAO.getInstance().selectAll();
        for (var supplier : listSupplier) {
            if (supplier.getPhone().toLowerCase().contains(text.toLowerCase())) {
                result.add(supplier);
            }
        }
        return result;
    }
}
