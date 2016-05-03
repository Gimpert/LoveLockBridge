package com.example.owner.lovelockclient;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Joseph Gregory on 2/23/2016.
 */
public class LockList {
    public static String DEBUG_STORED_LOCKS_FILENAME = "\\app\\src\\main\\res\\testLocks";
    public static final String STORED_LOCKS_FILENAME = "locks";

    private static LockList instance = null;

    private ArrayList<Lock> locks;

    private LockList () {
        locks = new ArrayList<Lock>();
    }

    public static LockList getInstance() {
        if (instance != null) {
            return instance;
        } else {
            return instance = new LockList();
        }
    }

    public void loadLocks() {
        try {
            FileInputStream in;
            in = MainActivity.getContext().openFileInput(STORED_LOCKS_FILENAME);

            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                int curStart = 0;
                String lockId = line.substring(curStart, line.indexOf(':'));

                curStart = line.indexOf(':') + 1;//Advance cursor past delimeter
                String lockName = line.substring(curStart, line.indexOf(curStart, ':'));

                curStart = line.indexOf(curStart, ':') + 1;//Advance cursor past delimeter
                String lockMessage = line.substring(curStart); //TODO: what if this has a new line? then we can't use bufferedReader.newLine

                locks.add(new Lock(lockId, lockName, lockMessage));
            }
            inputStreamReader.close();
        } catch (Exception e) {

            //Log.d("exceptions", e.getStackTrace().toString());
        }
    }

    public void saveLocks() {
        try {
            FileOutputStream out;
            out = MainActivity.getContext().openFileOutput(STORED_LOCKS_FILENAME, Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out);

            Iterator<Lock> it = locks.iterator();
            while (it.hasNext()) {
                Lock lock = it.next();
                outputStreamWriter.write(lock.getId() + ":" + lock.getName() + ":" + lock.getMessage() + "\n");
            }

            outputStreamWriter.close();
        } catch (Exception e) {
            //Log.d("exceptions", e.getStackTrace().toString());
        }
    }

    public ArrayList getList() {
        return locks;
    }

    public int getCount(){ return locks.size();}

    public Lock getLock(int position) { return locks.get(position);}

    public void addLock(Lock lock){ locks.add(lock); }

    public void removeLock(Lock lock) {locks.remove(lock); }
}
