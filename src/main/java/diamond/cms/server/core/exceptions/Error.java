package diamond.cms.server.core.exceptions;

public enum Error {
    UNKNOW_EXCEPTION(0, "unknow exception"),
    UN_AUTHORIZATION(10001, "un authorization"),
    PARAMS_ERROR(10002, "parasm valid %s"),
    INVALID_TOKEN(10003, "invaild token"),
    USERNAME_OR_PASSWORD_ERROR(10004, "username or password error"),
    PAGE_NO_FOUND(10005),
    SINGLE_USER_ERROR(10006, "has user, can't add more"),
    USER_ACCESS_NOT_INIT(10007, "user access not init"),
    NO_ENABLE_EMAIL_CONFIG(10008, "no enable email-config"),
    SEND_EMAIL_FAILD(10009, "send email faild: %s"),
    USER_INIT_API_REFUSE(10010, "this api user init use only"),
    INVALID_EMAIL_CODE(10011, "invalid email code"),
    INVALID_PARAMS(10012, "invalid params: %s"), 
    AUTH2_CODE_ERROR(10013, "get token failed auth2 code error message: %s"),
    ;

    private int code;
    private String msg;

    Error(int code) {
        this.code = code;
        this.msg = this.name();
    }

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
