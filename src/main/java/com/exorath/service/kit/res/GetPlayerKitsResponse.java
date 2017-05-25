package com.exorath.service.kit.res;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toonsev on 4/17/2017.
 */
public class GetPlayerKitsResponse {
    private List<String> kits;

    public GetPlayerKitsResponse() {
    }

    public GetPlayerKitsResponse(List<String> kits) {
        this.kits = kits;
    }

    public List<String> getKits() {
        return kits == null ? new ArrayList<>() : kits;
    }
}
