package ep2024.u5w3d5.controllers;

import ep2024.u5w3d5.exceptions.BadRequestException;
import ep2024.u5w3d5.payloads.NewUserDTO;
import ep2024.u5w3d5.payloads.NewUserResponseDTO;
import ep2024.u5w3d5.payloads.UserLoginDTO;
import ep2024.u5w3d5.payloads.UserLoginResponseDTO;
import ep2024.u5w3d5.services.AuthService;
import ep2024.u5w3d5.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UsersService usersService;

    @PostMapping("/login")
    public UserLoginResponseDTO login(@RequestBody UserLoginDTO payload){
        return new UserLoginResponseDTO(authService.authenticateUserAndGenerateToken(payload));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewUserResponseDTO saveUser(@RequestBody @Validated NewUserDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors());
        }
        return new NewUserResponseDTO(this.usersService.save(body).getId());
    }
}
