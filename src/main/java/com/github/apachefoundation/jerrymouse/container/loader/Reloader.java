package com.github.apachefoundation.jerrymouse.container.loader;

/**
 * @Author: xiantang
 * @Date: 2019/6/24 21:43
 */
public interface Reloader {
    public void addRepository(String repository);

    public String[] findRepository();

    public boolean modified();

    public void setReloadable(boolean reloadable);

    public boolean getReloadable();

    public void addPropertyChangeListener();

    public String[] findRepositories();


}
