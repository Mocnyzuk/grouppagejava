package com.grouppage.web.rest;

import com.grouppage.domain.notmapped.Layout;
import com.grouppage.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
            @RequestBody @Valid Layout layout
    ){
        this.authService.saveLayout(layout);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/multi")
    public ResponseEntity<Void> saveLayouts(
            @RequestBody List<Layout> layouts
    ){
        this.authService.saveLayouts(layouts);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }
    @DeleteMapping("/api/layout/{name}")
    public ResponseEntity<Void> deleteLayout(
            @PathVariable(name = "name") String name
    ){
        this.authService.deleteLayout(name);
        return null;
    }
}
