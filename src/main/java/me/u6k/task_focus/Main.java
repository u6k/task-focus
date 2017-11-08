
package me.u6k.task_focus;

import java.util.Locale;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    @PostConstruct
    public void started() {
        Locale.setDefault(Locale.JAPANESE);
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo"));
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
