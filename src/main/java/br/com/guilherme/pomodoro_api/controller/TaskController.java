package br.com.guilherme.pomodoro_api.controller;

import br.com.guilherme.pomodoro_api.entity.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final List<Task> tasks = new ArrayList<>();

    public TaskController() {
        tasks.add(new Task(idGenerator.getAndIncrement(), "Estudar Spring Boot", false));
        tasks.add(new Task(idGenerator.getAndIncrement(), "Praticar a API REST", false));
        tasks.add(new Task(idGenerator.getAndIncrement(), "Revisar o código", true));
    }

    @GetMapping()
    public ResponseEntity<List<Task>> mostrarTasks(@RequestParam(required = false) Boolean completed) {
        if (completed == null) {
            return ResponseEntity.ok(tasks);
        }
        return ResponseEntity.ok(tasks.stream().filter(t -> t.isCompleted() == completed).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> mostrarTaskId(@PathVariable Long id) {
        return ResponseEntity.of(tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        task.setId(idGenerator.getAndIncrement());
        tasks.add(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task update) {
        return tasks.stream().filter(t -> t.getId().equals(id))
                .findFirst()
                .map(t ->
                {
                    t.setTitle(update.getTitle());
                    t.setCompleted(update.isCompleted());
                    return ResponseEntity.ok(t);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/alterar/{id}")
    public ResponseEntity<Task> alterarCompleted(@PathVariable Long id) {
        return ResponseEntity.of(tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .map(t -> {t.setCompleted(!t.isCompleted());
                                return t;
                }));
    }


}
