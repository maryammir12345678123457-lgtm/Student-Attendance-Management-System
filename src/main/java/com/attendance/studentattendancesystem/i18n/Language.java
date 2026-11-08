package com.attendance.studentattendancesystem.i18n;

public enum Language {
    ENGLISH("English", "en"),
    URDU("اردو", "ur"),
    CHINESE("中文", "zh");

    private final String label;
    private final String code;

    Language(String label, String code) {
        this.label = label;
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return label;
    }
}
