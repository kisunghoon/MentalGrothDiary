package com.zerobase.mentalgrowhdiary.dto;

import com.zerobase.mentalgrowhdiary.domain.User;
import com.zerobase.mentalgrowhdiary.type.Role;
import lombok.Data;

public class Auth {

    @Data
    public static class SignUp{
        private String username;
        private String password;
        private String email;
        private Role role;

        public User toEntity(){
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .build();
        }
    }

    @Data
    public static class SignIn{
        private String username;
        private String password;
    }


}
