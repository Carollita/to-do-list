package br.com.carolina.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.carolina.todolist.users.IUserRepository;
import br.com.carolina.todolist.users.UserModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String servletPath = request.getServletPath();

        if (servletPath.equals("/tasks/")) {
            //* Pega autenticação (usuario e senha)

            String authorization = request.getHeader("Authorization"); // Basic Y2Fyb2xpdGE6MTIzNA==
            String authEncoded = authorization.substring("Basic".length()).trim(); // Y2Fyb2xpdGE6MTIzNA==

            // Decodifica senha
            byte[] authDecoded = Base64.getDecoder().decode(authEncoded); // [B@4439a718
            String authString = new String(authDecoded); // carolita:1234

            String[] credentials = authString.split(":"); // [Ljava.lang.String;@6b66fcb6 // [carolita, 1234]
            String username = credentials[0]; // carolita
            String password = credentials[1]; // 1234

            //* Valida a autenticação - Usuario e senha
            UserModel user = this.userRepository.findByUsername(username);
            // Valida usuário
            if(user == null) {
                response.sendError(401);
            } else {
                // Valida senha
                BCrypt.Result passwordVerified = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if(passwordVerified.verified) {
                    request.setAttribute("idUserObject", user.getId()); // Define o valor id do usuario autenticado (do tipo UUID gerado anteriormente) no atributo criado
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}