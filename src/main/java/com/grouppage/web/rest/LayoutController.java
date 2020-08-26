package com.grouppage.web.rest;

import com.grouppage.domain.notmapped.Layout;
import com.grouppage.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/single")
    public ResponseEntity<Void> saveLayout(
            @RequestBody Layout layout
    ){
        this.authService.saveLayout(layout);
        return null;
    }
    @PostMapping("/multi")
    public ResponseEntity<Void> saveLayouts(
            @RequestBody List<Layout> layouts
    ){
        //return this.authService.saveLayout(layout);
        return null;
    }
    @DeleteMapping("/api/layout/{name}")
    public ResponseEntity<Void> deleteLayout(
            @PathVariable(name = "name") String name
    ){
        return null;
    }
}
