package az.orient.bankdemoboot.service.impl;

import az.orient.bankdemoboot.dto.request.ReqLogin;
import az.orient.bankdemoboot.dto.request.ReqToken;
import az.orient.bankdemoboot.dto.response.RespStatus;
import az.orient.bankdemoboot.dto.response.RespToken;
import az.orient.bankdemoboot.dto.response.RespUser;
import az.orient.bankdemoboot.dto.response.Response;
import az.orient.bankdemoboot.entity.User;
import az.orient.bankdemoboot.entity.UserToken;
import az.orient.bankdemoboot.enums.EnumAvailableStatus;
import az.orient.bankdemoboot.exception.BankException;
import az.orient.bankdemoboot.exception.ExceptionConstants;
import az.orient.bankdemoboot.repository.UserRepository;
import az.orient.bankdemoboot.repository.UserTokenRepository;
import az.orient.bankdemoboot.service.UserService;
import az.orient.bankdemoboot.util.Utility;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
      private  final Utility utility;
    private final UserRepository userRepository;

    private final UserTokenRepository userTokenRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);



    @Override
    public Response<RespUser> login(ReqLogin reqLogin) {
        Response<RespUser> response = new Response<>();
        RespUser respUser = new RespUser();
        LOGGER.info("login request: "+reqLogin);
        try {
            String username = reqLogin.getUsername();
            String password = reqLogin.getPassword();
            if (username == null || password == null) {
                LOGGER.warn("login response: Invalid request data");
                throw new BankException(ExceptionConstants.INVALID_REQUEST_DATA,"Invalid request data");
            }
            User user = userRepository.findUserByUsernameAndPasswordAndActive(username,password, EnumAvailableStatus.ACTIVE.value);
            if (user == null) {
                LOGGER.warn("login response: User not found");
                throw new BankException(ExceptionConstants.USER_NOT_FOUND,"User not found");
            }
            String token = UUID.randomUUID().toString();
            UserToken userToken = UserToken.builder()
                    .user(user)
                    .token(token)
                    .build();
            userTokenRepository.save(userToken);
            respUser.setUsername(username);
            respUser.setFullName(user.getFullName());
            respUser.setRespToken(new RespToken(user.getId(), token));
            response.setT(respUser);
            response.setStatus(RespStatus.getSuccessMessage());
            LOGGER.warn("login response: success");
        } catch (BankException ex) {
            LOGGER.error("login error: ",ex);
            response.setStatus(new RespStatus(ex.getCode(),ex.getMessage()));
            ex.printStackTrace();
        } catch (Exception ex) {
            LOGGER.error("login error: ",ex);
            response.setStatus(new RespStatus(ExceptionConstants.INTERNAL_EXCEPTION,"Internal Exception"));
            ex.printStackTrace();
        }
        return response;
    }

    @Override
    public Response logout(ReqToken reqToken) {
        Response response = new Response();
        LOGGER.info("logout request: "+reqToken);
        try {
            UserToken userToken = utility.checkToken(reqToken);
            userToken.setActive(EnumAvailableStatus.DEACTIVE.value);
            userTokenRepository.save(userToken);
            response.setStatus(RespStatus.getSuccessMessage());
            LOGGER.info("logout response: success");
        }catch (BankException ex) {
            LOGGER.error("logout error: ",ex);
            response.setStatus(new RespStatus(ex.getCode(),ex.getMessage()));
            ex.printStackTrace();
        } catch (Exception ex) {
            LOGGER.error("logout error: ",ex);
            response.setStatus(new RespStatus(ExceptionConstants.INTERNAL_EXCEPTION,"Internal Exception"));
            ex.printStackTrace();
        }
        return response;
    }
}
