package cat.itacademy.s05.t02.virtualpet.repository;

import cat.itacademy.s05.t02.virtualpet.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}
