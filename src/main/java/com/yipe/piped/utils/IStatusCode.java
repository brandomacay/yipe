package com.yipe.piped.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface IStatusCode {

    @JsonIgnore
    public int getStatusCode();

}
