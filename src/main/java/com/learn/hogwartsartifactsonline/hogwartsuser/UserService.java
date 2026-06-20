package com.learn.hogwartsartifactsonline.hogwartsuser;

import com.learn.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<HogwartsUser> findAll() {
        return this.userRepository.findAll();
    }

    public HogwartsUser findById(Integer userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

    public HogwartsUser save(@Valid HogwartsUser hogwartsUser) {
        return this.userRepository.save(hogwartsUser);
    }

    public HogwartsUser update(Integer userId, HogwartsUser updateUser) {
        HogwartsUser oldUser = this.userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
        oldUser.setUsername(updateUser.getUsername());
        oldUser.setEnabled(updateUser.isEnabled());
        oldUser.setRoles(updateUser.getRoles());
        return this.userRepository.save(oldUser);
    }

    public void delete(Integer userId) {
        this.userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
        this.userRepository.deleteById(userId);
    }
}
