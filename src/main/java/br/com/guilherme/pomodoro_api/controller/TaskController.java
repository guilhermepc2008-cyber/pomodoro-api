package br.com.guilherme.pomodoro_api.controller;

import br.com.guilherme.pomodoro_api.dto.TaskRequestDTO;
import br.com.guilherme.pomodoro_api.dto.TaskResponseDTO;
import br.com.guilherme.pomodoro_api.entity.Task;
import br.com.guilherme.pomodoro_api.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// CORREÇÃO: removidos imports não utilizados (ArrayList e AtomicLong)
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping()
    public ResponseEntity<List<TaskResponseDTO>> mostrarTasks(@RequestParam(required = false) Boolean completed) {
        List<TaskResponseDTO> allTasks = taskService.findAllTasks();
        if (completed == null) {
            return ResponseEntity.ok(allTasks);
        }
        return ResponseEntity.ok(taskService.findTasksByCompleted(completed));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> mostrarTaskId(@PathVariable Long id) {
        TaskResponseDTO taskDTO = taskService.findTaskById(id);
        if (taskDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(taskDTO);
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO task) {
        TaskResponseDTO novatask = taskService.postTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(novatask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequestDTO update) {
        return ResponseEntity.of(taskService.updateTask(id, update));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TaskResponseDTO> patchCompleted(@PathVariable Long id) {
        return ResponseEntity.of(taskService.alterarCompleted(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskService.existeTask(id)) {
            taskService.deletarTask(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
