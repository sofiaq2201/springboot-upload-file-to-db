package com.sofiaq.springbootuploadfiletodb.services;

import org.springframework.util.StringUtils;
import com.sofiaq.springbootuploadfiletodb.models.FileDB;
import com.sofiaq.springbootuploadfiletodb.repository.FileDBRepository;
import java.io.IOException;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
    
    @Autowired
    private FileDBRepository fileDBRepository;

    public FileDB store(MultipartFile file) throws IOException{
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileDB fileDB = new FileDB(fileName, file.getContentType(), file.getBytes());
        
        return fileDBRepository.save(fileDB);
    }
    
    public FileDB getFile(String id){
        return fileDBRepository.findById(id).get();
    }
    
    public Stream<FileDB> getAllFiles(){
        return fileDBRepository.findAll().stream();
    }
    
}
