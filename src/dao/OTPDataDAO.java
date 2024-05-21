package dao;

import database.JDBCUtil;
import model.OTPdata;

import java.sql.*;
import java.util.ArrayList;

public class OTPDataDAO implements DAOinterface<OTPdata> {

    public static OTPDataDAO getInstance() {
        return new OTPDataDAO();
    }
    @Override
    public int insert(OTPdata o) {
        int affectedRows = 0;
        String insertQuery = "INSERT INTO otpdata (email, otp, creationtime) VALUES (?, ?, ?)";

        try (Connection con = JDBCUtil.getConnection()) {
            PreparedStatement pst = con.prepareStatement(insertQuery);
            pst.setString(1, o.getEmail());
            pst.setString(2, o.getOtp());
            pst.setTimestamp(3, o.getCreationTime());

            affectedRows = pst.executeUpdate();
            System.out.println("da insert " + affectedRows);
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    @Override
    public int update(OTPdata o) {
        int affectedRows = 0;
        String updateQuery = "UPDATE otpdata SET otp = ?, creationtime = ? WHERE email = ?";

        try (Connection con = JDBCUtil.getConnection()) {
            PreparedStatement pst = con.prepareStatement(updateQuery);
            pst.setString(3, o.getEmail());
            pst.setString(1, o.getOtp());
            pst.setTimestamp(2, o.getCreationTime());

            affectedRows = pst.executeUpdate();
            System.out.println("da update " + affectedRows);
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    @Override
    public int delete(OTPdata o) {
        int affectedRows = 0;
        String deleteQuery = "DELETE FROM otpdate WHERE email = ?";

        try (Connection con = JDBCUtil.getConnection()) {
            PreparedStatement pst = con.prepareStatement(deleteQuery);
            pst.setString(1, o.getEmail());
            affectedRows = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    @Override
    public ArrayList<OTPdata> selectAll() {
        ArrayList<OTPdata> listOTPdata = new ArrayList<>();
        String selectQuery = "SELECT * FROM otpdata ORDER BY creationtime ASC";

        try (Connection con = JDBCUtil.getConnection()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);

            while (rs.next()) {
                String email = rs.getString("email");
                String otp = rs.getString("otp");
                Timestamp creationTime = rs.getTimestamp("creationtime");

                OTPdata otPdata = new OTPdata(email, otp, creationTime);
                listOTPdata.add(otPdata);
            }

            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listOTPdata;
    }

    @Override
    public OTPdata selectById(String id) {
        OTPdata otPdata = null;
        String selectQuery = "SELECT * FROM otpdata WHERE email = ?";

        try (Connection con = JDBCUtil.getConnection()) {
            PreparedStatement pst = con.prepareStatement(selectQuery);
            pst.setString(1, id);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String email = rs.getString("email");
                String otp = rs.getString("otp");
                Timestamp creationTime = rs.getTimestamp("creationtime");
                otPdata = new OTPdata(email, otp, creationTime);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return otPdata;
    }
}
