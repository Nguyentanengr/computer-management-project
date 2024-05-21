package dao;

import database.JDBCUtil;
import model.Account;
import model.Computer;

import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ComputerDAO implements DAOinterface<Computer>{

    public static ComputerDAO getInstance() {
        return new ComputerDAO();
    }
    @Override
    public int insert(Computer t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public int update(Computer t) {
        int affectedRows = 0;
        String updateQuery = "UPDATE maytinh SET tenMay = ?,soLuong=?,gia=?,tenCpu=?,ram=?,xuatXu=?,cardManHinh=?,rom=?,trangThai=? WHERE maMay=?";
        try {
            Connection con = JDBCUtil.getConnection();
            PreparedStatement pst = con.prepareStatement(updateQuery);
            pst.setString(1, t.getMachineName());
            pst.setInt(2, t.getQuantity());
            pst.setDouble(3, t.getPrice());
            pst.setString(4, t.getCPUName());
            pst.setString(5, t.getRAM());
            pst.setString(6, t.getOrigin());
            pst.setString(7, t.getGraphicsCard());
            pst.setString(8, t.getRom());
            pst.setInt(9, t.getStatus());
            pst.setString(10, t.getMachineCode());

            affectedRows = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRows;
    }


    @Override
    public int delete(Computer t) {
        int affectedRows = 0;
        String deleteQuery = "DELETE FROM maytinh WHERE maMay = ?";
        try {
            Connection con = JDBCUtil.getConnection();
            PreparedStatement pst = con.prepareStatement(deleteQuery);
            pst.setString(1, t.getMachineCode());
            affectedRows = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRows;
    }



    @Override
    public ArrayList<Computer> selectAll() {
        ArrayList<Computer> listComputer = new ArrayList<>();
        String selectQuery = "SELECT * FROM maytinh";
        try (Connection con = JDBCUtil.getConnection()){
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);
            while(rs.next()) {
                Computer c = new Computer(rs.getString("maMay"), rs.getString("tenMay"), rs.getInt("soLuong"),
                        rs.getString("tenCpu"), rs.getString("ram") , rs.getString("cardManHinh"),
                        rs.getString("rom"), rs.getDouble("gia"), rs.getString("xuatXu"), rs.getInt("trangThai"));
                listComputer.add(c);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listComputer;
    }

    @Override
    public Computer selectById(String id) {
        Computer c = null;
        String selectQuery = "SELECT * FROM maytinh WHERE maMay = ?";
        try {
            Connection con = JDBCUtil.getConnection();
            PreparedStatement stmt = con.prepareStatement(selectQuery);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                c = new Computer(rs.getString("maMay"), rs.getString("tenMay"), rs.getInt("soLuong"),
                        rs.getString("tenCpu"), rs.getString("ram") , rs.getString("cardManHinh"),
                        rs.getString("rom"), rs.getDouble("gia"), rs.getString("xuatXu"), rs.getInt("trangThai"));
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    public int updateStatus(String machineCode) {
        int affectedRows = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE MayTinh SET trangThai = 0 WHERE maMay = ? ";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, machineCode);
            affectedRows= pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    public ArrayList<Computer> filter(String option, String content) {
        String selectQuery = "";
        ArrayList<Computer> listComputer = new ArrayList<>();
        switch (option) {
            case "Mã máy":
                selectQuery = "SELECT * FROM maytinh WHERE LOWER(maMay) LIKE ? AND trangThai = 1";
                break;
            case "Tên máy":
                selectQuery = "SELECT * FROM maytinh WHERE LOWER(tenMay) LIKE ? AND trangThai = 1";
                break;
            case "Số lượng":
                selectQuery = "SELECT * FROM maytinh WHERE soLuong >= ? AND trangThai = 1";
                break;
            case "Đơn giá":
                selectQuery = "SELECT * FROM maytinh WHERE gia >= ? AND trangThai = 1";
                break;
            case "RAM":
                selectQuery = "SELECT * FROM maytinh WHERE LOWER(ram) LIKE ? AND trangThai = 1";
                break;
            case "CPU":
                selectQuery = "SELECT * FROM maytinh WHERE LOWER(tenCpu) LIKE ? AND trangThai = 1";
                break;
            case "Dung lượng":
                selectQuery = "SELECT * FROM maytinh WHERE LOWER(rom) LIKE ? AND trangThai = 1";
                break;
            case "Card màn hình":
                selectQuery = "SELECT * FROM maytinh WHERE LOWER(cardManHInh) LIKE ? AND trangThai = 1";
                break;
            case "Xuất xứ":
                selectQuery = "SELECT * FROM maytinh WHERE LOWER(xuatXu) LIKE ? AND trangThai = 1";
                break;
            case "Tất cả":
                selectQuery = "SELECT * FROM maytinh WHERE trangThai = 1 AND (LOWER(maMay) LIKE ? OR LOWER(tenMay) LIKE ? OR " +
                        "LOWER(ram) LIKE ? OR LOWER(rom) LIKE ? OR LOWER(tenCpu) LIKE ? OR LOWER(cardManHInh) LIKE ? OR LOWER(xuatXu) LIKE ?)";
        }

        try (Connection con = JDBCUtil.getConnection()) {
            PreparedStatement pst = con.prepareStatement(selectQuery);
            if (option.equals("Tất cả")) {
                pst.setString(1, "%" + content.toLowerCase() + "%");
                pst.setString(2, "%" + content.toLowerCase() + "%");
                pst.setString(3, "%" + content.toLowerCase() + "%");
                pst.setString(4, "%" + content.toLowerCase() + "%");
                pst.setString(5, "%" + content.toLowerCase() + "%");
                pst.setString(6, "%" + content.toLowerCase() + "%");
                pst.setString(7, "%" + content.toLowerCase() + "%");
            }
            else if (option.equals("Số lượng")){
                try {
                    if (content.isEmpty()) pst.setInt(1, 0);
                    else pst.setInt(1, Integer.parseInt(content));
                } catch (NumberFormatException e) {
                    return listComputer;
                }
            } else if (option.equals("Đơn giá")) {
                try {
                    if (content.isEmpty()) pst.setDouble(1, 0);
                    else pst.setDouble(1, Double.parseDouble(content));
                } catch (NumberFormatException e) {
                    return listComputer;
                }
            } else {
                pst.setString(1, "%" + content.toLowerCase() + "%");
            }

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String machineCode = rs.getString("maMay");
                String machineName = rs.getString("tenMay");
                String cpuName = rs.getString("tenCpu");
                String ram = rs.getString("ram");
                String rom = rs.getString("rom");
                String type = rs.getString("loaiMay");
                String graphicsCard = rs.getString("cardManHInh");
                String origin = rs.getString("xuatXu");
                int status = rs.getInt("trangThai");
                int quantity = rs.getInt("soLuong");
                double price = rs.getDouble("gia");


                Computer computer = new Computer(machineCode, machineName, quantity, cpuName, ram, graphicsCard, rom, price, origin, status);
                listComputer.add(computer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listComputer;
    }
}
