package diamond.cms.server.exceptions;

public enum Error {
    UNKNOW_EXCEPTION(0, "unknow exception"),
    UN_AUTHORIZATION(10001, "un authorization"),
    PARAMS_ERROR(10002, "parasm valid %s"),
    INVALID_TOKEN(10003, "invaild token"),
    USERNAME_OR_PASSWORD_ERROR(10004, "username or password error")
    ;

    private int code;
    private String msg;

    Error(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }

}
