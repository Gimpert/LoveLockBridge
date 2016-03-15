package com.example.owner.lovelockclient;

import java.io.Serializable;

/**
 * Object representing a lock and key
 * Created by Corey Glasson on 2/12/2016.
 */
public class Lock implements Serializable {



    /**
     * The unique ID for the lock.
     * Represents the "key" for the lock
     */
    protected String id;
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

    public Lock(String id, String name, String message) {
        this.id = id;
        this.name = name;
        this.message = message;
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



}
