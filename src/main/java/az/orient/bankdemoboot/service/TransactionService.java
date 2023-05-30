package az.orient.bankdemoboot.service;

import az.orient.bankdemoboot.dto.request.ReqTransaction;
import az.orient.bankdemoboot.dto.response.RespTransaction;
import az.orient.bankdemoboot.dto.response.Response;

import java.util.List;

public interface TransactionService {
    Response<List<RespTransaction>> getTransactionList(Long accountId);

    Response createOperation(ReqTransaction reqTransaction);
}
