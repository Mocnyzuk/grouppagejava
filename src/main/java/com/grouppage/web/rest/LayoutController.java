package com.grouppage.web.rest;

import com.grouppage.domain.notmapped.Layout;
import com.grouppage.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/layouts")
public class LayoutController {

    private final AuthService authService;

    @Autowired
    public LayoutController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<List<Layout>> getLayouts(){
        return ResponseEntity.ok(this.authService.getLayouts());
    }

    @PostMapping("/api/layouts")
    public ResponseEntity<Void> savevLayouts(){

    }
}
