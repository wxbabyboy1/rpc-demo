package rpc.demo.contract.intf;

import rpc.demo.contract.model.UserInfo;

import java.util.List;

public interface UserIntf {

    public List<UserInfo> findById(int userId);

}
