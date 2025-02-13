package com.fpoly.dto;

import java.util.Date;

public class MyVoucherDTO {
    private int myVoucherId;
    private boolean status;
    private String voucherName;
    private String voucherDescription;
    private String voucherCode;
    private float voucherPercentSale;
    private Date startDate;
    private Date endDate;

    // Constructor
    public MyVoucherDTO(int myVoucherId, boolean status, String voucherName, String voucherDescription, String voucherCode, float voucherPercentSale, Date startDate, Date endDate) {
        this.myVoucherId = myVoucherId;
        this.status = status;
        this.voucherName = voucherName;
        this.voucherDescription = voucherDescription;
        this.voucherCode = voucherCode;
        this.voucherPercentSale = voucherPercentSale;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public int getMyVoucherId() {
        return myVoucherId;
    }

    public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setMyVoucherId(int myVoucherId) {
        this.myVoucherId = myVoucherId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public String getVoucherDescription() {
        return voucherDescription;
    }

    public void setVoucherDescription(String voucherDescription) {
        this.voucherDescription = voucherDescription;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public float getVoucherPercentSale() {
        return voucherPercentSale;
    }

    public void setVoucherPercentSale(float voucherPercentSale) {
        this.voucherPercentSale = voucherPercentSale;
    }
}

