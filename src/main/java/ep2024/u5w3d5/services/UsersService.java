package ep2024.u5w3d5.services;

import ep2024.u5w3d5.entities.User;
import ep2024.u5w3d5.exceptions.BadRequestException;
import ep2024.u5w3d5.exceptions.NotFoundException;
import ep2024.u5w3d5.payloads.NewUserDTO;
import ep2024.u5w3d5.repositories.UsersDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsersService {
    @Autowired
    private UsersDAO usersDAO;

    @Autowired
    private PasswordEncoder bcrypt;

    public Page<User> getUsers(int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 20) pageSize = 20;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return usersDAO.findAll(pageable);
    }

    public User findById(UUID userId) {
        return this.usersDAO.findById(userId).orElseThrow(() -> new NotFoundException(userId));
    }

    public User findByEmail(String email){
        return usersDAO.findByEmail(email).orElseThrow(() -> new NotFoundException("Utente con email " + email + " non trovato!"));
    }

    public User save(NewUserDTO body) {
        this.usersDAO.findByEmail(body.email()).ifPresent(
                user -> {
                    throw new BadRequestException("The email address: " + body.email() + " is already in use!");
                }
        );

        User newUser = new User(body.name(), body.email(), bcrypt.encode(body.password()));
        return usersDAO.save(newUser);
    }
}