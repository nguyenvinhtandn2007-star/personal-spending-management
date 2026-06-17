package network;

import java.io.Serializable;

public class Request implements Serializable {
    private String action;
    private Object data;
    private String token;

    public Request() {
    }

    public Request(String action, Object data) {
        this.action = action;
        this.data = data;
    }

    public Request(String action, Object data, String token) {
        this.action = action;
        this.data = data;
        this.token = token;
    }

    public String getAction() {
        return action;
    }

    public Object getData() {
        return data;
    }

    public String getToken() {
        return token;
    }
}
