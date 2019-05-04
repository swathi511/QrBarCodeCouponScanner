package com.hjsoftware.qrbarcodecouponscanner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hjsoftware on 12/2/18.
 */

public class GetBarcodeResponcePojo {

    @SerializedName("barcde_id")
    @Expose
    private String barcdeId;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("customer_mobile")
    @Expose
    private String customerMobile;
    @SerializedName("vendor_id")
    @Expose
    private String vendorId;
    @SerializedName("batch_number")
    @Expose
    private String batchNumber;
    @SerializedName("promotion_value")
    @Expose
    private String promotionValue;
    @SerializedName("date_created")
    @Expose
    private String dateCreated;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;

    public String getBarcdeId(String barcde_id) {
        return barcdeId;
    }

    public void setBarcdeId(String barcdeId) {
        this.barcdeId = barcdeId;
    }

    public String getCustomerName(String customer_name) {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile(String mobile) {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getPromotionValue() {
        return promotionValue;
    }

    public void setPromotionValue(String promotionValue) {
        this.promotionValue = promotionValue;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getId(String dealer_id) {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
