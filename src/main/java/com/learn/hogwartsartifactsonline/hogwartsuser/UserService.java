package com.learn.hogwartsartifactsonline.hogwartsuser;

import com.learn.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public List<HogwartsUser> findAll() {
        return this.userRepository.findAll();
    }

    public HogwartsUser findById(Integer userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

    public HogwartsUser save(HogwartsUser hogwartsUser) {
        hogwartsUser.setPassword(this.passwordEncoder.encode(hogwartsUser.getPassword()));
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .map(hogwartsUser -> new MyUserPrincipal(hogwartsUser))
                .orElseThrow(() -> new UsernameNotFoundException("username " + username + " not found"));

    }
}
