package com.vce.baselib.utils;

/**
 * 描述：NetWorkUtil
 * 创建者: shichengxiang
 * 创建时间：2022/3/23
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

public class NetworkUtil {
    public static final String NETWORK_TYPE_WIFI = "wifi";
    public static final String NETWORK_CLASS_DISCONNECTED = "disconnected";
    public static final String NETWORK_CLASS_UNKNOWN = "unknown";
    public static final String NETWORK_CLASS_DENIED = "denied";
    public static final String NETWORK_CLASS_2G = "2g";
    public static final String NETWORK_CLASS_3G = "3g";
    public static final String NETWORK_CLASS_4G = "4g";
    public static final String NETWORK_CLASS_5G = "5g";
    public static final String UNKNOW = "";
    public static final String WIFI = "Wi-Fi";
    public static final String MOBILE_NETWORK = "2G/3G";

    public NetworkUtil() {
    }

    public static NetworkInfo getActiveNetworkInfo(Context var0) {
        NetworkInfo var1 = null;

        try {
            ConnectivityManager var4;
            if ((var4 = (ConnectivityManager)var0.getSystemService(Context.CONNECTIVITY_SERVICE)) == null) {
                Log.w("efs.base", "get CONNECTIVITY_SERVICE is null");
                return null;
            }

            NetworkInfo[] var5;
            if (((var1 = var4.getActiveNetworkInfo()) == null || !var1.isConnected()) && (var5 = var4.getAllNetworkInfo()) != null) {
                for(int var2 = 0; var2 < var5.length; ++var2) {
                    if (var5[var2] != null && var5[var2].isConnected()) {
                        var1 = var5[var2];
                        break;
                    }
                }
            }
        } catch (Throwable var3) {
            Log.e("efs.base", "get network info error", var3);
        }

        return var1;
    }

    public static boolean isConnected(Context var0) {
        NetworkInfo var1;
        if ((var1 = getActiveNetworkInfo(var0)) != null && var1.isConnected()) {
            return var1.getState() == State.CONNECTED;
        } else {
            return false;
        }
    }

    public static boolean isWifi(Context var0) {
        if (isRejectAccessNetworkState(var0)) {
            return false;
        } else {
            NetworkInfo var1;
            if ((var1 = getActiveNetworkInfo(var0)) != null && var1.isConnected()) {
                return var1.getType() == 1;
            } else {
                return false;
            }
        }
    }

    public static boolean hasAccessNetworkState(Context var0) {
        try {
            return var0.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", var0.getPackageName()) == PackageManager.PERMISSION_GRANTED;
        } catch (Throwable var1) {
            return false;
        }
    }

    public static boolean isRejectAccessNetworkState(Context var0) {
        return !hasAccessNetworkState(var0);
    }

    public static String getNetworkType(Context var0) {
        if (isRejectAccessNetworkState(var0)) {
            return "denied";
        } else {
            NetworkInfo var1;
            if ((var1 = getActiveNetworkInfo(var0)) == null) {
                return "disconnected";
            } else if (var1.getType() == 1) {
                return "wifi";
            } else {
                switch(var1.getSubtype()) {
                    case 1:
                    case 2:
                    case 4:
                    case 7:
                    case 11:
                        return "2g";
                    case 3:
                    case 5:
                    case 6:
                    case 8:
                    case 9:
                    case 10:
                    case 12:
                    case 14:
                    case 15:
                        return "3g";
                    case 13:
                        return "4g";
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    default:
                        String var2;
                        if (TextUtils.isEmpty(var2 = var1.getSubtypeName())) {
                            return "unknown";
                        } else {
                            if (!var2.equalsIgnoreCase("TD-SCDMA") && !var2.equalsIgnoreCase("WCDMA") && !var2.equalsIgnoreCase("CDMA2000")) {
                                return var2;
                            }

                            return "3g";
                        }
                    case 20:
                        return "5g";
                }
            }
        }
    }

    public static boolean checkPermission(Context var0, String var1) {
        boolean var2 = false;
        if (var0 == null) {
            return false;
        } else {
            if (VERSION.SDK_INT >= 23) {
                try {
                    if ((Integer)Class.forName("android.content.Context").getMethod("checkSelfPermission", String.class).invoke(var0, var1) == 0) {
                        var2 = true;
                    } else {
                        var2 = false;
                    }
                } catch (Throwable var3) {
                    var2 = false;
                }
            } else if (var0.getPackageManager().checkPermission(var1, var0.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                var2 = true;
            }

            return var2;
        }
    }

    public static String[] getNetworkAccessMode(Context var0) {
        String[] var1 = new String[]{"", ""};
        if (var0 == null) {
            return var1;
        } else {
            try {
                if (!checkPermission(var0, "android.permission.ACCESS_NETWORK_STATE")) {
                    var1[0] = "";
                    return var1;
                }

                ConnectivityManager var4;
                if ((var4 = (ConnectivityManager)var0.getSystemService(Context.CONNECTIVITY_SERVICE)) == null) {
                    var1[0] = "";
                    return var1;
                }

                NetworkInfo var2;
                if ((var2 = var4.getNetworkInfo(1)) != null && var2.getState() == State.CONNECTED) {
                    var1[0] = "Wi-Fi";
                    return var1;
                }

                NetworkInfo var5;
                if ((var5 = var4.getNetworkInfo(0)) != null && var5.getState() == State.CONNECTED) {
                    var1[0] = "2G/3G";
                    var1[1] = var5.getSubtypeName();
                    return var1;
                }
            } catch (Throwable var3) {
            }

            return var1;
        }
    }

    public static int getNetworkTypeUmeng(Context var0) {
        int var1 = 0;

        try {
            TelephonyManager var2 = (TelephonyManager)var0.getSystemService(Context.TELEPHONY_SERVICE);
            if (checkPermission(var0, "android.permission.READ_PHONE_STATE")) {
                var1 = var2.getNetworkType();
            }
        } catch (Exception var3) {
            var1 = -100;
        }

        return var1;
    }
}
