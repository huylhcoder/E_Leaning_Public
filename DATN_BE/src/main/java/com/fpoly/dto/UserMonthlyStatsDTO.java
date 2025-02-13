package com.fpoly.dto;

public class UserMonthlyStatsDTO {
	private int month;
    private long userCount;

    public UserMonthlyStatsDTO(int month, long userCount) {
        this.month = month;
        this.userCount = userCount;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public long getUserCount() {
        return userCount;
    }

    public void setUserCount(long userCount) {
        this.userCount = userCount;
    }
}
