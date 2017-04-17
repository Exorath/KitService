package com.exorath.service.kit.res;

import java.util.List;

/**
 * Created by toonsev on 4/17/2017.
 */
public class GetPackagesRes {
    private List<String> packages;

    public GetPackagesRes() {
    }

    public GetPackagesRes(List<String> packages) {
        this.packages = packages;
    }

    public List<String> getPackages() {
        return packages;
    }
}
