package com.example.deni.chartcollaboration.model;

/**
 * Created by deni on 28/07/2018.
 */

public class AccountManager {
//    $result, array('id_account'=>$row[0],
// 'username'=>$row[1],
// 'password'=>$row[2],
// 'email'=>$row[3],
// 'last_login'=>$row[4],
// 'status'=>$row[5]));



    String id_account;
    String username;
    String password;
    String email;
    String last_login;
    String status;

    public String getIdAccount(){
        return id_account;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getEmail(){
        return email;
    }
    public String getLastLogin(){
        return last_login;
    }
    public String getStatus(){
        return status;
    }



}


