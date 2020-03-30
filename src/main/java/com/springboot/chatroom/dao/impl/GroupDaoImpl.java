package com.springboot.chatroom.dao.impl;

import com.springboot.chatroom.dao.GroupDao;
import com.springboot.chatroom.model.po.Group;
import com.springboot.chatroom.model.po.User;
import com.springboot.chatroom.util.Constant;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GroupDaoImpl implements GroupDao {

    @Override
    public void loadGroup() {
        User userInfo = new User("001", "Member001", "001", "img/avatar/Member001.jpg");
        User userInfo2 = new User("002", "Member002", "002", "img/avatar/Member002.jpg");
        User userInfo3 = new User("003", "Member003", "003", "img/avatar/Member003.jpg");
        User userInfo4 = new User("004", "Member004", "004", "img/avatar/Member004.jpg");
        User userInfo5 = new User("005", "Member005", "005", "img/avatar/Member005.jpg");
        User userInfo6 = new User("006", "Member006", "006", "img/avatar/Member006.jpg");
        User userInfo7 = new User("007", "Member007", "007", "img/avatar/Member007.jpg");
        User userInfo8 = new User("008", "Member008", "008", "img/avatar/Member008.jpg");
        User userInfo9 = new User("009", "Member009", "009", "img/avatar/Member009.jpg");
        List<User> members = new ArrayList<User>();
        members.add(userInfo);
        members.add(userInfo2);
        members.add(userInfo3);
        members.add(userInfo4);
        members.add(userInfo5);
        members.add(userInfo6);
        members.add(userInfo7);
        members.add(userInfo8);
        members.add(userInfo9);
        Group groupInfo = new Group("01", "Group01", "img/avatar/Group01.jpg", members);
        Constant.groupMap.put(groupInfo.getGroupId(), groupInfo);

    }

    @Override
    public Group getByGroupId(String groupId) {
        return Constant.groupMap.get(groupId);
    }
}
