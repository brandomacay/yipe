package com.yipe.piped.utils;


import com.yipe.piped.consts.Constants;

public class RydHelper {
    public static double getDislikeRating(String videoId) throws Exception {

        if (Constants.DISABLE_RYD)
            return -1;

        return RequestUtils.sendGetJson(Constants.RYD_PROXY_URL + "/votes/" + videoId)
                .thenApply(tree -> tree.path("rating").asDouble(-1))
                .get();

    }
}
