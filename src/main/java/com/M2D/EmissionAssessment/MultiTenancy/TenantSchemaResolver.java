package com.M2D.EmissionAssessment.MultiTenancy;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.hibernate.cfg.AvailableSettings.DEFAULT_SCHEMA;

@Component
public class TenantSchemaResolver implements CurrentTenantIdentifierResolver {

    @Override
    public String resolveCurrentTenantIdentifier() {
        String t = String.valueOf(TenantContext.getCurrentTenant());
        return Objects.requireNonNullElse(t, AvailableSettings.DEFAULT_SCHEMA);
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}