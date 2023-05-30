package az.orient.bankdemoboot.repository;

import az.orient.bankdemoboot.entity.User;
import az.orient.bankdemoboot.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken,Long> {

    UserToken findUserTokenByUserAndTokenAndActive(User user,String token,Integer active);

}
