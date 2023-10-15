package br.com.carolina.todolist.task;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    // http://localhost:8080/tasks/
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        // Pega o id do usuario autenticado e adiciona ao atributo idUser do taskModel convertendo para UUID, para que o idUser não seja retornado como null
        Object idUser = request.getAttribute("idUserObject");
        taskModel.setIdUser((UUID) idUser);

        // Valida data e horário
        LocalDateTime currentDate = LocalDateTime.now();
        if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data e horário de início e fim da tarefa deve ser maior que a data e horário atual.");
        }

        if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data e horário de início da tarefa deve ser menor que a data e horário do término da tarefa!");
        }

        TaskModel task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }
}
