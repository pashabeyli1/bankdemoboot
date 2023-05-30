package az.orient.bankdemoboot.repository;


import az.orient.bankdemoboot.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

	Users findUsersByEmailAndActive(String email, Integer active);
	
}
