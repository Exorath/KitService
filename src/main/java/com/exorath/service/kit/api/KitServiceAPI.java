package com.exorath.service.kit.api;


import com.exorath.service.kit.Service;
import com.exorath.service.kit.res.*;
import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;

import java.util.List;

/**
 * Created by toonsev on 4/16/2017.
 */
public class KitServiceAPI implements Service {
    private static final Gson GSON = new Gson();
    private String address;

    public KitServiceAPI(String address) {
        this.address = address;
    }

    @Override
    public GetPackagesRes getPackages() {
        try {
            return GSON.fromJson(Unirest.get("/packages").asString().getBody(), GetPackagesRes.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public KitPackage getPackage(String packageId) {
        try {
            return GSON.fromJson(Unirest.get("/packages/{packageId}").routeParam("packageId", packageId).asString().getBody(), KitPackage.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Success updatePackage(String packageId, KitPackage pack) {
        try {
            return GSON.fromJson(Unirest.put("/packages/{packageId}").routeParam("packageId", packageId).body(GSON.toJson(pack)).asString().getBody(), Success.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new Success(e.getMessage(), -1);
        }
    }

    @Override
    public GetPlayerKitsResponse getKits(String packageId, String uuid) {
        try {
            return GSON.fromJson(Unirest.put("/packages/{packageId}/player/{uuid}/kits")
                    .routeParam("packageId", packageId)
                    .routeParam("uuid", uuid)
                    .asString().getBody(), GetPlayerKitsResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Success purchaseKit(PurchaseKitReq req) {
        try {
            return GSON.fromJson(Unirest.post("/packages/{packageId}/player/{uuid}/kits/{kitId}")
                    .routeParam("packageId", req.getPackageId())
                    .routeParam("uuid", req.getUuid())
                    .routeParam("kitId", req.getKitId())
                    .asString().getBody(), Success.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new Success(e.getMessage(), -1);
        }
    }

    @Override
    public OwnsKitRes ownsKit(String packageId, String uuid, String kitId) {
        try {
            return GSON.fromJson(Unirest.post("/packages/{packageId}/player/{uuid}/kits/{kitId}")
                    .routeParam("packageId", packageId)
                    .routeParam("uuid", uuid)
                    .routeParam("kitId", kitId)
                    .asString().getBody(), OwnsKitRes.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
