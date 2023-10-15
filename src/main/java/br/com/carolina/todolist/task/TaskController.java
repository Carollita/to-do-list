package br.com.carolina.todolist.task;

import br.com.carolina.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    //* Cria tarefa
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

    //* Lista tarefas
    @GetMapping("/")
    // http://localhost:8080/tasks/
    public List<TaskModel> list(HttpServletRequest request) {
        // Pega o id do usuario autenticado e adiciona ao atributo idUser do taskModel convertendo para UUID, para que o idUser não seja retornado como null
        Object idUser = request.getAttribute("idUserObject");
        // Cria lista com todas as tarefas do usuario autenticado
        List<TaskModel> tasks = this.taskRepository.findByIdUser((UUID) idUser);
        return tasks;
    }

    //* Atualiza tarefa
    @PutMapping("/{id}")
    // http://localhost:8080/tasks/11f967c6-7099-4a4a-a9ce-5a5548f327ab
    public TaskModel update(@RequestBody TaskModel taskModel, @PathVariable UUID id, HttpServletRequest request) {
        Object idUser = request.getAttribute("idUserObject");

        TaskModel task = this.taskRepository.findById(id).orElse(null);
        Utils.copyNonNullProperties(taskModel, task);

        return this.taskRepository.save(task);
    }
}

