package com.fpoly.dto;

public class YearRevenueDTO {
	private Integer year;
    private Long recordCount;

    public YearRevenueDTO(Integer year, Long recordCount) {
        this.year = year;
        this.recordCount = recordCount;
    }

    // Getters and setters
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Long recordCount) {
        this.recordCount = recordCount;
    }
}
