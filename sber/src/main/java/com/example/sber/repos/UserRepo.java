package com.example.sber.repos;

import com.example.sber.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepo extends CrudRepository<User, Long> {

    List<User> findByName(String name);

    User findFirstById (Integer id);
}
