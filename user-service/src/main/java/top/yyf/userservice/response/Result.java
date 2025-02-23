// 在user-service中创建response包下的Result.java
package top.yyf.userservice.response;

public class Result {
    private String username;
    private String answer;

    // 构造方法、Getter和Setter
    public Result(String username, String answer) {
        this.username = username;
        this.answer = answer;
    }

    // Lombok可简化以下代码
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
}