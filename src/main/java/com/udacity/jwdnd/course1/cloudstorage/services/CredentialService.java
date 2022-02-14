package com.udacity.jwdnd.course1.cloudstorage.services;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import org.springframework.stereotype.Service;

@Service
public class CredentialService {
    private UserMapper userMapper;
    private CredentialMapper credentialMapper;

    //Constructor
    public CredentialService(UserMapper userMapper, CredentialMapper credentialMapper) {
        this.userMapper = userMapper;
        this.credentialMapper = credentialMapper;
    }

    //Credential list
    public Credential[] getCredentialsList() {
        return credentialMapper.getCredentialList();
    }

    // Get a credential by credential Id
    public Credential getCredential(Integer credentialId) {
        return credentialMapper.getCredential(credentialId);
    }


    //Add Credential
    public void addCredential(String url, String userName,String credUserName, String key, String password) {
        Integer userId = userMapper.getUserByName(userName).getUserId();
        Credential credential = new Credential(0, url, credUserName, key, password,userId);
        credentialMapper.insert(credential);
    }
    //Update Credential
    public void updateCredential(Integer credentialId, String userName, String url, String key, String password){
        credentialMapper.updateCredential(credentialId,userName,url,key,password);
    }
    //Delete credential
    public void deleteCredential(Integer credentialId){
        credentialMapper.deleteCredential(credentialId);
    }
}
