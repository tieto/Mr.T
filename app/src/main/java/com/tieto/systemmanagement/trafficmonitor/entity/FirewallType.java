package com.tieto.systemmanagement.trafficmonitor.entity;

/**
 * Created by jane on 15-3-24.
 */
public class FirewallType {

    //prohibit network sneaking
    public static final int SNEAKING_PROHIBIT = 0;
    //prohibit networking
    public static final int NETWORK_PROHIBIT = 1;
    //networking allowed
    public static final int NETWORK_ALLOWED = 2;
    //wifi allowed only
    public static final int WIFI_ALLOWED_ONLY = 3;

    public static final String[] FIREWALL_TYPE_NAME =
            new String[]{"禁止偷跑","禁止联网","允许网络","仅wifi连接"};

    @Override
    public String toString() {
        return super.toString();
    }

    public static String getFireWallType(int type) {
        return FIREWALL_TYPE_NAME[type];
    }


}
