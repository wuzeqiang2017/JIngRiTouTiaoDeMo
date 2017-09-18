package test.bwie.com.wzq_0831jinritoutiao.bean;

/**
 * 移动1507D  武泽强
 * 2017/9/6.
 * 作用：
 */

public class User {
    private String title;
    private String context;

    public User() {
    }

    public User(String title, String context) {
        this.title = title;
        this.context = context;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

}
