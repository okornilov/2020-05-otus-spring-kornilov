package ru.otus.okornilov.homework13.repository;


import org.springframework.data.repository.CrudRepository;
import ru.otus.okornilov.homework13.domain.User;

public interface UserRepository extends CrudRepository<User, String> {
}
