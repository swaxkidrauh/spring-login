package com.example.demo.user;


import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserAppService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "User with email %s not found";
    private final UserAppRepository userAppRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userAppRepository.findByEmail(email)
                .orElseThrow( () ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpUser(UserApp userApp) {
        boolean isUserExists = userAppRepository
                .findByEmail(userApp.getEmail())
                .isPresent();
        if(isUserExists) {
            throw new IllegalStateException( "email already taken");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(userApp.getPassword());

        userApp.setPassword(encodedPassword);
        userAppRepository.save(userApp);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                userApp
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public int enableUser(String email) {
        return userAppRepository.enableUser(email);
    }
}
