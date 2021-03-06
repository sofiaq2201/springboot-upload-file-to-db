package com.sofiaq.springbootuploadfiletodb.controllers;

import com.sofiaq.springbootuploadfiletodb.message.ResponseFile;
import com.sofiaq.springbootuploadfiletodb.models.FileDB;
import com.sofiaq.springbootuploadfiletodb.services.FileStorageService;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller

public class FileController {

    @Autowired
    private FileStorageService storageService;

    @GetMapping("/uploadForm")
    public String uploadForm(){
        return "uploadForm";
    }
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
             
        String message = "";
       
       try {
            storageService.store(file);
            message = "Subio exitosamente " + file.getOriginalFilename();
           redirectAttributes.addFlashAttribute("message",message);
           redirectAttributes.addFlashAttribute("status","ok");

        } catch (IOException e) {
            message = "No se pudo subir el archivo " + file.getOriginalFilename() + "!";
            redirectAttributes.addFlashAttribute("message",message);
            redirectAttributes.addFlashAttribute("status","fail");
        }
        return "redirect:/uploadForm";
    }

    @GetMapping("/")
    public String getListFiles(Model model){
        List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(dbFile.getId())
                    .toUriString();

            return new ResponseFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());
        model.addAttribute("files",files);
        return "index";
    }
    
    
    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        FileDB fileDB = storageService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }
    
    @RequestMapping("/search")
    public String search (@RequestParam("q") String q, Model model){
        List<ResponseFile> files;
        files = storageService.searching(q)
                .map(dbFile -> {
                    String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(dbFile.getId())
                    .toUriString();

            return new ResponseFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());
        model.addAttribute("files", files);
        return "busqueda";
    }
    
}
