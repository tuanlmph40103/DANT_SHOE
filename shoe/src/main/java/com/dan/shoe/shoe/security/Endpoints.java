package com.dan.shoe.shoe.security;

public class Endpoints {
    public static final String[] PUBLIC_POST_ENDPOINTS = {
            "/auth/login",
            "/auth/signup",
            "/auth/forgot-password",
    };

    public static final String[] PRIVATE_POST_ENDPOINTS = {
            "/auth/logout",
            "/orders/**",
            "/cart/**"
    };

    public static final String[] ADMIN_POST_ENDPOINTS = {
            "/categories/admin/create",
            "/foods/admin/create",
            "/tables/admin/create",
            "/catalogs/admin/**",
            "/auth/admin/**",
    };

    public static final String[] STAFF_POST_ENDPOINTS = {
            "/products/staff/**",
    };

    public static final String[] PUBLIC_GET_ENDPOINTS = {
            "/auth/**",
            "/categories/all",
            "/products/public/**",
            "/orders/**",
            "/files/**",
    };

    public static final String[] PRIVATE_GET_ENDPOINTS = {
            "/auth/get/profile",
            "/cart/**",
    };

    public static final String[] PUBLIC_PUT_ENDPOINTS = {
            "/auth/reset-password",
            "/auth/update-verify-code",
            "/tables/update-status/**",
    };

    public static final String[] PRIVATE_PUT_ENDPOINTS = {
            "/auth/change-password",
            "/cart/**",
    };

    public static final String[] ADMIN_PUT_ENDPOINTS = {
            "/admin/**",
    };

    public static final String[] PRIVATE_DELETE_ENDPOINTS = {
            "/auth/delete/hard",
            "/cart/**",
    };

    public static final String[] ADMIN_DELETE_ENDPOINTS = {
            "/admin/**",
    };

    public static final String[] ADMIN_GET_ENDPOINTS = {
            "/admin/**",
    };
}
