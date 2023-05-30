package az.orient.bankdemoboot.repository;

import az.orient.bankdemoboot.entity.Account;
import az.orient.bankdemoboot.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

      List<Account> findAllByCustomerAndActive(Customer customer, Integer active);

      Account findAccountByIdAndActive(Long id,Integer active);


}
