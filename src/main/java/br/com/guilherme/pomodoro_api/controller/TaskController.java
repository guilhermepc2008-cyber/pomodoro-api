package br.com.guilherme.pomodoro_api.controller;

import br.com.guilherme.pomodoro_api.dto.TaskRequestDTO;
import br.com.guilherme.pomodoro_api.dto.TaskResponseDTO;
import br.com.guilherme.pomodoro_api.entity.Task;
import br.com.guilherme.pomodoro_api.service.RepositoryService;
import jakarta.validation.Valid;
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

    @GetMapping()
    public ResponseEntity<List<TaskResponseDTO>> mostrarTasks(@RequestParam(required = false) Boolean completed) {
        List<TaskResponseDTO> allTasks = repositoryService.findAllTasks();
        if (completed == null) {
            return ResponseEntity.ok(allTasks);
        }
        return ResponseEntity.ok(repositoryService.findTasksByCompleted(completed));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> mostrarTaskId(@PathVariable Long id) {
        TaskResponseDTO taskDTO = repositoryService.findTaskById(id);
        if (taskDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(taskDTO);
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO task) {
        TaskResponseDTO novatask = repositoryService.postTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(novatask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @RequestBody Task update) {
        return ResponseEntity.of(repositoryService.updateTask(id, update));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TaskResponseDTO> patchCompleted(@PathVariable Long id) {
        return ResponseEntity.of(repositoryService.alterarCompleted(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (repositoryService.existeTask(id)) {
            repositoryService.deletarTask(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
