package com.grouppage.service.storage;

import com.grouppage.domain.entity.Image;
import com.grouppage.domain.repository.ImageRepository;
import com.grouppage.exception.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
@Transactional
public class StorageService {

    private final Path rootLocation;
    private final ImageRepository imageRepository;


    @Autowired
    public StorageService(
            @Value("${file.root.path}") Path rootLocation,
            ImageRepository imageRepository) {
        this.rootLocation = rootLocation;
        this.imageRepository = imageRepository;
    }

    public void store(MultipartFile file, String dir) throws Exception{
        String filename = dir+"/"+ StringUtils.cleanPath(file.getOriginalFilename());
            if(file.isEmpty()){
                throw new StorageException("Nazwa pliku jest pusta -> '"+file.getName()+"'");
            }
            if(filename.contains("..")){
                throw new StorageException(
                        "Nieobsługiwany ciąg znaków nazwie pliku '..' -> '"+file.getName()+"'" );
            }
            try(InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
                imageRepository.save(new Image("added", "/files/"+filename));
            }

    }
    public Path load(String filename) {

        return rootLocation.resolve(filename);
    }

    public ResponseEntity<Resource> loadAsResponse(String filename) throws StorageException {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return new ResponseEntity<>(resource,
                        getProperMediaType(resource.getFile()),
                        HttpStatus.OK);
            }
            else {
                throw new StorageException(
                        "Nie można załadować pliku -> '" + filename+ "'");

            }
        } catch (IOException e) {
            throw new StorageException("Nie można załadować pliku -> '" + filename+ "'");
        }
    }

    private HttpHeaders getProperMediaType(File file){
        HttpHeaders header = new HttpHeaders();
        String filename = file.getName();

        MediaType mediaType;
        String str = filename.substring(filename.lastIndexOf('.')+1);
        switch (str){
            case "png":
                mediaType = MediaType.IMAGE_PNG;
                break;
            case "txt":
                mediaType = new MediaType("text", "html", StandardCharsets.UTF_8);
                break;
            default:
                System.out.println(str);
                mediaType = MediaType.IMAGE_JPEG;
                break;
                //jeszcze txt formaty
        }
        header.setContentType(mediaType);
        return header;
    }
}
