package com.learn.hogwartsartifactsonline.hogwartsuser;

import com.learn.hogwartsartifactsonline.client.ai.chat.rediscache.RedisCacheClient;
import com.learn.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import com.learn.hogwartsartifactsonline.system.exception.PasswordChangeIllegalArgumentException;
import jakarta.validation.Valid;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final PasswordEncoder passwordEncoder;

    private final RedisCacheClient redisCacheClient;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RedisCacheClient redisCacheClient) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisCacheClient = redisCacheClient;
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //if the user is not admin then the user can only update username
        if(authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_admin"))){
            oldUser.setUsername(updateUser.getUsername());
        }else{
            oldUser.setUsername(updateUser.getUsername());
            oldUser.setEnabled(updateUser.isEnabled());
            oldUser.setRoles(updateUser.getRoles());

            //revoke JWT when user is updated
            this.redisCacheClient.delete("whitelist:"+userId);
        }
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

    public HogwartsUser changePassword(Integer userId, String oldPassword, String newPassword, String confirmPassword) {
        HogwartsUser user = this.userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));

        // if the oldPassword does not match the password in the database
        if(!this.passwordEncoder.matches(oldPassword, user.getPassword())){
            throw new BadCredentialsException("old password is incorrect");
        }

        // if the newPassword and confirmPassword are different
        if(!newPassword.equals(confirmPassword)){
            throw new PasswordChangeIllegalArgumentException("new password and confirm password are not the same");
        }

        //new password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character
        if(!newPassword.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$")){
            throw new PasswordChangeIllegalArgumentException("new password does not meet the password policy");
        }

        user.setPassword(this.passwordEncoder.encode(newPassword));

        //revoke user's current JWT by deleting it from redis
        this.redisCacheClient.delete("whitelist:"+user.getId());
        return this.userRepository.save(user);
    }
}
