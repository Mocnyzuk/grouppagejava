package com.grouppage.web.rest;

import com.grouppage.domain.response.DashboardResponse;
import com.grouppage.service.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

class DashboardControllerUnitTest {

    @Mock
    private GroupService groupService;

    @InjectMocks
    private DashboardController dashboardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getDashboardResponse() throws InterruptedException, ExecutionException, TimeoutException {
        List<DashboardResponse> list = Arrays.asList(
                new DashboardResponse(null, null),
                new DashboardResponse(null, null),
                new DashboardResponse(null, null),
                new DashboardResponse(null, null),
                new DashboardResponse(null, null),
                new DashboardResponse(null, null),
                new DashboardResponse(null, null),
                new DashboardResponse(null, null)
        );
        doReturn(list)
                .when(groupService)
                .generateDashboard();
        Object response = this.dashboardController.getDashboard();
        assertNotNull(response);
        assertTrue(response instanceof ResponseEntity);
        assertTrue(((ResponseEntity) response).getBody() instanceof List);
        assertEquals(HttpStatus.OK, ((ResponseEntity) response).getStatusCode());
        assertEquals(list.size(), ((List) ((ResponseEntity) response).getBody()).size());
    }
}