package az.orient.bankdemoboot.service.impl;

import az.orient.bankdemoboot.dto.request.ReqTransaction;
import az.orient.bankdemoboot.dto.response.*;
import az.orient.bankdemoboot.entity.Account;
import az.orient.bankdemoboot.entity.Transaction;
import az.orient.bankdemoboot.enums.EnumAvailableStatus;
import az.orient.bankdemoboot.exception.BankException;
import az.orient.bankdemoboot.exception.ExceptionConstants;
import az.orient.bankdemoboot.repository.AccountRepository;
import az.orient.bankdemoboot.repository.CustomerRepository;
import az.orient.bankdemoboot.repository.TransactionRepository;
import az.orient.bankdemoboot.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    private final AccountRepository accountRepository;

    @Override
    public Response<List<RespTransaction>> getTransactionList(Long accountId) {
        Response<List<RespTransaction>> response = new Response<>();
        try {
            if (accountId == null){
                throw new BankException(ExceptionConstants.INVALID_REQUEST_DATA,"Invalid request data");
            }
            Account account = accountRepository.findAccountByIdAndActive(accountId,EnumAvailableStatus.ACTIVE.value);
            if (account == null) {
                throw new BankException(ExceptionConstants.ACCOUNT_NOT_FOUND,"Account not found");
            }
            List<Transaction> transactionList = transactionRepository.findAllByDtAccountAndActive(account, EnumAvailableStatus.ACTIVE.value);
            if (transactionList.isEmpty()) {
                throw new BankException(ExceptionConstants.TRANSACTION_NOT_FOUND,"Transaction not found");
            }
            List<RespTransaction> respTransactionList = transactionList.stream().map(transaction -> mapping(transaction)).collect(Collectors.toList());
            response.setT(respTransactionList);
            response.setStatus(RespStatus.getSuccessMessage());
        } catch (BankException ex) {
            response.setStatus(new RespStatus(ex.getCode(),ex.getMessage()));
            ex.printStackTrace();
        } catch (Exception ex) {
            response.setStatus(new RespStatus(ExceptionConstants.INTERNAL_EXCEPTION,"Internal Exception"));
            ex.printStackTrace();
        }
        return response;
    }

    @Override
    public Response createOperation(ReqTransaction reqTransaction) {
        Response response = new Response();
        try {
            Long dtAccountId = reqTransaction.getDtAccountId();
            String crAccount = reqTransaction.getCrAccount();
            Double amount = reqTransaction.getAmount();
            Double commission = reqTransaction.getCommission();
            String currency = reqTransaction.getCurrency();
            if (dtAccountId == null || crAccount == null ||
             amount == null || commission == null || currency == null) {
                throw new BankException(ExceptionConstants.INVALID_REQUEST_DATA,"Invalid request data");
            }
            if (amount <= 0) {
                throw new BankException(ExceptionConstants.INVALID_AMOUNT,"Invalid amount");
            }
            Account dtAccount = accountRepository.findAccountByIdAndActive(dtAccountId,EnumAvailableStatus.ACTIVE.value);
            if (dtAccount == null) {
                throw new BankException(ExceptionConstants.DEBIT_ACCOUNT_NOT_FOUND,"Debit account not found");
            }
            Transaction transaction = Transaction.builder()
                    .dtAccount(dtAccount)
                    .crAccount(crAccount)
                    .amount(amount)
                    .commission(commission)
                    .currency(currency)
                    .build();
          transactionRepository.save(transaction);
          response.setStatus(RespStatus.getSuccessMessage());
        } catch (BankException ex) {
            response.setStatus(new RespStatus(ex.getCode(),ex.getMessage()));
            ex.printStackTrace();
        } catch (Exception ex) {
            response.setStatus(new RespStatus(ExceptionConstants.INTERNAL_EXCEPTION,"Internal Exception"));
            ex.printStackTrace();
        }
        return response;
    }


    private RespTransaction mapping(Transaction transaction) {
        RespCustomer respCustomer = RespCustomer.builder()
                .customerId(transaction.getDtAccount().getCustomer().getId())
                .name(transaction.getDtAccount().getCustomer().getName())
                .surname(transaction.getDtAccount().getCustomer().getSurname())
                .cif(transaction.getDtAccount().getCustomer().getCif())
                .build();

        RespAccount dtAccount = RespAccount.builder()
                .name(transaction.getDtAccount().getName())
                .accountNo(transaction.getDtAccount().getAccountNo())
                .iban(transaction.getDtAccount().getIban())
                .branchCode(transaction.getDtAccount().getBranchCode())
                .currency(transaction.getDtAccount().getCurrency())
                .respCustomer(respCustomer)
                .build();
        RespTransaction respTransaction = RespTransaction.builder()
                .dtAccount(dtAccount)
                .crAccount(transaction.getCrAccount())
                .amount(transaction.getAmount())
                .commission(transaction.getCommission())
                .trDate(transaction.getTrDate())
                .currency(transaction.getCurrency())
                .build();
        return respTransaction;
    }

}
