package com.power.enums;

public enum UserRole {
    CANDIDATE(1, "求职者"),
    RECRUITER(2, "求职者、招聘者");

    public final Integer type;
    public final String value;

    UserRole(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
