package dao;

import database.JDBCUtil;
import model.DeliveryBill;

import java.sql.*;
import java.util.ArrayList;

public class DeliveryBillDAO implements DAOinterface<DeliveryBill> {

    public static DeliveryBillDAO getInstance() {
        return new DeliveryBillDAO();
    }
    @Override
    public int insert(DeliveryBill deliveryBill) {
        int affectedRows = 0;
        String insertQuery = "INSERT INTO phieuxuat (maPhieu, thoiGianTao, nguoiTao, tongTien) VALUES (?, ?, ?, ?)";
        try (Connection con = JDBCUtil.getConnection()){
            PreparedStatement pst = con.prepareStatement(insertQuery);
            pst.setString(1, deliveryBill.getBillCode());
            pst.setTimestamp(2, deliveryBill.getCreationTime());
            pst.setString(3, deliveryBill.getCreator());
            pst.setDouble(4, deliveryBill.getTotalAmount());
            affectedRows = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    @Override
    public int update(DeliveryBill deliveryBill) {
        int affectedRows = 0;
        String insertQuery = "UPDATE phieuxuat SET maPhieu = ?, thoiGianTao = ?, nguoiTao = ?, tongTien = ? WHERE maPhieu = ?";
        try (Connection con = JDBCUtil.getConnection()){
            PreparedStatement pst = con.prepareStatement(insertQuery);
            pst.setString(1, deliveryBill.getBillCode());
            pst.setTimestamp(2, deliveryBill.getCreationTime());
            pst.setString(3, deliveryBill.getCreator());
            pst.setDouble(4, deliveryBill.getTotalAmount());
            pst.setString(5, deliveryBill.getBillCode());
            affectedRows = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    @Override
    public int delete(DeliveryBill deliveryBill) {
        int affectedRows = 0;
        String insertQuery = "DELETE FROM phieuxuat WHERE maPhieu = ?";
        try (Connection con = JDBCUtil.getConnection()){
            PreparedStatement pst = con.prepareStatement(insertQuery);
            pst.setString(1, deliveryBill.getBillCode());
            affectedRows = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    @Override
    public ArrayList<DeliveryBill> selectAll() {
        ArrayList<DeliveryBill> listDeliveryBill = new ArrayList<>();
        String selectQuery = "SELECT * FROM phieuxuat ORDER BY thoiGianTao DESC";
        try (Connection con = JDBCUtil.getConnection()){
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(selectQuery);

            while (rs.next()) {
                String billCode = rs.getString( "maPhieu");
                Timestamp timestamp = rs.getTimestamp("thoiGianTao");
                String creator = rs.getString("nguoiTao");
                Double totalPrice = rs.getDouble("tongTien");

                DeliveryBill deliveryBill = new DeliveryBill(billCode, timestamp, creator, DeliveryBillDetailsDAO.getInstance().selectBy(billCode), totalPrice);
                listDeliveryBill.add(deliveryBill);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listDeliveryBill;
    }

    @Override
    public DeliveryBill selectById(String id) {
        DeliveryBill deliveryBill = null;
        String selectQuery = "SELECT * FROM phieuxuat WHERE maPhieu = ?";
        try (Connection con = JDBCUtil.getConnection()){
            PreparedStatement pst = con.prepareStatement(selectQuery);
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String billCode = rs.getString( "maPhieu");
                Timestamp timestamp = rs.getTimestamp("thoiGianTao");
                String creator = rs.getString("nguoiTao");
                Double totalPrice = rs.getDouble("tongTien");
                deliveryBill = new DeliveryBill(billCode, timestamp, creator, DeliveryBillDetailsDAO.getInstance().selectBy(billCode), totalPrice);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deliveryBill;
    }

    public ArrayList<DeliveryBill> filter(String option, String content, Timestamp fromDate, Timestamp toDate, double fromPrice, double toPrice) {
        ArrayList<DeliveryBill> listDeliveryBill = new ArrayList<>();

        String selectQuery = "SELECT * FROM phieuxuat WHERE (thoiGianTao BETWEEN ? AND ?) AND (tongTien BETWEEN ? AND ?)";

        if (option.equals("Mã phiếu")) {
            selectQuery += "AND (LOWER(maPhieu) LIKE ?)";
        } else if (option.equals("Người tạo")) {
            selectQuery += "AND (LOWER(nguoiTao) LIKE ?)";
        } else if (option.equals("Tất cả")) {
            selectQuery += "AND (LOWER(maPhieu) LIKE ? OR LOWER(nguoiTao) LIKE ?)";
        }

        try (Connection con = JDBCUtil.getConnection()) {
            PreparedStatement pst = con.prepareStatement(selectQuery);
            pst.setTimestamp(1, fromDate);
            pst.setTimestamp(2, toDate);
            pst.setDouble(3, fromPrice);
            pst.setDouble(4, toPrice);
            pst.setString(5, "%" + content.toLowerCase() + "%");
            if (option.equals("Tất cả")) pst.setString(6, "%" + content.toLowerCase() + "%");

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String billCode = rs.getString("maPhieu");
                Timestamp creationTime = rs.getTimestamp("thoiGianTao");
                String creator = rs.getString("nguoiTao");
                double totalAmount = rs.getDouble("tongTien");


                DeliveryBill deliveryBill = new DeliveryBill(billCode, creationTime, creator, null, totalAmount);
                listDeliveryBill.add(deliveryBill);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listDeliveryBill;
    }
}
