package br.com.guilherme.pomodoro_api.Controller;

import br.com.guilherme.pomodoro_api.models.entity.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    private List<Task> tasks = new ArrayList<>();

    public TaskController() {
        tasks.add(new Task(1L, "Estudar Spring Boot", false));
        tasks.add(new Task(2L, "Praticar a API REST", false));
        tasks.add(new Task(3L, "Revisar o código", true));
    }

    @GetMapping()
    public ResponseEntity<List<Task>> mostrarTasks(@RequestParam(required = false) Boolean completed){
        if (completed == null) {
            return ResponseEntity.ok(tasks);
        }
         return ResponseEntity.ok(tasks.stream().filter(t -> t.isCompleted() ==  completed).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> mostrarTaskId(@PathVariable Long id){
        return ResponseEntity.ok(tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null));
    }




}
