package com.exorath.service.kit.api;


import com.exorath.service.kit.Service;
import com.exorath.service.kit.res.KitPackage;
import com.exorath.service.kit.res.PurchaseKitReq;
import com.exorath.service.kit.res.Success;

import java.util.List;

/**
 * Created by toonsev on 4/16/2017.
 */
public class KitServiceAPI implements Service {
    @Override
    public List<String> getPackages() {
        return null;
    }

    @Override
    public KitPackage getPackage(String packageId) {
        return null;
    }

    @Override
    public Success updatePackage(String packageId, KitPackage pack) {
        return null;
    }

    @Override
    public List<String> getKits(String packageId, String uuid) {
        return null;
    }

    @Override
    public Success purchaseKit(PurchaseKitReq req) {
        return null;
    }

    @Override
    public boolean ownsKit(String packageId, String uuid, String kitId) {
        return false;
    }
}
