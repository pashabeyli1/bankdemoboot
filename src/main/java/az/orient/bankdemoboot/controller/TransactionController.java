package az.orient.bankdemoboot.controller;

import az.orient.bankdemoboot.dto.request.ReqTransaction;
import az.orient.bankdemoboot.dto.response.RespTransaction;
import az.orient.bankdemoboot.dto.response.Response;
import az.orient.bankdemoboot.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

      private final TransactionService transactionService;


      @GetMapping("/GetTransactionList/{accountId}")
      public Response<List<RespTransaction>> getTransactionList(@PathVariable Long accountId) {
            return transactionService.getTransactionList(accountId);
      }

      @PostMapping("/CreateOperation")
      public Response createOperation(@RequestBody ReqTransaction reqTransaction) {
            return transactionService.createOperation(reqTransaction);
      }




}
