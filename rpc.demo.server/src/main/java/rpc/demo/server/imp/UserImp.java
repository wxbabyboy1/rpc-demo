package rpc.demo.server.imp;

import rpc.demo.contract.intf.UserIntf;
import rpc.demo.contract.model.UserInfo;

public class UserImp implements UserIntf {

    @Override
    public UserInfo findById(int userId) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(1);
        userInfo.setUserName("老金");
        userInfo.setSex(1);

        return userInfo;
    }

}
