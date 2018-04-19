package rpc.demo.contract.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserInfo implements Serializable {

    private int userId;

    private String userName;

    private int sex;

    @Override
    public String toString() {
        return String.format(
                "{userId:%d,userName:'%s',sex:%d}",
                this.userId,
                this.userName,
                this.sex);
    }

}
