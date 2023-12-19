package com.M2D.EmissionAssessment.MultiTenancy;
import com.fasterxml.jackson.databind.JsonNode;

public class TenantContext {
    private static ThreadLocal<Long> currentTenant = new InheritableThreadLocal<>();
    private static ThreadLocal<String> requestIp = new InheritableThreadLocal<>();
    private static ThreadLocal<JsonNode> token = new InheritableThreadLocal<>();

    public static Long getCurrentTenant() {
        return currentTenant.get();
    }

    public static JsonNode getToken() {
        return token.get();
    }
    public static String getRequestedIP() {
        return requestIp.get();
    }

    public static void setCurrentTenant(Long tenant) {
        currentTenant.set(tenant);
    }

    public static void setToken(JsonNode _token) {
        token.set(_token);
    }
    public static void setRequestedIP(String _requestIp) {
        requestIp.set(_requestIp);
    }

    public static void clear() {
        currentTenant.set(null);
    }
}