package com.springboot.chatroom.dao.impl;

import com.springboot.chatroom.dao.UserDao;
import com.springboot.chatroom.model.po.Group;
import com.springboot.chatroom.model.po.User;
import com.springboot.chatroom.util.Constant;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {


    @Override
    public void loadUser() {
        // 设置用户基本信息，共9个用户
        User userInfo = new User("001", "Member001", "001", "static/img/avatar/Member001.jpg");
        User userInfo2 = new User("002", "Member002", "002", "static/img/avatar/Member002.jpg");
        User userInfo3 = new User("003", "Member003", "003", "static/img/avatar/Member003.jpg");
        User userInfo4 = new User("004", "Member004", "004", "static/img/avatar/Member004.jpg");
        User userInfo5 = new User("005", "Member005", "005", "static/img/avatar/Member005.jpg");
        User userInfo6 = new User("006", "Member006", "006", "static/img/avatar/Member006.jpg");
        User userInfo7 = new User("007", "Member007", "007", "static/img/avatar/Member007.jpg");
        User userInfo8 = new User("008", "Member008", "008", "static/img/avatar/Member008.jpg");
        User userInfo9 = new User("009", "Member009", "009", "static/img/avatar/Member009.jpg");

        // 设置用户好友列表
        userInfo.setFriendList(generateFriendList("001"));
        userInfo2.setFriendList(generateFriendList("002"));
        userInfo3.setFriendList(generateFriendList("003"));
        userInfo4.setFriendList(generateFriendList("004"));
        userInfo5.setFriendList(generateFriendList("005"));
        userInfo6.setFriendList(generateFriendList("006"));
        userInfo7.setFriendList(generateFriendList("007"));
        userInfo8.setFriendList(generateFriendList("008"));
        userInfo9.setFriendList(generateFriendList("009"));

        // 设置用户群列表，共1个群
        Group groupInfo = new Group("01", "Group01", "static/img/avatar/Group01.jpg", null);
        List<Group> groupList = new ArrayList<Group>();
        groupList.add(groupInfo);
        userInfo.setGroupList(groupList);
        userInfo2.setGroupList(groupList);
        userInfo3.setGroupList(groupList);
        userInfo4.setGroupList(groupList);
        userInfo5.setGroupList(groupList);
        userInfo6.setGroupList(groupList);
        userInfo7.setGroupList(groupList);
        userInfo8.setGroupList(groupList);
        userInfo9.setGroupList(groupList);

        Constant.userMap.put("Member001", userInfo);
        Constant.userMap.put("Member002", userInfo2);
        Constant.userMap.put("Member003", userInfo3);
        Constant.userMap.put("Member004", userInfo4);
        Constant.userMap.put("Member005", userInfo5);
        Constant.userMap.put("Member006", userInfo6);
        Constant.userMap.put("Member007", userInfo7);
        Constant.userMap.put("Member008", userInfo8);
        Constant.userMap.put("Member009", userInfo9);

    }

    @Override
    public User getByUsername(String username) {
        return Constant.userMap.get(username);
    }

    @Override
    public User getByUserId(String userId) {
        User user=null;
        Iterator<Map.Entry<String, User>> iterator = Constant.userMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, User> next = iterator.next();
            if (next.getValue().getUserId().equals(userId)){
                user=next.getValue();
                break;
            }
        }
        return user;
    }

    private List<User> generateFriendList(String userId) {
        User userInfo = new User("001", "Member001", "001", "static/img/avatar/Member001.jpg");
        User userInfo2 = new User("002", "Member002", "002", "static/img/avatar/Member002.jpg");
        User userInfo3 = new User("003", "Member003", "003", "static/img/avatar/Member003.jpg");
        User userInfo4 = new User("004", "Member004", "004", "static/img/avatar/Member004.jpg");
        User userInfo5 = new User("005", "Member005", "005", "static/img/avatar/Member005.jpg");
        User userInfo6 = new User("006", "Member006", "006", "static/img/avatar/Member006.jpg");
        User userInfo7 = new User("007", "Member007", "007", "static/img/avatar/Member007.jpg");
        User userInfo8 = new User("008", "Member008", "008", "static/img/avatar/Member008.jpg");
        User userInfo9 = new User("009", "Member009", "009", "static/img/avatar/Member009.jpg");
        List<User> friendList = new ArrayList<User>();
        friendList.add(userInfo);
        friendList.add(userInfo2);
        friendList.add(userInfo3);
        friendList.add(userInfo4);
        friendList.add(userInfo5);
        friendList.add(userInfo6);
        friendList.add(userInfo7);
        friendList.add(userInfo8);
        friendList.add(userInfo9);
        Iterator<User> iterator = friendList.iterator();
        while(iterator.hasNext()) {
            User entry = iterator.next();
            if (userId.equals(entry.getUserId())) {
                iterator.remove();
            }
        }
        return friendList;
    }
}
