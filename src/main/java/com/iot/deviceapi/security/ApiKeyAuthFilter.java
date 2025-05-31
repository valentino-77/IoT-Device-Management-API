package com.iot.deviceapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {
    private static final String VALID_API_KEY = "secret123";
    private static final Map<String, String> VALID_DEVICES = Map.of(
            "device123", "token123",
            "cam-001","cam-token-123"

    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {System.out.println("\n=== Processing Request ===");
        if (request.getRequestURI().startsWith("/h2-console") || //Skipping auth for H2 console and device registration
                ("POST".equalsIgnoreCase(request.getMethod()) && "/devices".equals(request.getRequestURI()))) {
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getMethod().equals("POST") && request.getRequestURI().equals("/devices")) {
            filterChain.doFilter(request, response);
            return;
        }
        System.out.println("Method: " + request.getMethod());
        System.out.println("URI: " + request.getRequestURI());
        System.out.println("Headers:");
        Collections.list(request.getHeaderNames())
                .forEach(header -> System.out.println(header + ": " + request.getHeader(header)));
        System.out.println("=========================\n");
        Collections.list(request.getHeaderNames()).forEach(header ->
                System.out.println(header + ": " + request.getHeader(header)));

        //validating the API Key
        String apiKey = request.getHeader("X-API-Key");
        if (apiKey == null || !VALID_API_KEY.equals(apiKey)) {
            sendError(response, "Invalid API Key");
            return;
        }

        //validating the device token
        String deviceId = null;
        if (requiresDeviceAuth(request)) {
            deviceId = extractDeviceId(request);
            String deviceToken = request.getHeader("X-Device-Token");

            System.out.println("Device Auth Required for: " + request.getRequestURI());
            System.out.println("Extracted Device ID: " + deviceId);
            System.out.println("Received Token: " + deviceToken);
            System.out.println("Valid Devices: " + VALID_DEVICES);


            if (deviceId == null) {
                sendError(response, "Device ID not found in URL");
                return;
            }


            if (deviceToken == null || !isValidDeviceToken(deviceId, deviceToken)) {
                sendError(response, "Invalid Device Token");
                return;
            }
        }

        //setting authentication context
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "system",
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_SYSTEM")) //assigning system role
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }


    private boolean requiresDeviceAuth(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();


        if ("POST".equalsIgnoreCase(method) && path.equals("/devices")) {
            return false;
        }


        return ("POST".equalsIgnoreCase(method) && path.matches("/devices/[^/]+/telemetry")) ||
                ("POST".equalsIgnoreCase(method) && path.matches("/devices/[^/]+/commands"));
    }

    private boolean isValidDeviceToken(String deviceId, String token) {
        return token != null && token.equals(VALID_DEVICES.get(deviceId));
    }


    private String extractDeviceId(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (path.startsWith("/devices/")) {
            String[] parts = path.split("/");
            if (parts.length >= 3) {

                return parts[2];
            }
        }
        return null;
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"error\": \"" + message + "\"}");
        response.getWriter().flush();
    }
}
