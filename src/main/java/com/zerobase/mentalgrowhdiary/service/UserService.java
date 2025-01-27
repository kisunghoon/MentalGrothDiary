package com.zerobase.mentalgrowhdiary.service;

import com.zerobase.mentalgrowhdiary.domain.User;
import com.zerobase.mentalgrowhdiary.dto.Auth;
import com.zerobase.mentalgrowhdiary.exception.MentalGrowthException;
import com.zerobase.mentalgrowhdiary.repository.UserRepository;
import com.zerobase.mentalgrowhdiary.security.CustomUserDetail;
import com.zerobase.mentalgrowhdiary.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.USER_NOT_FOUND));

        return new CustomUserDetail(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole()
        );

    }

    public User register(Auth.SignUp user){

        boolean isExist = this.userRepository.existsByUsername(user.getUsername());
        boolean isEmailExist = this.userRepository.existsByEmail(user.getEmail());

        if(isExist){
            throw new MentalGrowthException(ErrorCode.USER_ALREADY_EXIST);
        }

        if(isEmailExist){
            throw new MentalGrowthException(ErrorCode.EMAIL_ALERADY_EXIST);
        }


        user.setPassword(passwordEncoder.encode(user.getPassword()));


        return userRepository.save(user.toEntity());
    }

    public User authenticate(Auth.SignIn signUser){

        var user = this.userRepository.findByUsername(signUser.getUsername())
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.USER_NOT_FOUND_ID));

        if(!passwordEncoder.matches(signUser.getPassword(), user.getPassword())){
            throw new MentalGrowthException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        return user;
    }
}
