package com.daoninhthai.crm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to enforce permission-based access control on methods.
 * Used in conjunction with CrmPermissionEvaluator.
 *
 * Example usage:
 * {@code @RequirePermission("contacts:delete")}
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {

    /**
     * The permission name required to access the annotated method.
     * Format: "module:action" (e.g., "contacts:delete", "deals:export")
     */
    String value();
}
