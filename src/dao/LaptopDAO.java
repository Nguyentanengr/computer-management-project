package dao;

import database.JDBCUtil;
import model.DesktopComputer;
import model.Laptop;

import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class LaptopDAO implements DAOinterface<Laptop> {
    public static LaptopDAO getInstance() {
        return new LaptopDAO();
    }


    @Override
    public int insert(Laptop laptop) {
        int affectedRows = 0;
        String insertQuery = "INSERT INTO maytinh (maMay, tenMay, soLuong, tenCpu, ram, cardManHinh, gia, loaiMay, rom, kichThuocMan, dungLuongPin, xuatXu, trangThai) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection con = JDBCUtil.getConnection();
            PreparedStatement pst = con.prepareStatement(insertQuery);
            pst.setString(1, laptop.getMachineCode());
            pst.setString(2, laptop.getMachineName());
            pst.setInt(3, laptop.getQuantity());
            pst.setString(4, laptop.getCPUName());
            pst.setString(5, laptop.getRAM());
            pst.setString(6, laptop.getGraphicsCard());
            pst.setDouble(7, laptop.getPrice());
            pst.setString(8, "Laptop");
            pst.setString(9, laptop.getRom());
            pst.setDouble(10, laptop.getScreenSize());
            pst.setString(11, laptop.getBatteryCapacity());
            pst.setString(12, laptop.getOrigin());
            pst.setInt(13, laptop.getStatus());

            affectedRows = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Không thêm được " + laptop.getMachineCode(),"Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return affectedRows;
    }

    @Override
    public int update(Laptop laptop) {
        int affectedRows = 0;
        String insertQuery = "UPDATE maytinh SET tenMay = ?, soLuong = ?, tenCpu = ?, ram = ?, cardManHinh = ?, gia = ?, loaiMay = ?, rom = ?, kichThuocMan = ?, dungLuongPin = ?, xuatXu = ?, trangThai = ? WHERE maMay = ?";
        try {
            Connection con = JDBCUtil.getConnection();
            PreparedStatement pst = con.prepareStatement(insertQuery);
            pst.setString(1, laptop.getMachineName());
            pst.setInt(2, laptop.getQuantity());
            pst.setString(3, laptop.getCPUName());
            pst.setString(4, laptop.getRAM());
            pst.setString(5, laptop.getGraphicsCard());
            pst.setDouble(6, laptop.getPrice());
            pst.setString(7, "Laptop");
            pst.setString(8, laptop.getRom());
            pst.setDouble(9, laptop.getScreenSize());
            pst.setString(10, laptop.getBatteryCapacity());
            pst.setString(11, laptop.getOrigin());
            pst.setInt(12, laptop.getStatus());
            pst.setString(13, laptop.getMachineCode());

            affectedRows = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    @Override
    public int delete(Laptop laptop) {
        int affectedRow = 0;
        String deleteQuery = "DELETE FROM MayTinh WHERE maMay=? ";
        try {
            Connection con = JDBCUtil.getConnection();
            PreparedStatement pst = con.prepareStatement(deleteQuery);
            pst.setString(1, laptop.getMachineCode());
            affectedRow = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return affectedRow;
    }

    @Override
    public ArrayList<Laptop> selectAll() {

        ArrayList<Laptop> listLaptop = new ArrayList<>();
        String selectQuery = "SELECT * FROM maytinh";
        try {
            Connection con = JDBCUtil.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);

            while (rs.next()) {
                Laptop lt = new Laptop(rs.getString("maMay"), rs.getString("tenMay"), rs.getInt("soLuong"),
                        rs.getString("tenCPU"), rs.getString("ram"), rs.getString("cardManHinh"),
                        rs.getString("rom"), rs.getDouble("gia"), rs.getString("xuatXu"),
                        rs.getInt("trangThai"), rs.getDouble("kichThuocMan"), rs.getString("dungLuongPin"));
                listLaptop.add(lt);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listLaptop;
    }

    @Override
    public Laptop selectById(String id) {
        String selectQuery = "SELECT * FROM maytinh WHERE maMay = ?";
        Laptop lt = null;
        try {
            Connection con = JDBCUtil.getConnection();
            PreparedStatement stmt = con.prepareStatement(selectQuery);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lt = new Laptop(rs.getString("maMay"), rs.getString("tenMay"), rs.getInt("soLuong"),
                        rs.getString("tenCPU"), rs.getString("ram"), rs.getString("cardManHinh"),
                        rs.getString("rom"), rs.getDouble("gia"), rs.getString("xuatXu"),
                        rs.getInt("trangThai"), rs.getDouble("kichThuocMan"), rs.getString("dungLuongPin"));
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lt;
    }

    public boolean isLaptop(String id) {
        boolean isLaptop = false;
        try (Connection con = JDBCUtil.getConnection()){
            String sql = "SELECT * FROM maytinh WHERE maMay = ? AND loaiMay = 'Laptop'";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, id);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                isLaptop = true;
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return isLaptop;
    }
}
