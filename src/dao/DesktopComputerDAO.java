package dao;


import database.JDBCUtil;
import model.DesktopComputer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DesktopComputerDAO implements DAOinterface<DesktopComputer> {

    public static DesktopComputerDAO getInstance() {
        return new DesktopComputerDAO();
    }
    @Override
    public int insert(DesktopComputer t) {
        int affectedRows = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO MayTinh (maMay, tenMay, soLuong, tenCpu, ram, cardManHinh, gia, mainBoard, congSuatNguon, xuatXu, loaiMay, rom, trangThai) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getMachineCode());
            pst.setString(2, t.getMachineName());
            pst.setInt(3, t.getQuantity());
            pst.setString(4, t.getCPUName());
            pst.setString(5, t.getRAM());
            pst.setString(6, t.getGraphicsCard());
            pst.setDouble(7, t.getPrice());
            pst.setString(8, t.getMainboard());
            pst.setInt(9, t.getPowerSupply());
            pst.setString(10, t.getOrigin());
            pst.setString(11, "PC - Lắp ráp");
            pst.setString(12, t.getRom());
            pst.setInt(13, t.getStatus());
            affectedRows = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    @Override
    public int update(DesktopComputer t) {
        int affectedRows = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            //String sql = "INSERT INTO MayTinh (maMay, tenMay, soLuong, tenCpu, ram, cardManHinh, gia, dungLuongPin, dungLuongPin, dungLuongPin, loaiMay, rom) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            String sql = "UPDATE MayTinh SET maMay=?, tenMay=?, soLuong=?, tenCpu=?, ram=?, cardManHinh=?, gia=?, mainBoard=?, congSuatNguon=?, xuatXu=?, loaiMay = ?, rom = ?, trangThai = ? WHERE maMay= ? ";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getMachineCode());
            pst.setString(2, t.getMachineName());
            pst.setInt(3, t.getQuantity());
            pst.setString(4, t.getCPUName());
            pst.setString(5, t.getRAM());
            pst.setString(6, t.getGraphicsCard());
            pst.setDouble(7, t.getPrice());
            pst.setString(8, t.getMainboard());
            pst.setInt(9, t.getPowerSupply());
            pst.setString(10, t.getOrigin());
            pst.setString(11, "DesktopComputer");
            pst.setString(12, t.getRom());
            pst.setInt(13, t.getStatus());
            pst.setString(14, t.getMachineCode());
            affectedRows = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    @Override
    public int delete(DesktopComputer t) {
        int affectedRows = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "DELETE FROM MayTinh WHERE maMay=? ";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getMachineCode());
            affectedRows = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return affectedRows;
    }

    @Override
    public ArrayList<DesktopComputer> selectAll() {
        ArrayList<DesktopComputer> listDesktopComputer = new ArrayList<DesktopComputer>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM MayTinh";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String maMay = rs.getString("maMay");
                String tenMay = rs.getString("tenMay");
                int soLuong = rs.getInt("soLuong");
                String tenCpu = rs.getString("tenCpu");
                String ram = rs.getString("ram");
                String cardManHinh = rs.getString("cardManHinh");
                double gia = rs.getDouble("gia");
                String mainBoard = rs.getString("mainBoard");
                int congSuatNguon = rs.getInt("congSuatNguon");
                String rom = rs.getString("rom");
                String xuatXu = rs.getString("xuatXu");
                int trangThai = rs.getInt("trangThai");
                DesktopComputer dc = new DesktopComputer(maMay, tenMay, soLuong, tenCpu, ram, cardManHinh, rom, gia, xuatXu, trangThai, mainBoard, congSuatNguon);

                listDesktopComputer.add(dc);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return listDesktopComputer;
    }

    @Override
    public DesktopComputer selectById(String id) {
        DesktopComputer desktopComputer = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM MayTinh WHERE maMay=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String maMay = rs.getString("maMay");
                String tenMay = rs.getString("tenMay");
                int soLuong = rs.getInt("soLuong");
                String tenCpu = rs.getString("tenCpu");
                String ram = rs.getString("ram");
                String cardManHinh = rs.getString("cardManHinh");
                double gia = rs.getDouble("gia");
                String mainBoard = rs.getString("mainBoard");
                int congSuatNguon = rs.getInt("congSuatNguon");
                String rom = rs.getString("rom");
                String xuatXu = rs.getString("xuatXu");
                int trangThai = rs.getInt("trangThai");
                //Laptop(String kichThuocMan, String dungLuongPin, String maMay, String tenMay, int soLuong, double gia, String tenCpu, String ram, String xuatXu, String cardManHinh, String Rom)
                desktopComputer = new DesktopComputer(maMay, tenMay, soLuong, tenCpu, ram, cardManHinh, rom, gia, xuatXu, trangThai, mainBoard, congSuatNguon);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return desktopComputer;
    }
}
