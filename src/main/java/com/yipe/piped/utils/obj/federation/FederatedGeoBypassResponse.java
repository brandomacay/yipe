package com.yipe.piped.utils.obj.federation;

import com.yipe.piped.consts.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.yipe.piped.utils.obj.Streams;

@NoArgsConstructor
@Getter
public class FederatedGeoBypassResponse {
    private String videoId;
    private String country;
    private String videoProxyUrl;
    private Streams data;

    public FederatedGeoBypassResponse(String videoId, String country, Streams data) {
        this.videoId = videoId;
        this.country = country;
        this.data = data;
        this.videoProxyUrl = Constants.PROXY_PART;
    }
}
