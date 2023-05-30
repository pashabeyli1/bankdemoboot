package az.orient.bankdemoboot.service;

import az.orient.bankdemoboot.dto.request.ReqLogin;
import az.orient.bankdemoboot.dto.request.ReqToken;
import az.orient.bankdemoboot.dto.response.RespUser;
import az.orient.bankdemoboot.dto.response.Response;

public interface UserService {

    Response<RespUser> login(ReqLogin reqLogin);

    Response logout(ReqToken reqToken);
}
