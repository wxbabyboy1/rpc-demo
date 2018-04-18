package rpc.demo.contract.intf;

import rpc.demo.contract.model.UserInfo;

public interface UserIntf {

    public UserInfo findById(int userId);

}
