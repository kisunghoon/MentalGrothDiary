package com.zerobase.mentalgrowhdiary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class MentalGrowhDiaryApplication {

    public static void main(String[] args) {
        SpringApplication.run(MentalGrowhDiaryApplication.class, args);


    }

}
