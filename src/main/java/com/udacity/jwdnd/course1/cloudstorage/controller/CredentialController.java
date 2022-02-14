package com.udacity.jwdnd.course1.cloudstorage.controller;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.entity.CredentialForm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
@RequestMapping("/credential")
public class CredentialController {
    CredentialService credentialService;
    EncryptionService encryptionService;
    UserService userService;

    // Constructor

    public CredentialController(CredentialService credentialService, EncryptionService encryptionService, UserService userService) {
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
        this.userService = userService;
    }

    @GetMapping(value = "/get-credential/{credentialId}")
    public Credential getCredential(@PathVariable Integer credentialId) {
        return credentialService.getCredential(credentialId);
    }
    // add or Update credential
    @PostMapping("add-credential")
    public String newCredential(
            Authentication authentication,
            @ModelAttribute("newCredential") CredentialForm newCredential,
            Model model) {
        String userName = authentication.getName();
        String url = newCredential.getUrl();
        String credentialId = newCredential.getCredentialId();
        String password = newCredential.getPassword();

        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);
        Integer userId = userService.getUserByName(userName).getUserId();
        if (credentialId.isEmpty()) {
            credentialService.addCredential(url, userName, newCredential.getUserName(), encodedKey, encryptedPassword);
        } else {
            Credential existingCredential = getCredential(Integer.parseInt(credentialId));
            credentialService.updateCredential(existingCredential.getCredentialId(), newCredential.getUserName(), url, encodedKey, encryptedPassword);
        }

        model.addAttribute("credentials", credentialService.getCredentialsList());
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("result", "success");

        return "result";
    }
// Delete credential
    @GetMapping(value = "/delete-credential/{credentialId}")
    public String deleteCredential(@PathVariable Integer credentialId, Model model) {
        credentialService.deleteCredential(credentialId);
        model.addAttribute("credentials", credentialService.getCredentialsList());
        model.addAttribute("result", "success");
        return "result";
    }

}
