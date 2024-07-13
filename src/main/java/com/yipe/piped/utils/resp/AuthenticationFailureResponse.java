package com.yipe.piped.utils.resp;

import com.yipe.piped.utils.IStatusCode;

public class AuthenticationFailureResponse implements IStatusCode {

    public String error = "An invalid Session ID was provided.";

    @Override
    public int getStatusCode() {
        return 401;
    }
}
