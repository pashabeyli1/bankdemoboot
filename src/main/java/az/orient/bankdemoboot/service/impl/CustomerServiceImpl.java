package az.orient.bankdemoboot.service.impl;

import az.orient.bankdemoboot.dto.request.ReqCustomer;
import az.orient.bankdemoboot.dto.request.ReqToken;
import az.orient.bankdemoboot.dto.response.RespCustomer;
import az.orient.bankdemoboot.dto.response.RespStatus;
import az.orient.bankdemoboot.dto.response.Response;
import az.orient.bankdemoboot.entity.Customer;
import az.orient.bankdemoboot.entity.User;
import az.orient.bankdemoboot.entity.UserToken;
import az.orient.bankdemoboot.enums.EnumAvailableStatus;
import az.orient.bankdemoboot.exception.BankException;
import az.orient.bankdemoboot.exception.ExceptionConstants;
import az.orient.bankdemoboot.repository.CustomerRepository;
import az.orient.bankdemoboot.repository.UserRepository;
import az.orient.bankdemoboot.repository.UserTokenRepository;
import az.orient.bankdemoboot.service.CustomerService;
import az.orient.bankdemoboot.util.Utility;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final UserTokenRepository userTokenRepository;

    private final UserRepository userRepository;

    private final Utility utility;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);


    @Override
    public Response<List<RespCustomer>> getCustomerList(ReqToken reqToken) {
        Response<List<RespCustomer>> response = new Response<>();
        LOGGER.info("getCustomerList request: "+reqToken);
        try {
            utility.checkToken(reqToken);
            List<Customer> customerList = customerRepository.findAllByActive(EnumAvailableStatus.ACTIVE.value);
            if (customerList.isEmpty()) {
                LOGGER.warn("getCustomerList response: Customer not found");
                throw new BankException(ExceptionConstants.CUSTOMER_NOT_FOUND,"Customer not found");
            }

            List<RespCustomer> respCustomerList = customerList.stream().map(this::mapping).collect(Collectors.toList());
            response.setT(respCustomerList);
            response.setStatus(RespStatus.getSuccessMessage());
            LOGGER.info("getCustomerList response: success ; "+respCustomerList);
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
    public Response<RespCustomer> getCustomerById(ReqCustomer reqCustomer) {
        Response<RespCustomer> response = new Response<>();
        try {
            Long customerId = reqCustomer.getCustomerId();
            utility.checkToken(reqCustomer.getReqToken());
            if (customerId == null) {
                throw new BankException(ExceptionConstants.INVALID_REQUEST_DATA,"Invalid request data");
            }
            Customer customer = customerRepository.findCustomerByIdAndActive(customerId,EnumAvailableStatus.ACTIVE.value);
            if (customer == null) {
                throw new BankException(ExceptionConstants.CUSTOMER_NOT_FOUND,"Customer not found");
            }
            RespCustomer respCustomer = mapping(customer);
            response.setT(respCustomer);
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
    public Response addCustomer(ReqCustomer reqCustomer) {
        Response response = new Response();
        try {
            String name = reqCustomer.getName();
            String surname = reqCustomer.getSurname();
            ReqToken reqToken=reqCustomer.getReqToken();
            utility.checkToken(reqToken);
            if (name == null || surname == null) {
                throw new BankException(ExceptionConstants.INVALID_REQUEST_DATA,"Invalid request data");
            }
            Customer customer = Customer.builder().
                    name(name).
                    surname(surname).
                    address(reqCustomer.getAddress()).
                    phone(reqCustomer.getPhone()).
                    dob(reqCustomer.getDob()).
                    pin(reqCustomer.getPin()).
                    seria(reqCustomer.getSeria()).
                    cif(reqCustomer.getCif()).
                    build();
            customerRepository.save(customer);
            response.setStatus(RespStatus.getSuccessMessage());
        }  catch (BankException ex) {
            response.setStatus(new RespStatus(ex.getCode(),ex.getMessage()));
            ex.printStackTrace();
        } catch (Exception ex) {
            response.setStatus(new RespStatus(ExceptionConstants.INTERNAL_EXCEPTION,"Internal Exception"));
            ex.printStackTrace();
        }
        return response;
    }

    @Override
    public Response updateCustomer(ReqCustomer reqCustomer) {
        Response response = new Response();
        try {
            String name = reqCustomer.getName();
            String surname = reqCustomer.getSurname();
            Long customerId = reqCustomer.getCustomerId();
            ReqToken reqToken=reqCustomer.getReqToken();
            utility.checkToken(reqToken);
            if (name == null || surname == null || customerId == null) {
                throw new BankException(ExceptionConstants.INVALID_REQUEST_DATA,"Invalid request data");
            }
            Customer customer = customerRepository.findCustomerByIdAndActive(customerId,EnumAvailableStatus.ACTIVE.value);
            if (customer == null) {
                throw new BankException(ExceptionConstants.CUSTOMER_NOT_FOUND,"Customer not found");
            }
            customer.setName(name);
            customer.setSurname(surname);
            customer.setAddress(reqCustomer.getAddress());
            customer.setDob(reqCustomer.getDob());
            customer.setPhone(reqCustomer.getPhone());
            customer.setPin(reqCustomer.getPin());
            customer.setSeria(reqCustomer.getSeria());
            customer.setCif(reqCustomer.getCif());
            customerRepository.save(customer);
            response.setStatus(RespStatus.getSuccessMessage());
        }  catch (BankException ex) {
            response.setStatus(new RespStatus(ex.getCode(),ex.getMessage()));
            ex.printStackTrace();
        } catch (Exception ex) {
            response.setStatus(new RespStatus(ExceptionConstants.INTERNAL_EXCEPTION,"Internal Exception"));
            ex.printStackTrace();
        }
        return response;
    }

    @Override
    public Response deleteCustomer(ReqCustomer reqCustomer) {
        Response response = new Response();
        try {
            Long customerId= reqCustomer.getCustomerId();
            ReqToken reqToken=reqCustomer.getReqToken();
            utility.checkToken(reqToken);
            if (customerId == null) {
                throw new BankException(ExceptionConstants.INVALID_REQUEST_DATA,"Invalid request data");
            }
            Customer customer = customerRepository.findCustomerByIdAndActive(customerId,EnumAvailableStatus.ACTIVE.value);
            if (customer == null) {
                throw new BankException(ExceptionConstants.CUSTOMER_NOT_FOUND,"Customer not found");
            }
            customer.setActive(EnumAvailableStatus.DEACTIVE.value);
            customerRepository.save(customer);
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


    private RespCustomer mapping(Customer customer) {
        RespCustomer respCustomer = RespCustomer.builder().
                customerId(customer.getId()).
                name(customer.getName()).
                surname(customer.getSurname()).
                phone(customer.getPhone()).
                address(customer.getAddress()).
                pin(customer.getPin()).
                seria(customer.getSeria()).
                dob(customer.getDob()).
                cif(customer.getCif()).
                build();
        return respCustomer;
    }




}
