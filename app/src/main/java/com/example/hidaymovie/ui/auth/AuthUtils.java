package com.example.hidaymovie.ui.auth;

import android.text.TextUtils;
import android.util.Patterns;

public class AuthUtils {

    /**
     * Kiểm tra email hợp lệ
     * @param email chuỗi email cần kiểm tra
     * @return true nếu email hợp lệ, false nếu không
     */
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Kiểm tra mật khẩu hợp lệ
     * Quy tắc ví dụ: tối thiểu 6 ký tự
     * @param password chuỗi mật khẩu cần kiểm tra
     * @return true nếu mật khẩu hợp lệ, false nếu không
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    /**
     * Kiểm tra tên hiển thị hợp lệ
     * Ví dụ không để trống và ít nhất 3 ký tự
     * @param displayName chuỗi tên hiển thị
     * @return true nếu hợp lệ, false nếu không
     */
    public static boolean isValidDisplayName(String displayName) {
        return displayName != null && displayName.trim().length() >= 3;
    }
}
