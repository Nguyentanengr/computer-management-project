package dao;

import database.JDBCUtil;
import model.BillDetails;

import java.sql.*;
import java.util.ArrayList;

public class ReceiptBillDetailsDAO implements DAOinterface<BillDetails> {

    public static ReceiptBillDetailsDAO getInstance() {
        return new ReceiptBillDetailsDAO();
    }
    @Override
    public int insert(BillDetails billDetails) {
        int affectedRows = 0;
        String insertQuery = "INSERT INTO chitietphieunhap (maPhieu, maMay, soLuong, donGia) VALUES(?, ?, ?, ?)";
        try (Connection con = JDBCUtil.getConnection()){
            PreparedStatement pst = con.prepareStatement(insertQuery);
            pst.setString(1, billDetails.getBillCode());
            pst.setString(2, billDetails.getMachineCode());
            pst.setInt(3, billDetails.getQuantity());
            pst.setDouble(4, billDetails.getUnitPrice());
            affectedRows = pst.executeUpdate();
            JDBCUtil.closeConnection(con);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    @Override
    public int update(BillDetails billDetails) {
        int affectedRows = 0;
        String updateQuery = "UPDATE chitietphieunhap SET maPhieu = ?, maMay = ?, soLuong = ?, donGia = ? WHERE maPhieu = ? AND maMay = ?";
        try (Connection con = JDBCUtil.getConnection()){
            PreparedStatement pst = con.prepareStatement(updateQuery);
            pst.setString(1, billDetails.getBillCode());
            pst.setString(2, billDetails.getMachineCode());
            pst.setInt(3, billDetails.getQuantity());
            pst.setDouble(4, billDetails.getUnitPrice());
            pst.setString(5, billDetails.getBillCode());
            pst.setString(6, billDetails.getMachineCode());
            affectedRows = pst.executeUpdate();
            JDBCUtil.closeConnection(con);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    @Override
    public int delete(BillDetails billDetails) {
        int affectedRows = 0;
        String deleteQuery = "DELETE FROM chitietphieunhap WHERE maPhieu = ? AND maMay = ?";
        try (Connection con = JDBCUtil.getConnection()){
            PreparedStatement pst = con.prepareStatement(deleteQuery);
            pst.setString(1, billDetails.getBillCode());
            pst.setString(2, billDetails.getMachineCode());
            affectedRows = pst.executeUpdate();
            JDBCUtil.closeConnection(con);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    @Override
    public ArrayList<BillDetails> selectAll() {
        ArrayList<BillDetails> listBillDetails = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection()){
            String sql = "SELECT * FROM ChiTietPhieuNhap";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String billCode = rs.getString("maPhieu");
                String machineCode = rs.getString("maMay");
                int quantity = rs.getInt("soLuong");
                double price = rs.getDouble("donGia");
                BillDetails billDetails = new BillDetails(billCode, machineCode, quantity, price);
                listBillDetails.add(billDetails);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return listBillDetails;
    }

    @Override
    public BillDetails selectById(String id) {
        return null;
    }

    public ArrayList<BillDetails> selectBy(String billCode) {
        ArrayList<BillDetails> listBillDetails = new ArrayList<>();
        String selectQuery = "SELECT * FROM chitietphieunhap WHERE maPhieu = ?";
        try (Connection con = JDBCUtil.getConnection()){
            PreparedStatement pst = con.prepareStatement(selectQuery);
            pst.setString(1, billCode);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String code = rs.getString( "maPhieu");
                String machineCode = rs.getString("maMay");
                int quantity = rs.getInt("soLuong");
                Double price = rs.getDouble("donGia");
                BillDetails billDetails = new BillDetails(code, machineCode, quantity, price);
                listBillDetails.add(billDetails);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listBillDetails;
    }

    public int selectQuantityBy(String machineCode) {
        int quantity = 0;
        String selectQuery = "SELECT SUM(soLuong) AS quantity FROM chitietphieunhap WHERE maMay = ?";
        try (Connection con = JDBCUtil.getConnection()) {
            PreparedStatement pst = con.prepareStatement(selectQuery);
            pst.setString(1, machineCode);


            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                quantity += rs.getInt("quantity");
                if (rs.wasNull()) {
                    quantity = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quantity;
    }
}
