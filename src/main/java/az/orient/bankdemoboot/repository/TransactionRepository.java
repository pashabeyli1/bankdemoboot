package az.orient.bankdemoboot.repository;

import az.orient.bankdemoboot.entity.Account;
import az.orient.bankdemoboot.entity.Customer;
import az.orient.bankdemoboot.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {


    List<Transaction> findAllByDtAccountAndActive(Account account, Integer active);
}
