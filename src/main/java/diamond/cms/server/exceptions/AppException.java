package diamond.cms.server.exceptions;

public class AppException extends RuntimeException{

	/**
     *
     */
    private static final long serialVersionUID = 2404372373182554123L;
    private int code;
	private String msg;

	public AppException() {
	    this(Error.UNKNOW_EXCEPTION);
	}

	public AppException(Error code) {
		code = code == null ? Error.UNKNOW_EXCEPTION : code;
		this.code = code.getCode();
		this.msg = code.getMsg();
	}

	public AppException(Error code, String exMsg) {
		this(code);
		this.msg = String.format(code.getMsg(), exMsg);
	}

	@Override
	public String getMessage() {
		return msg;
	}

	public int getCode() {
		return code;
	}

}
