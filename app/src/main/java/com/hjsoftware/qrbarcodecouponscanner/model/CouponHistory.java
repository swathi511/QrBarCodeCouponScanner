package com.hjsoftware.qrbarcodecouponscanner.model;

import java.io.Serializable;

/**
 * Created by hjsoftware on 12/2/18.
 */

public class CouponHistory implements Serializable {

    private String couponcode,couponamount,customername,customermobile;

    public CouponHistory(String couponcode,String couponamount,String customername,String customermobile){

        this.couponcode=couponcode;
        this.couponamount=couponamount;
        this.customername=customername;
        this.customermobile=customermobile;
    }

    public String getCouponcode() {
        return couponcode;
    }

    public void setCouponcode(String couponcode) {
        this.couponcode = couponcode;
    }

    public String getCouponamount() {
        return couponamount;
    }

    public void setCouponamount(String couponamount) {
        this.couponamount = couponamount;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getCustomermobile() {
        return customermobile;
    }

    public void setCustomermobile(String customermobile) {
        this.customermobile = customermobile;
    }
}
