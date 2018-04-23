package rpc.demo.server.imp;

import rpc.demo.contract.intf.UserIntf;
import rpc.demo.contract.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class UserImp implements UserIntf {

    @Override
    public List<UserInfo> findById(int userId) {
        List<UserInfo> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(i + 1);
            userInfo.setUserName("老金" + (i + 1));
            userInfo.setSex(i + 1);
            //List<UserInfo> users = new ArrayList<>();
            //users.add(userInfo);
            //userInfo.setUsers(users);

            list.add(userInfo);
        }

        return list;
    }

    @Override
    public UserInfo find(int userId) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId + 1);
        userInfo.setUserName("老金" + (userId + 1));
        userInfo.setSex(userId + 1);
        List<UserInfo> users = new ArrayList<>();
        users.add(userInfo);
        userInfo.setUsers(users);

        return userInfo;
    }

}
