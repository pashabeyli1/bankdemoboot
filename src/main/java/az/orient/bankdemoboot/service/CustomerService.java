package az.orient.bankdemoboot.service;

import az.orient.bankdemoboot.dto.request.ReqCustomer;
import az.orient.bankdemoboot.dto.request.ReqToken;
import az.orient.bankdemoboot.dto.response.RespCustomer;
import az.orient.bankdemoboot.dto.response.Response;

import java.util.List;

public interface CustomerService {

     Response<List<RespCustomer>> getCustomerList(ReqToken reqToken);

     Response<RespCustomer> getCustomerById(ReqCustomer reqCustomer);

     Response addCustomer(ReqCustomer reqCustomer);

     Response updateCustomer(ReqCustomer reqCustomer);

     Response deleteCustomer(ReqCustomer reqCustomer);
}
