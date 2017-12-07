package com.ktanx.jdbc.exception;


import com.ktanx.common.enums.IEnum;
import com.ktanx.common.exception.KtanxException;

/**
 * Created by liyd on 17/4/11.
 */
public class KtanxJdbcException extends KtanxException {

    private static final long serialVersionUID = -5833705110587887140L;

    /**
     * Instantiates a new KtanxJdbcException.
     *
     * @param e the e
     */
    public KtanxJdbcException(IEnum e) {
        super(e);
    }

    public KtanxJdbcException(String message, Throwable e) {
        super(message, e);
    }

    public KtanxJdbcException(IEnum msgEnum, Throwable e) {
        super(msgEnum, e);
    }

    /**
     * Instantiates a new KtanxJdbcException.
     *
     * @param e the e
     */
    public KtanxJdbcException(Throwable e) {
        super(e);
    }

    /**
     * Constructor
     *
     * @param message the message
     */
    public KtanxJdbcException(String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param code    the code
     * @param message the message
     */
    public KtanxJdbcException(String code, String message) {
        super(code, message);
    }


}
