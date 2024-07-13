package com.yipe.piped.utils.resp;

import com.yipe.piped.utils.IStatusCode;

public class IncorrectCredentialsResponse implements IStatusCode {

    public String error = "The username or password you have entered is incorrect.";

    @Override
    public int getStatusCode() {
        return 401;
    }
}
