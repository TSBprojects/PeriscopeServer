package ru.sstu.vak.periscope.periscopeserver.DAL.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sstu.vak.periscope.periscopeserver.DAL.entity.User;

import java.util.List;


@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
    User findByLoginAndHashPassword(String login, String hashPassword);
    User findByLogin(String login);
    User findByAuthToken(String authToken);
}

