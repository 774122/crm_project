package com.chen.exception.Login;

import com.chen.exception.SystemException;

/**
 * @author chenhongchang
 * @date 2021/5/7 0007 - 下午 6:16
 */
public class LoginException extends SystemException {
    public LoginException() {
        super();
    }

    public LoginException(String message) {
        super(message);
    }
}
