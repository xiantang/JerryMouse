package info.xiantang.jerrymouse.user;

public class User {
    private final String name;
    private final String pwd;

    public User(String name, String pwd) {

        this.name = name;
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public String getPwd() {
        return pwd;
    }
}
