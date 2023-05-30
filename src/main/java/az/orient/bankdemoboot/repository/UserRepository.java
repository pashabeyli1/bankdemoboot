package az.orient.bankdemoboot.repository;

import az.orient.bankdemoboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findUserByUsernameAndPasswordAndActive(String username,String password,Integer active);

    User findUserByIdAndActive(Long id,Integer active);

}
