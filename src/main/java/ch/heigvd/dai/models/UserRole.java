package ch.heigvd.dai.models;

import io.javalin.security.RouteRole;

public enum UserRole implements RouteRole {
    UNAUTHENTICATED,
    USER,
}
