package com.github.apache_foundation.jerrymouse.http.Session;

import javax.servlet.http.HttpSession;
import java.io.*;

public class FileStore implements Store {
    private SessionManager sessionManager;
    private String filePath;


    public FileStore(SessionManager sessionManager, String filePath) {
        this.sessionManager = sessionManager;
        this.filePath = filePath;
    }

    @Override
    public void load() {
        File fileD = new File(filePath);
        try {
            if (fileD.isDirectory()) {
                File[] files = fileD.listFiles();
                for (File f : files) {
                    FileInputStream fis = new FileInputStream(f);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    HttpSession httpSession = (HttpSession) ois.readObject();
                    sessionManager.putSession(httpSession);
                }
            } else {
                throw new IOException("文件目录错误");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        File fileD = new File(filePath);

        try {
            if (fileD.isDirectory()) {
                HttpSession[] httpSessions = sessionManager.findSessions();
                for (HttpSession httpSession : httpSessions) {
                    FileOutputStream fos = new FileOutputStream(httpSession.getId() + ".session");
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(httpSession);
                    oos.flush();
                    fos.flush();
                    oos.close();
                    fos.close();
                }
            } else {
                throw new IOException("文件目录错误");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
