package model;

import java.sql.Timestamp;

public class OTPdata {
    private String email;
    private String otp;
    private Timestamp creationTime;

    public OTPdata(String email, String otp, Timestamp creationTime) {
        this.email = email;
        this.otp = otp;
        this.creationTime = creationTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }
}
