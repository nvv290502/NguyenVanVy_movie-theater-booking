package com.movie_theaters.entity.enums;

public enum StatusMovie {
    DANG_CHIEU("Đang Chiếu"),
    SAP_CHIEU("Sắp Chiếu"),
    NGUNG_CHIEU("Ngừng Chiếu");

    private final String displayName;

    StatusMovie(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
