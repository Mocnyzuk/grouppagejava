package com.grouppage.web.rest;

import com.grouppage.domain.response.DashboardResponse;
import com.grouppage.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api")
public class DashboardController {

    private final GroupService groupService;

    @Autowired
    public DashboardController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<List<DashboardResponse>> getDashboard() throws InterruptedException, ExecutionException, TimeoutException {
        return ResponseEntity.ok(this.groupService.generateDashboard());
    }

}
