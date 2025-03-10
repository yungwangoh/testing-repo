package org.traffichunter.javaagent.testai.core.repository;

import org.traffichunter.javaagent.testai.domain.Users;

public interface UserRepository {

    Users save(Users user);

    Users findById(Long id);

    void deleteById(Long id);
}
