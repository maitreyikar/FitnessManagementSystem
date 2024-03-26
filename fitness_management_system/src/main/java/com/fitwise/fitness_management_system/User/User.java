package com.fitwise.fitness_management_system.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int u_id;
    private String u_name;
    private String u_email;
    private String u_password;  
    private long u_phone;
    private int u_age;
    private float u_height;
    private float u_weight;
    private Gender u_gender;

    
    public void set_id(int id){
        this.u_id = id;
    }
    public void set_email(String mail){
        this.u_email = mail;
    }
    public void set_name(String name){
        this.u_name = name;
    }
    public void set_phone(long phno){
        this.u_phone = phno;
    }
    public void set_password(String password){
        this.u_password = password;
    }
    public void set_height(float height){
        this.u_height = height;
    }
    public void set_weight(float weight){
        this.u_weight = weight;
    }
    public void set_age(int age){
        this.u_age = age;
    }
    public void set_gender(Gender gender){
        this.u_gender = gender;
    }


    public int get_id(){
        return u_id;
    }
    public String get_email(){
        return u_email;
    }
    public String get_name(){
        return u_name;
    }
    public long get_phone(){
        return u_phone;
    }
    public String get_password(){
        return u_password;
    }
    public float get_height(){
        return u_height;
    }
    public float get_weight(){
        return u_weight;
    }
    public int get_age(){
        return u_age;
    }
    public Gender get_gender(){
        return u_gender;
    }
}
