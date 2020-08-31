package com.grouppage.exception;

import sun.rmi.runtime.RuntimeUtil;

public class ThisGroupDoesntIncludeForm extends RuntimeException {
    public ThisGroupDoesntIncludeForm(String message) {
        super(message);
    }
}
