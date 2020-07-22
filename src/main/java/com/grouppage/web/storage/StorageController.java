package com.grouppage.web.storage;

import com.grouppage.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class StorageController {

    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/{dirname}/{filename:.+}")
    public ResponseEntity<Resource> getTheFile(
            @PathVariable(name = "dirname") String dirname,
            @PathVariable(name = "filename") String filename) {
       return storageService.loadAsResponse(dirname+"/"+filename);
    }

    @PostMapping("/uploadfile/{dirname}")
    public ResponseEntity<Void> uploadFile(@RequestParam("file") MultipartFile file,
                                           @PathVariable("dirname") String dirname) {
        try{
            System.out.println("jestem");
            System.out.println(file.getName());
            storageService.store(file, dirname);
        }catch (Exception e){
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/upload-multiple-files")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        try{
            for (MultipartFile file : files) {
                //storageService.store(file);
            }
        }catch (Exception e){
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
