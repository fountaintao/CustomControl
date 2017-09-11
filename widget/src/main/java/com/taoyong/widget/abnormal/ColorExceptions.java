package com.taoyong.widget.abnormal;

/**
 * Creation Time :  2017/9/12.<br/>
 * Creator : tao yong.<br/>
 * Module description : Color abnormal<br/>
 */
public class ColorExceptions extends RuntimeException {
    public ColorExceptions() {
        super("Color conversion exception, need to be invoked {@link getResources().getColor()}.");
    }

    public ColorExceptions(String message) {
        super(message);
    }

    public ColorExceptions(String message, Throwable cause) {
        super(message, cause);
    }
}
