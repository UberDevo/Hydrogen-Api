package rip.vaxp.bridgeapi;

import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@ControllerAdvice(annotations = RestController.class)
public class WebApi {
    @Getter private static SettingsManager settingsManager = new SettingsManager();
    @Getter private static RedisManager redisManager = new RedisManager();

    public static void main(String[] args){
        if(!settingsManager.init(true) || !redisManager.init())
            return;

        SpringApplication.run(WebApi.class, args);
    }

}
