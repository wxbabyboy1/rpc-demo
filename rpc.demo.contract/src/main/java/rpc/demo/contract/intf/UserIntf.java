package rpc.demo.contract.intf;

import rpc.demo.contract.model.Ad;
import rpc.demo.contract.model.UserInfo;

import java.util.List;

public interface UserIntf {

    public List<UserInfo> findById(int userId);

    public UserInfo find(int userId);

    public UserInfo[] findByIds(int[] userId);

    public List<Ad> findByUserinfos(List<UserInfo> userInfos);

}
