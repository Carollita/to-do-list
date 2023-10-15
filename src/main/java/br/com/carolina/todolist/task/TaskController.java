package br.com.carolina.todolist.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    //http://localhost:8080/tasks/
    public TaskModel create(@RequestBody TaskModel taskModel) {
        TaskModel task = this.taskRepository.save(taskModel);
        return task;
    }
}
