package com.udacity.jwdnd.course1.cloudstorage.mapper;


import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import org.apache.ibatis.annotations.*;


@Mapper
public interface CredentialMapper {

    // Make a credentials list
    @Select("SELECT * FROM CREDENTIALS")
    Credential[] getCredentialList();

    //Select Credentials from credential ID
    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    Credential getCredential(Integer credentialId);

    // Insert credential into credential form
    @Insert("INSERT INTO CREDENTIALS (url, username, key, password,userid) " +
            "VALUES(#{url}, #{userName}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insert(Credential credential);

    // Update statement for a credential
    @Update("UPDATE CREDENTIALS SET url = #{url}, key = #{key}, password = #{password}, username = #{userName} WHERE credentialid = #{credentialId}")
    void updateCredential(Integer credentialId, String userName, String url, String key, String password);

    //Delete a credential by credential ID
    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    void deleteCredential(Integer credentialId);


}
