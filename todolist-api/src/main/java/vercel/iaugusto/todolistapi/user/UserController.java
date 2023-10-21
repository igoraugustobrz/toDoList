package vercel.iaugusto.todolistapi.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserRepository userRepository;
    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel) {
        var user = this.userRepository.findByUsername(userModel.getUsername());

        // Verifica se o usuário já exsite, caso já exista retorna o status HTTP 400
        if (user != null) {
            System.out.println("Username já existente.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existente.");
        }

        // Criptografa a senha do usuário utilizando a biblioteca BCrypt
        var passwordHash = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(passwordHash);

        var userCreated = this.userRepository.save(userModel);

        // Retorna o status HTTP 201, caso o usuário não exista (usuário criado)
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
