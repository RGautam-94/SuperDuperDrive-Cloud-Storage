package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.*;
import com.udacity.jwdnd.course1.cloudstorage.entity.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.entity.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.entity.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/home")
public class HomeController {
    private FileService fileService;
    private NoteService noteService;
    private CredentialService credentialService;
    private UserService userService;
    private EncryptionService encryptionService;

    public HomeController(FileService fileService, NoteService noteService, CredentialService
            credentialService, UserService userService, EncryptionService encryptionService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    // Get user Id
    private Integer getUserId(Authentication authentication) {
        String userName = authentication.getName();
        User user = userService.getUserByName(userName);
        return user.getUserId();
    }

    // Getting Home Page
    @GetMapping
    public String homePage(
            Authentication authentication,
            @ModelAttribute("newFile") FileForm newFile,
            @ModelAttribute("newCredential") CredentialForm newCredential,
            @ModelAttribute("newNote") NoteForm newNote, Model model) {
        Integer userId = getUserId(authentication);
        model.addAttribute("files", this.fileService.getFileList(userId));
        model.addAttribute("notes", noteService.getNoteList());
        model.addAttribute("credentials", credentialService.getCredentialsList());
        model.addAttribute("encryptionService", encryptionService);

        return "home";
    }

    // add a new file
    @PostMapping
    public String newFile(
            Authentication authentication, @ModelAttribute("newFile") FileForm newFile,
            Model model) throws IOException, IOException {
        String userName = authentication.getName();
        User user = userService.getUserByName(userName);
        MultipartFile multipartFile = newFile.getFile();
        String fileName = multipartFile.getOriginalFilename();
        Integer userId = user.getUserId();
        String[] fileList = fileService.getFileList(userId);
        boolean copyOfFile = false;
        for (String fileListing : fileList) {
            if (fileListing.equals(fileName)) {
                copyOfFile = true;
                break;
            }
        }
        if ((!copyOfFile) && (!multipartFile.isEmpty())) {
            fileService.addFile(multipartFile, userName);
            model.addAttribute("result", "success");
        } else {
            model.addAttribute("result", "error");
        }
        model.addAttribute("files", fileService.getFileList(userId));
        return "result";
    }

    // View or download file
    @GetMapping(value = "/file/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody
    byte[] getFile(@PathVariable String fileName) {
        return fileService.getFile(fileName).getFileData();
    }

    // Delete File
    @GetMapping(value = "/delete-file/{fileName}")
    public String deleteFile(
            Authentication authentication, @PathVariable String fileName, Model model, User user) {
        Integer userId = user.getUserId();
        fileService.deleteFile(fileName);
        model.addAttribute("files", fileService.getFileList(userId));
        model.addAttribute("result", "success");
        return "result";
    }


}
