package com.yipe.piped.utils.resp;

import com.yipe.piped.utils.IStatusCode;

public class DisabledRegistrationResponse implements IStatusCode {

    public String error = "This instance has registrations disabled.";

    @Override
    public int getStatusCode() {
        return 400;
    }
}
