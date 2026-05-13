package org.example.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.example.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByNickname(String nickname);

    List<UserProfile> findByPhoneNumberContaining(String partialPhoneNumber);

    List<UserProfile> findByBioIsNotNull();

    List<UserProfile> findByNicknameStartingWithIgnoreCase(String prefix);

    List<UserProfile> findByCreatedAtAfter(Instant createdAt);

    long countByPhoneNumberStartingWith(String prefix);
}
