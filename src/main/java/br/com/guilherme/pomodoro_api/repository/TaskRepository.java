package br.com.guilherme.pomodoro_api.repository;

import br.com.guilherme.pomodoro_api.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCompleted(Boolean completed);
}