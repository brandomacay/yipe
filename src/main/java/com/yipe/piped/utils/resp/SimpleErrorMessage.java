package com.yipe.piped.utils.resp;

import com.yipe.piped.utils.IStatusCode;

public record SimpleErrorMessage(String error) implements IStatusCode {
    @Override
    public int getStatusCode() {
        return 500;
    }
}
