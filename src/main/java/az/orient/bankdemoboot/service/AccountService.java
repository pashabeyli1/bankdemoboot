package az.orient.bankdemoboot.service;

import az.orient.bankdemoboot.dto.request.ReqAccount;
import az.orient.bankdemoboot.dto.response.RespAccount;
import az.orient.bankdemoboot.dto.response.Response;

import java.util.List;

public interface AccountService {
    Response<List<RespAccount>> getAccountListByCustomerId(Long customerId);

    Response createAccount(ReqAccount reqAccount);
}
