package az.orient.bankdemoboot.util;

import az.orient.bankdemoboot.dto.request.ReqToken;
import az.orient.bankdemoboot.entity.User;
import az.orient.bankdemoboot.entity.UserToken;
import az.orient.bankdemoboot.enums.EnumAvailableStatus;
import az.orient.bankdemoboot.exception.BankException;
import az.orient.bankdemoboot.exception.ExceptionConstants;
import az.orient.bankdemoboot.repository.UserRepository;
import az.orient.bankdemoboot.repository.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Utility {

    private final UserTokenRepository userTokenRepository;

    private final UserRepository userRepository;

    public UserToken checkToken(ReqToken reqToken) {
        Long userId = reqToken.getUserId();
        String token = reqToken.getToken();
        if (userId == null || token == null) {
            throw new BankException(ExceptionConstants.INVALID_REQUEST_DATA,"Invalid request data");
        }
        User user = userRepository.findUserByIdAndActive(userId, EnumAvailableStatus.ACTIVE.value);
        if (user == null) {
            throw new BankException(ExceptionConstants.USER_NOT_FOUND,"User not found");
        }
        UserToken userToken = userTokenRepository.findUserTokenByUserAndTokenAndActive(user,token,EnumAvailableStatus.ACTIVE.value);
        if (userToken == null) {
            throw new BankException(ExceptionConstants.INVALID_TOKEN,"Invalid token");
        }
        return userToken;
    }


}
