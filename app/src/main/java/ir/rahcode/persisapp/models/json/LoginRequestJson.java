package ir.rahcode.persisapp.models.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Androgo on 10/13/2018.
 */

public class LoginRequestJson {

    @SerializedName("phone")
    @Expose
    private String phone;



    @SerializedName("reg_id")
    @Expose
    private String reg_id;

    public String getphone() {
        return phone;
    }

    public void setphone(String phone) {
        this.phone = phone;
    }

    public String getRegId() {
        return reg_id;
    }

    public void setRegId(String regId) {
        this.reg_id = regId;
    }
}
