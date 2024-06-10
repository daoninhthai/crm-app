package com.daoninhthai.crm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * WebSocket configuration for real-time notifications.
 *
 * When spring-boot-starter-websocket is added, this config enables
 * STOMP messaging over WebSocket for pushing real-time notifications
 * to connected clients.
 *
 * Clients can subscribe to: /topic/notifications/{userId}
 * Server pushes via SimpMessagingTemplate to the user's topic.
 *
 * For now, notifications are stored in the database and fetched via REST API.
 * WebSocket support will be enabled when the websocket starter is added.
 */
@Configuration
@Slf4j
public class WebSocketNotificationConfig {

    /**
     * Placeholder for WebSocket message broker configuration.
     * To enable WebSocket support:
     * 1. Add spring-boot-starter-websocket dependency
     * 2. Implement WebSocketMessageBrokerConfigurer
     * 3. Configure STOMP endpoints and message broker
     */
    public WebSocketNotificationConfig() {
        log.info("WebSocket notification config initialized - REST polling mode active");
    }
}
