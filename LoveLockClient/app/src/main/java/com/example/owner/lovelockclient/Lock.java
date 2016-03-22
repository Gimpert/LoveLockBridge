package com.example.owner.lovelockclient;

import java.io.Serializable;

/**
 * Object representing a lock and key
 * Created by Corey Glasson on 2/12/2016.
 */
public class Lock implements Serializable {



    /**
     * The unique ID for the lock, generated from the MongoDB.
     * Represents the "key" for the lock
     */
    protected String id;

    /**
     * The password that changes whenever a key is transfered,
     * changing ownership of the key.
     */
    protected String password;
    /**
     * The name of the lock
     */
    protected String name;
    /**
     * The message contained within the lock
     */
    protected String message = null;

    protected Boolean isExpanded;

    public Lock(String id, String name){
        this.id = id;
        this.name = name;
    }

    public Lock(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.isExpanded = true;
    }

    public Lock(String id, String name, String message, String password) {
        this.id = id;
        this.name = name;
        this.message = message;
        this.password = password;
        this.isExpanded = true;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
