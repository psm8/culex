package io.web.websocket;

import io.web.websocket.dto.ActivityDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import java.security.Principal;


class ActivityServiceTest {
    private ActivityService activityService;
    private SimpMessageSendingOperations simpMessageSendingOperations;
    private ActivityDTO activityDTO;
    private StompHeaderAccessor stompHeaderAccessor;
    private Principal principal;

    @BeforeEach
    void initAll(){
        //activityService = new ActivityService(simpMessageSendingOperations);
        //activityDTO = new ActivityDTO();
        //stompHeaderAccessor = (StompHeaderAccessor) StompHeaderAccessor.create();
        //principal = new PrincipalImpl("user");
    }

    @Test
    void testSendActivity() {
        //ActivityDTO activity = sendActivity();
    }

    @Test
    void testOnApplicationEvent() {
    }
}
