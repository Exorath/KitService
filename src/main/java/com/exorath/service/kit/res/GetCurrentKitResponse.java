package com.exorath.service.kit.res;

/**
 * Created by toonsev on 6/3/2017.
 */
public class GetCurrentKitResponse {
    String kit;

    public GetCurrentKitResponse() {}

    public GetCurrentKitResponse(String kit) {
        this.kit = kit;
    }

    public String getKit() {
        return kit;
    }
}
