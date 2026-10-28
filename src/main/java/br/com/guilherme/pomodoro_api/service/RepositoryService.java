package br.com.guilherme.pomodoro_api.service;

import br.com.guilherme.pomodoro_api.dto.TaskRequestDTO;
import br.com.guilherme.pomodoro_api.dto.TaskResponseDTO;
import br.com.guilherme.pomodoro_api.entity.Task;
import br.com.guilherme.pomodoro_api.repository.TaskRepository;
import org.hibernate.validator.internal.constraintvalidators.bv.NotBlankValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RepositoryService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private NotBlankValidator notBlankValidator;

    public TaskResponseDTO findTaskById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        return optionalTask.map(task -> new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.isCompleted()
        )).orElse(null);
    }

    public List<TaskResponseDTO> findAllTasks() {
        return taskRepository.findAll().stream()
                .map(task -> new TaskResponseDTO(
                        task.getId(),
                        task.getTitle(),
                        task.isCompleted()
                ))
                .collect(Collectors.toList());
    }

    public List<TaskResponseDTO> findTasksByCompleted(Boolean completed) {
        return taskRepository.findByCompleted(completed).stream()
                .map(task -> new TaskResponseDTO(
                        task.getId(),
                        task.getTitle(),
                        task.isCompleted()
                ))
                .collect(Collectors.toList());
    }

    public Optional<TaskResponseDTO> updateTask(Long id, TaskRequestDTO task) {
            return taskRepository.findById(id).map(t -> {
            t.setTitle(task.title());
            t.setCompleted(task.completed());
            taskRepository.save(t);
            return new TaskResponseDTO(t.getId(), t.getTitle(), t.isCompleted());
        });
    }

    public TaskResponseDTO postTask(TaskRequestDTO novatask) {
        Task task = new Task(novatask.title(), novatask.completed());
        taskRepository.save(task);
        return new TaskResponseDTO(task.getId(), task.getTitle(), task.isCompleted());

    }

    public void deletarTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Boolean existeTask(Long id) {
        Boolean existe = taskRepository.existsById(id);
        return existe;
    }

    public Optional<TaskResponseDTO> alterarCompleted(Long id) {
        return taskRepository.findById(id).map(t -> {
            t.setCompleted(!t.isCompleted());
            taskRepository.save(t);
            return new TaskResponseDTO(t.getId(), t.getTitle(), t.isCompleted());
        });
    }
}