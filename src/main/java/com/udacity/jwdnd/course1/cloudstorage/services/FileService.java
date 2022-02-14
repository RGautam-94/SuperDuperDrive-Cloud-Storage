package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
public class FileService {
    private FileMapper fileMapper;
    private UserMapper userMapper;

    public FileService(FileMapper fileMapper, UserMapper userMapper) {
        this.fileMapper = fileMapper;
        this.userMapper = userMapper;
    }

    // Get file
    public File getFile(String fileName) {
        return fileMapper.getFile(fileName);
    }

    // Add file
    public void addFile(MultipartFile multipartFile, String userName) throws IOException {
        byte[] fileData = multipartFile.getBytes();
        String fileName = multipartFile.getOriginalFilename();
        String fileSize = String.valueOf(multipartFile.getContentType());
        String contentType = multipartFile.getContentType();
        Integer userId = userMapper.getUserByName(userName).getUserId();
        File file = new File(0, fileName, contentType, fileSize, userId, fileData);
        fileMapper.insert(file);
    }

    // Get file list
    public String[] getFileList(Integer userId) {
        return fileMapper.getFileList(userId);
    }

    // Delete file
    public void deleteFile(String fileName) {
        fileMapper.deleteFile(fileName);
    }
}
