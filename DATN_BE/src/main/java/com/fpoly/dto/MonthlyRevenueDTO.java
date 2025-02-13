package com.fpoly.dto;

public class MonthlyRevenueDTO {
	private Integer month;
    private Long recordCount;

    public MonthlyRevenueDTO(Integer month, Long recordCount) {
        this.month = month;
        this.recordCount = recordCount;
    }

    // Getters and setters
    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Long recordCount) {
        this.recordCount = recordCount;
    }
}
