package org.ahmedukamel.ecommerce.model.enumeration;

import lombok.Getter;

@Getter
public enum ReportType {
    SPAM,
    HARASSMENT,
    VIOLENCE,
    IMPERSONATE,
    FALSE_INFO,
    PRIVACY_VIOLATION,
    OTHER
}