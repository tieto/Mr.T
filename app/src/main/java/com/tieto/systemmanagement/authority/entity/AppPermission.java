package com.tieto.systemmanagement.authority.entity;

/**
 * @author Jiang Ping
 */
public final class AppPermission {

    private String mLabel;
    private String mDescription;

    public AppPermission(String label, String desc) {
        mLabel = label;
        mDescription = desc;
    }
}
