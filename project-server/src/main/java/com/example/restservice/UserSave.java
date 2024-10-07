package com.example.restservice;

import java.io.*;
import java.util.ArrayList;

public class UserSave implements Serializable {
    private final ArrayList<User> users;

    public UserSave() {
        users = new ArrayList<>();
        output();
    }

    public void output() {
        // conserve la sauvegarde de le fichier
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(new FileOutputStream("user.ser"));
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static UserSave input() {
        // recupere la sauvegarde du fichier et cree une nouvelle si le fichier est vide
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(new FileInputStream("user.ser"));
            return (UserSave) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new UserSave();
        }
    }


    public User getUser(String name) {
        return users.get(getUserID(name));
    }

    public int getUserID(String name) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getName().equals(name))
                return i;
        }
        return -1;
    }

    public void addNewUser(User u) {
        users.add(u);
        output();
    }

    public void updateUser(User user) {
        int index = 0;
        for (User u : users) {
            if (u.getName().equals(user.getName()))
                if (u.getPassword().equals(user.getPassword())) {
                    index = users.indexOf(u);
                }
        }
        users.set(index, user);
    }

    public boolean existDeja(String name) {
        for (User u : users) {
            if (u.getName().equals(name))
                return true;
        }
        return false;
    }
}
