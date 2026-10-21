package br.com.guilherme.pomodoro_api.service;

import br.com.guilherme.pomodoro_api.dto.TaskDTO;
import br.com.guilherme.pomodoro_api.entity.Task;
import br.com.guilherme.pomodoro_api.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RepositoryService {

    @Autowired
    private static TaskRepository taskRepository;

    public  static TaskDTO findTaskById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        return optionalTask.map(task -> new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.isCompleted()
        )).orElse(null);
    }
}