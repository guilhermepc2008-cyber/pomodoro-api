package br.com.guilherme.pomodoro_api.service;

import br.com.guilherme.pomodoro_api.dto.TaskRequestDTO;
import br.com.guilherme.pomodoro_api.dto.TaskResponseDTO;
import br.com.guilherme.pomodoro_api.entity.Task;
import br.com.guilherme.pomodoro_api.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    // Converte TaskRequestDTO (entrada) para Task (entidade)
    public Task toEntity(TaskRequestDTO dto) {
        Task task = new Task();
        task.setTitle(dto.title());
        // Tratamento de null para completed (Boolean -> boolean)
        task.setCompleted(dto.completed() != null ? dto.completed() : false);
        return task;
    }

    // Converte Task (entidade) para TaskResponseDTO (saída)
    public TaskResponseDTO toDTO(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.isCompleted()
        );
    }

    public TaskResponseDTO findTaskById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        return optionalTask.map(this::toDTO).orElse(null);
    }

    public List<TaskResponseDTO> findAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<TaskResponseDTO> findTasksByCompleted(Boolean completed) {
        // CORREÇÃO: antes carregava todas as tarefas (findAll) e filtrava em memória com Stream.
        // Agora usa o método findByCompleted do TaskRepository, filtrando direto no banco.
        return taskRepository.findByCompleted(completed).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<TaskResponseDTO> updateTask(Long id, TaskRequestDTO taskDto) {
        return taskRepository.findById(id).map(t -> {
            // CORREÇÃO: NullPointerException possível — completed é Boolean (pode ser null)
            // e setCompleted espera boolean primitivo. Tratamos o null com valor default false.
            t.setTitle(taskDto.title());
            t.setCompleted(taskDto.completed() != null ? taskDto.completed() : false);
            taskRepository.save(t);
            return toDTO(t);
        });
    }

    public TaskResponseDTO postTask(TaskRequestDTO novatask) {
        Task task = toEntity(novatask);
        taskRepository.save(task);
        return toDTO(task);
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
            return toDTO(t);
        });
    }
}