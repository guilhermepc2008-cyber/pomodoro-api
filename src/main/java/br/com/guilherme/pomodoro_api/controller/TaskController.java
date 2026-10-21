package br.com.guilherme.pomodoro_api.controller;

import br.com.guilherme.pomodoro_api.dto.TaskDTO;
import br.com.guilherme.pomodoro_api.entity.Task;
import br.com.guilherme.pomodoro_api.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private RepositoryService repositoryService;
private final AtomicLong idGenerator = new AtomicLong(1);
    private final List<Task> tasks = new ArrayList<>();

    public TaskController() {
        tasks.add(new Task("Estudar Spring Boot", false));
        tasks.add(new Task("Praticar a API REST", false));
        tasks.add(new Task("Revisar o código", true));
        tasks.get(0).setId(idGenerator.getAndIncrement());
        tasks.get(1).setId(idGenerator.getAndIncrement());
        tasks.get(2).setId(idGenerator.getAndIncrement());
    }

    @GetMapping()
    public ResponseEntity<List<Task>> mostrarTasks(@RequestParam(required = false) Boolean completed) {
        if (completed == null) {
            return ResponseEntity.ok(tasks);
        }
        return ResponseEntity.ok(tasks.stream().filter(t -> t.isCompleted() == completed).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> mostrarTaskId(@PathVariable Long id) {
        TaskDTO taskDTO = repositoryService.findTaskById(id);
        if (taskDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(taskDTO);
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

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Task> alterarCompleted(@PathVariable Long id) {
        return ResponseEntity.of(tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .map(t -> {
                    t.setCompleted(!t.isCompleted());
                    return t;
                }));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        boolean removed = tasks.removeIf(t -> t.getId().equals(id));
        if (removed) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
