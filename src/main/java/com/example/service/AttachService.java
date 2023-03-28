package com.example.service;


import com.example.dto.attach.AttachDTO;
import com.example.entity.AttachEntity;
import com.example.exceptions.ItemNotFoundException;
import com.example.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AttachService {
    @Value("${attach.upload.folder}")
    private String attachFolder;


    @Value("${application.url}")
    private String attachOpenUrl;

    @Autowired
    private AttachRepository attachRepository;
  /*  public String saveToSystemOld(MultipartFile file) {
        try {
            // attaches/2022/04/23/UUID.png
            String attachPath = getYmDString(); // 2022/04/23
            String extension = getExtension(file.getOriginalFilename()); // .png....
            String fileName = UUID.randomUUID().toString() + "." + extension; // UUID.png

            File folder = new File(attachFolder + attachPath);  // attaches/2022/04/23/
            if (!folder.exists()) {
                folder.mkdirs();
            }

            byte[] bytes = file.getBytes();

            Path path = Paths.get(attachFolder + attachPath + "/" + fileName); // attaches/2022/04/23/UUID.png
            Files.write(path, bytes);
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    public AttachDTO saveToSystem(MultipartFile file) {
        try {
            // attaches/2022/04/23/UUID.png
            String attachPath = getYmDString(); // 2022/04/23
            String extension = getExtension(file.getOriginalFilename()); // .png....
            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + "." + extension; // UUID.png

            File folder = new File(attachFolder + attachPath);  // attaches/2022/04/23/
            if (!folder.exists()) {
                folder.mkdirs();
            }

            byte[] bytes = file.getBytes();

            Path path = Paths.get(attachFolder + attachPath + "/" + fileName); // attaches/2022/04/23/UUID.png
            Files.write(path, bytes);

            AttachEntity entity = new AttachEntity();
            entity.setPath(attachPath);
            entity.setExtension(extension);
            entity.setSize(file.getSize());
            entity.setOriginalName(file.getOriginalFilename());
            entity.setCreatedData(LocalDateTime.now());
            entity.setId(uuid);
            attachRepository.save(entity);

            AttachDTO attachDTO = toDTO(entity);
            attachDTO.setOriginalName(file.getOriginalFilename());
            attachDTO.setUrl(attachOpenUrl + fileName);


            return attachDTO;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private AttachDTO toDTO(AttachEntity entity) {
        AttachDTO dto = new AttachDTO();
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedData());
        dto.setOriginalName(entity.getOriginalName());
        dto.setPath(entity.getPath());
        dto.setSize(entity.getSize());
        return dto;
    }

   /* public String saveToSystemOld(MultipartFile file) {
        try {
            File folder = new File("attaches");
            if (!folder.exists()) {
                folder.mkdir();
            }

            byte[] bytes = file.getBytes();

            Path path = Paths.get("attaches/" + file.getOriginalFilename());
            Files.write(path, bytes);
            return file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    public byte[] loadImage(String fileName) {
        byte[] imageInByte;
        BufferedImage originalImage;

        try {
            Optional<AttachEntity> byId = attachRepository.findById(fileName);
            if (byId.isEmpty()) {
                throw new ItemNotFoundException("attach");
            }
            File file = new File("attaches/" + byId.get().getPath() + "/" + fileName + "." + byId.get().getExtension());
            if (!file.exists()) {
                throw new ItemNotFoundException("File");
            }
            originalImage = ImageIO.read(file);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "png", baos);


            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
   /* public byte[] loadImageOld(String fileName) {
        byte[] imageInByte;
        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(new File("attaches/" + fileName));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "png", baos);

            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }*/
/*    public byte[] open_generalOld(String fileName) {
        byte[] data;
        try {
            Path file = Paths.get("attaches/2022/12/7" + fileName);
            data = Files.readAllBytes(file);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }*/


    public byte[] open_general(String key) {
        byte[] data;
        try {
            AttachEntity entity = get(key);
            String path = entity.getPath() + "/" + key + "." + entity.getExtension();
            Path file = Paths.get(attachFolder + path);
            data = Files.readAllBytes(file);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }


    public ResponseEntity<Resource> download(String fileName) {
        try {
            AttachEntity entity = get(fileName);
            String path = entity.getPath() + "/" + fileName + "." + entity.getExtension();
            Path file = Paths.get(attachFolder + path);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + entity.getOriginalName() + "\"").body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("ERROR: " + e.getMessage());
        }
    }

    /* public Resource downloadOld(String fileName) {
         try {
             Path file = Paths.get("attaches/" + fileName);
             Resource resource = new UrlResource(file.toUri());

             if (resource.exists() || resource.isReadable()) {
                 return resource;
             } else {
                 throw new RuntimeException("Could not read the file!");
             }
         } catch (MalformedURLException e) {
             throw new RuntimeException("Error: " + e.getMessage());
         }
     }*/
    public String getYmDString() {
        int year = LocalDateTime.now().getYear();
        int month = LocalDateTime.now().getMonthValue();
        int day = LocalDateTime.now().getDayOfMonth();

        return year + "/" + month + "/" + day; // 2022/04/23
    }

    public String getExtension(String fileName) { // mp3/jpg/npg/mp4.....
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }


    public AttachEntity get(String id) {
        return attachRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Attach not found"));
    }

    public Page<AttachDTO> getPaginationUrl(Integer page, Integer size) {
        Pageable paging = PageRequest.of(page, size);

        Page<AttachEntity> all = attachRepository.findAll(paging);

        List<AttachEntity> allContent = all.getContent();

        List<AttachDTO> dtoList = new ArrayList<>();

        for (AttachEntity attach : allContent) {
            AttachDTO dto = toDTO(attach);
            dto.setUrl(attachOpenUrl + getExtension(attach.getId()));
            dtoList.add(dto);
        }

        return new PageImpl<>(dtoList, paging, all.getTotalElements());
    }

    public Boolean delete(String id) {
        AttachEntity attach = attachRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException("Not found");
        });

        File file = new File(attachFolder + attach.getPath() + "/" + id + "." + attach.getExtension());
        if (file.delete()) {
            attachRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
