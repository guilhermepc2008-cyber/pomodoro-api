package br.com.guilherme.pomodoro_api.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {
    @GetMapping("/")
    public Map<String,String> home(){
        return Map.of("message", "primeira versÃo api pomodoro");
    }
}
