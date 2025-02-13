package com.fpoly.dto;

public class UserYearStatsDTO {
	private int year;
    private long userCount;

    public UserYearStatsDTO(int year, long userCount) {
        this.year = year;
        this.userCount = userCount;
    }

    public int getMonth() {
        return year;
    }

    public void setMonth(int month) {
        this.year = year;
    }

    public long getUserCount() {
        return userCount;
    }

    public void setUserCount(long userCount) {
        this.userCount = userCount;
    }
}
