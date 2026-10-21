package br.com.guilherme.pomodoro_api.repository;

import br.com.guilherme.pomodoro_api.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

}