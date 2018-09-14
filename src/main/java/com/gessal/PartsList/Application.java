package com.gessal.PartsList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/* Так приложение становится запускаемым. Т.е. не нужно собирать war-файл и
 * закидывать его в TomCat.*/
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
