package com.example.hidaymovie.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

    /**
     * Kiểm tra xem thiết bị có đang kết nối Internet không
     * @param context Context của Activity hoặc Application
     * @return true nếu có mạng, false nếu không
     */
    public static boolean isNetworkConnected(Context context) {
        if (context == null) return false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }

        return false;
    }
}
