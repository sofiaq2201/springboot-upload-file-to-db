package com.sofiaq.springbootuploadfiletodb.repository;

import com.sofiaq.springbootuploadfiletodb.models.FileDB;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB, String>{
    
    List<FileDB> findByNameContaining(String name);
    
  
}
