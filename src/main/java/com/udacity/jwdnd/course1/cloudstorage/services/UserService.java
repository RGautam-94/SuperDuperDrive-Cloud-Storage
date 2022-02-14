package com.udacity.jwdnd.course1.cloudstorage.services;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {
    private UserMapper userMapper;
    private HashService hashService;


    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }
    // Checking username
    public boolean isUserNameAvailable(String username) {
        return userMapper.getUserByName(username) == null;
    }
    // get username
    public User getUserByName(String username) {
        return userMapper.getUserByName(username);
    }

    // get username
    public User getUserById(Integer userId) {return userMapper.getUserById(userId);
    }
    //create user
    public int createUser(User user) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        user = new User(null, user.getUserName(), encodedSalt, hashedPassword, user.getFirstName(), user.getLastName());
        return userMapper.insert(user);
    }
}
