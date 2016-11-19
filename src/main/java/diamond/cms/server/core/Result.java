package diamond.cms.server.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class Result {
	private boolean success;
	private String msg;
	private int code;
	private Object data;

	public Result() {

	}
	public Result(String string) {
	    this.data = string;
	    this.success = true;
    }
    public Result(boolean success) {
        this.success = success;
    }
    public Result(Object data) {
        this.success = true;
        this.data = data;
    }
    public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
