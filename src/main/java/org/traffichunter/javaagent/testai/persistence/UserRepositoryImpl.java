package org.traffichunter.javaagent.testai.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.traffichunter.javaagent.testai.core.repository.UserRepository;
import org.traffichunter.javaagent.testai.domain.Users;
import org.traffichunter.javaagent.testai.persistence.jpa.JpaUserRepository;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    @Transactional
    public Users save(final Users user) {
        return jpaUserRepository.save(user);
    }

    @Override
    public Users findById(final Long id) {
        return jpaUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found user"));
    }

    @Override
    @Transactional
    public void deleteById(final Long id) {
        jpaUserRepository.deleteById(id);
    }
}
