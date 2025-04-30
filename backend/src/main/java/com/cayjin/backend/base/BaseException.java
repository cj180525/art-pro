package com.cayjin.backend.base;

import com.cayjin.backend.common.ResponseCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * 自定义异常
 *
 * @author cj180
 * @date 2025年3月31日
 */
@Setter
@Getter
public class BaseException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 1L;
	
    private String msg;
    private int code = ResponseCode.UNKNOWN_EXCEPTION.getCode();
    
    public BaseException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public BaseException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}

	public BaseException(ResponseCode code) {
		super(code.getMsg());
		this.msg = code.getMsg();
		this.code = code.getCode();
	}
	
	public BaseException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public BaseException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}


}
