package br.com.guilherme.pomodoro_api.service;

import br.com.guilherme.pomodoro_api.dto.TaskDTO;
import br.com.guilherme.pomodoro_api.entity.Task;
import br.com.guilherme.pomodoro_api.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RepositoryService {

    @Autowired
    private TaskRepository taskRepository;

    public TaskDTO findTaskById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        return optionalTask.map(task -> new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.isCompleted()
        )).orElse(null);
    }

    public List<TaskDTO> findAllTasks() {
        return taskRepository.findAll().stream()
                .map(task -> new TaskDTO(
                        task.getId(),
                        task.getTitle(),
                        task.isCompleted()
                ))
                .collect(Collectors.toList());
    }

    public List<TaskDTO> findTasksByCompleted(Boolean completed) {
        return taskRepository.findAll().stream()
                .filter(task -> task.isCompleted() == completed)
                .map(task -> new TaskDTO(
                        task.getId(),
                        task.getTitle(),
                        task.isCompleted()
                ))
                .collect(Collectors.toList());
    }

    public Optional<TaskDTO> updateTask(Long id,Task task){
        return taskRepository.findById(id).map(t -> {t.setTitle(task.getTitle());
            t.setCompleted(task.isCompleted());
            taskRepository.save(t);
            return new TaskDTO(t.getId(), t.getTitle(), t.isCompleted());

        });
    }

    public void postTask(Task task) {
        taskRepository.save(task);
    }

    public void deletarTask(Long id) {

        taskRepository.deleteById(id);

    }

    public Boolean existeTask(Long id) {
        Boolean existe = taskRepository.existsById(id);
        return existe;
    }
}