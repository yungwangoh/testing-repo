package org.traffichunter.javaagent.testai.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.traffichunter.javaagent.testai.domain.Users;

public interface JpaUserRepository extends JpaRepository<Users, Long> {
}
