package dao;

import com.mysql.cj.jdbc.ConnectionImpl;
import com.mysql.cj.jdbc.JdbcConnection;
import database.JDBCUtil;
import model.Supplier;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SupplierDAO implements DAOinterface<Supplier> {

    public static SupplierDAO getInstance() {
        return new SupplierDAO();
    }

    @Override
    public int insert(Supplier supplier) {
        int affectedRows = 0;
        String insertQuery = "INSERT INTO nhacungcap (maNhaCungCap, tenNhaCungCap, Sdt, diaChi) VALUES(?,?,?,?)";
        try (Connection con = JDBCUtil.getConnection()) {
            PreparedStatement pst = con.prepareStatement(insertQuery);
            pst.setString(1, supplier.getSupplierCode());
            pst.setString(2, supplier.getSupplierName());
            pst.setString(3, supplier.getPhone());
            pst.setString(4, supplier.getAddress());
            affectedRows = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
        }
        return affectedRows;
    }

    @Override
    public int update(Supplier supplier) {
        int affectedRows = 0;
        String updateQuery = "UPDATE NhaCungCap SET maNhaCungCap=?, tenNhaCungCap=?, Sdt=?, diaChi=? WHERE maNhaCungCap=?";
        try (Connection con = JDBCUtil.getConnection()) {
            PreparedStatement pst = con.prepareStatement(updateQuery);
            pst.setString(1, supplier.getSupplierCode());
            pst.setString(2, supplier.getSupplierName());
            pst.setString(3, supplier.getPhone());
            pst.setString(4, supplier.getAddress());
            pst.setString(5, supplier.getSupplierCode());
            affectedRows = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Không cập nhật được nhà cung cấp " + supplier.getSupplierCode(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return affectedRows;
    }

    @Override
    public int delete(Supplier supplier) {
        int affectedRows = 0;
        String deleteQuery = "DELETE FROM NhaCungCap WHERE maNhaCungCap=?";
        try (Connection con = JDBCUtil.getConnection()) {
            PreparedStatement pst = con.prepareStatement(deleteQuery);
            pst.setString(1, supplier.getSupplierCode());
            affectedRows = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Không xóa được nhà cung cấp " + supplier.getSupplierCode(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return affectedRows;
    }

    @Override
    public ArrayList<Supplier> selectAll() {
        ArrayList<Supplier> listSupplier = new ArrayList<>();
        String selectQuery = "SELECT * FROM NhaCungCap";
        try (Connection con = JDBCUtil.getConnection()) {
            PreparedStatement pst = con.prepareStatement(selectQuery);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String supplierCode = rs.getString("maNhaCungCap");
                String supplierName = rs.getString("tenNhaCungCap");
                String phone = rs.getString("Sdt");
                String address = rs.getString("diaChi");
                Supplier supplier = new Supplier(supplierCode, supplierName, phone, address);
                listSupplier.add(supplier);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi khi lấy lấy thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return listSupplier;
    }

    @Override
    public Supplier selectById(String id) {
        Supplier supplier = null;
        String selectQuery = "SELECT * FROM NhaCungCap WHERE maNhaCungCap=? ";
        try (Connection con = JDBCUtil.getConnection()) {
            PreparedStatement pst = con.prepareStatement(selectQuery);
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String supplierCode = rs.getString("maNhaCungCap");
                String supplierName = rs.getString("tenNhaCungCap");
                String phone = rs.getString("Sdt");
                String address = rs.getString("diaChi");
                supplier = new Supplier(supplierCode, supplierName, phone, address);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi khi lấy lấy thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return supplier;
    }
}

