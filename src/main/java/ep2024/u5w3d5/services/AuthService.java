package ep2024.u5w3d5.services;

import ep2024.u5w3d5.entities.User;
import ep2024.u5w3d5.exceptions.UnauthorizedException;
import ep2024.u5w3d5.payloads.UserLoginDTO;
import ep2024.u5w3d5.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UsersService usersService;

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private JWTTools jwtTools;

    public String authenticateUserAndGenerateToken(UserLoginDTO payload){

        User user = this.usersService.findByEmail(payload.email());
        if(bcrypt.matches(payload.password(), user.getPassword())){
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizedException("Your credentials are incorrect!");
        }
    }
}
