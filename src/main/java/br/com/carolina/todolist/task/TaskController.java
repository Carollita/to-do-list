package br.com.carolina.todolist.task;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    // http://localhost:8080/tasks/
    public TaskModel create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        // Pega o id do usuario autenticado e adiciona ao atributo idUser do taskModel convertendo para UUID, para que o idUser não seja retornado como null
        Object idUser = request.getAttribute("idUserObject");
        taskModel.setIdUser((UUID) idUser);

        TaskModel task = this.taskRepository.save(taskModel);
        return task;
    }
}
