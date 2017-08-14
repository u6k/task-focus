
package me.u6k.task_focus.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    @Query("select t from Task t where t.date = :date")
    List<Task> findByDate(@Param("date") Date date);

}
