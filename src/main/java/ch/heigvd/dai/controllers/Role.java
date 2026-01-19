package ch.heigvd.dai.controllers;

import io.javalin.security.RouteRole;

public enum Role implements RouteRole {
    ANYONE,
    AUTHENTICATED
}
