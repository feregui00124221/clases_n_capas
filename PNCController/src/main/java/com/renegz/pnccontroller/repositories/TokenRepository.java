package com.renegz.pnccontroller.repositories;

import com.renegz.pnccontroller.domain.entities.Token;
import com.renegz.pnccontroller.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TokenRepository
        extends JpaRepository<Token, UUID> {

    List<Token> findByUserAndActive(User user, Boolean active);

}
