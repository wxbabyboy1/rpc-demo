package rpc.demo.contract.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UserInfo implements Serializable {

    private int userId;

    private String userName;

    private int sex;

    private List<UserInfo> users;

    @Override
    public String toString() {
        return String.format(
                "{userId:%d,userName:'%s',sex:%d,users:[{%s}]}"
                ,this.userId
                ,this.userName
                ,this.sex
                ,this.users.get(0).getUserName()
        );
    }

}
