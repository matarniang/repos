package com.spring.verification.springbackendverification.repository;
import com.spring.verification.springbackendverification.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;


@Repository
@Transactional(readOnly = true)
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByLoginad(String loginad);
    @Transactional
    @Modifying
    @Query("UPDATE AppUser a SET a.enabled=true WHERE a.email=?1")
    int enableAppUser(String email);
	@Query("select p from AppUser p where loginad= ?1")
	AppUser getUser(String loginad);
	@Query("select p from AppUser p where email= ?1")
	AppUser getUserEmail(String email);
		
}
