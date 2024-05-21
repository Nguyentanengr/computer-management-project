package dao;

import database.JDBCUtil;
import jxl.write.DateTime;
import model.BillDetails;
import model.ReceiptBill;

import java.sql.*;
import java.util.ArrayList;

public class ReceiptBillDAO implements DAOinterface<ReceiptBill> {

    public static ReceiptBillDAO getInstance() {
        return new ReceiptBillDAO();
    }
    @Override
    public int insert(ReceiptBill receiptBill) {
        int affectedRows = 0;
        String insertQuery = "INSERT INTO phieunhap (maPhieu, thoiGianTao, nguoiTao, maNhaCungCap, tongTien) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = JDBCUtil.getConnection()){
            PreparedStatement pst = con.prepareStatement(insertQuery);
            pst.setString(1, receiptBill.getBillCode());
            pst.setTimestamp(2, receiptBill.getCreationTime());
            pst.setString(3, receiptBill.getCreator());
            pst.setString(4, receiptBill.getSupplier());
            pst.setDouble(5, receiptBill.getTotalAmount());
            affectedRows = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    @Override
    public int update(ReceiptBill receiptBill) {
        int affectedRows = 0;
        String insertQuery = "UPDATE phieunhap SET maPhieu = ?, thoiGianTao = ?, nguoiTao = ?, maNhaCungCap = ?, tongTien = ? WHERE maPhieu = ?";
        try (Connection con = JDBCUtil.getConnection()){
            PreparedStatement pst = con.prepareStatement(insertQuery);
            pst.setString(1, receiptBill.getBillCode());
            pst.setTimestamp(2, receiptBill.getCreationTime());
            pst.setString(3, receiptBill.getCreator());
            pst.setString(4, receiptBill.getSupplier());
            pst.setDouble(5, receiptBill.getTotalAmount());
            pst.setString(6, receiptBill.getBillCode());
            affectedRows = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    @Override
    public int delete(ReceiptBill receiptBill) {
        int affectedRows = 0;
        String insertQuery = "DELETE FROM phieunhap WHERE maPhieu = ?";
        try (Connection con = JDBCUtil.getConnection()){
            PreparedStatement pst = con.prepareStatement(insertQuery);
            pst.setString(1, receiptBill.getBillCode());
            affectedRows = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    @Override
    public ArrayList<ReceiptBill> selectAll() {
        ArrayList<ReceiptBill> listReceiptBill = new ArrayList<>();
        String selectQuery = "SELECT * FROM phieuNhap ORDER BY thoiGianTao DESC";
        try (Connection con = JDBCUtil.getConnection()){
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(selectQuery);

            while (rs.next()) {
                String billCode = rs.getString( "maPhieu");
                Timestamp timestamp = rs.getTimestamp("thoiGianTao");
                String creator = rs.getString("nguoiTao");
                String supplierCode = rs.getString("maNhaCungCap");
                Double totalPrice = rs.getDouble("tongTien");

                ReceiptBill receiptBill = new ReceiptBill(billCode, timestamp, creator, ReceiptBillDetailsDAO.getInstance().selectBy(billCode), totalPrice, supplierCode);
                listReceiptBill.add(receiptBill);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listReceiptBill;
    }

    @Override
    public ReceiptBill selectById(String id) {
        ReceiptBill receiptBill = null;
        String selectQuery = "SELECT * FROM phieunhap WHERE maPhieu = ?";
        try (Connection con = JDBCUtil.getConnection()){
            PreparedStatement pst = con.prepareStatement(selectQuery);
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String billCode = rs.getString( "maPhieu");
                Timestamp timestamp = rs.getTimestamp("thoiGianTao");
                String creator = rs.getString("nguoiTao");
                String supplierCode = rs.getString("maNhaCungCap");
                Double totalPrice = rs.getDouble("tongTien");
                receiptBill = new ReceiptBill(billCode, timestamp, creator, ReceiptBillDetailsDAO.getInstance().selectBy(billCode), totalPrice, supplierCode);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receiptBill;
    }

    public ArrayList<ReceiptBill> filter(String option, String content, Timestamp fromDate, Timestamp toDate, double formPrice, double toPrice) {
        ArrayList<ReceiptBill> listReceiptBill = new ArrayList<>();
        String selectQuery = "SELECT * FROM phieunhap WHERE (thoiGianTao BETWEEN ? AND ?) AND (tongTien BETWEEN ? AND ?)";

        if (option.equals("Mã phiếu")) {
            selectQuery += "AND (LOWER(maPhieu) LIKE ?)";
        } else if (option.equals("Người tạo")) {
            selectQuery += "AND (LOWER(nguoiTao) LIKE ?)";
        } else if (option.equals("Nhà cung cấp")) {
            selectQuery += "AND (maNhaCungCap IN (SELECT maNhaCungCap FROM nhacungcap WHERE LOWER(tenNhaCungCap) LIKE ?))";
        } else if (option.equals("Tất cả")) {
            selectQuery += "AND (LOWER(maPhieu) LIKE ? OR LOWER(nguoiTao) LIKE ? OR maNhaCungCap IN (SELECT maNhaCungCap FROM nhacungcap WHERE LOWER(tenNhaCungCap) LIKE ?))";
        }
        try (Connection con = JDBCUtil.getConnection()) {
            PreparedStatement pst = con.prepareStatement(selectQuery);
            pst.setTimestamp(1, fromDate);
            pst.setTimestamp(2, toDate);
            pst.setDouble(3, formPrice);
            pst.setDouble(4, toPrice);
            pst.setString(5, "%" + content.toLowerCase() + "%");

            if (option.equals("Tất cả")) {
                pst.setString(6, "%" + content.toLowerCase() + "%");
                pst.setString(7, "%" + content.toLowerCase() + "%");
            }

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String billCode = rs.getString("maPhieu");
                Timestamp creationTime = rs.getTimestamp("thoiGianTao");
                String creator = rs.getString("nguoiTao");
                String supplierCode = rs.getString("maNhaCungCap");
                double totalAmount = rs.getDouble("tongTien");

                ReceiptBill receiptBill = new ReceiptBill(billCode, creationTime, creator, null, totalAmount, supplierCode);
                listReceiptBill.add(receiptBill);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listReceiptBill;
    }

}
