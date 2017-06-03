/*
 *    Copyright 2017 Exorath
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.exorath.service.kit;

import com.exorath.service.commons.portProvider.PortProvider;
import com.exorath.service.kit.res.KitPackage;
import com.exorath.service.kit.res.PurchaseKitReq;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import spark.Route;

import static spark.Spark.port;
import static spark.Spark.put;

import static spark.Spark.get;

import static spark.Spark.post;

/**
 * Handles HTTP transport to and from the service
 * Created by Toon on 4/4/2017.
 */
public class Transport {
    private static final Gson GSON = new Gson();

    public static void setup(Service service, PortProvider portProvider) {
        port(portProvider.getPort());

        get("/packages", getGetPackagesRoute(service), GSON::toJson);
        get("/packages/:packageId", getGetPackageRoute(service), GSON::toJson);
        put("/packages/:packageId", getPutPackageRoute(service), GSON::toJson);
        get("/packages/:packageId/player/:uuid/kits", getGetPlayerKitsRoute(service), GSON::toJson);
        post("/packages/:packageId/player/:uuid/kits/:kitId", getPurchaseKitRoute(service), GSON::toJson);
        get("/packages/:packageId/player/:uuid/kit/:kitId", getOwnsKitRoute(service), GSON::toJson);

        get("/packages/:packageId/player/:uuid/current", getGetCurrentKitRoute(service), GSON::toJson);
        put("/packages/:packageId/player/:uuid/current/:kitId", getPutCurrentKitRoute(service), GSON::toJson);
    }

    private static Route getGetCurrentKitRoute(Service service) {
        return (req, res) -> service.getCurrentKit(req.params("packageId"), req.params("uuid"));
    }
    private static Route getPutCurrentKitRoute(Service service) {
        return (req, res) -> service.setCurrentKit(req.params("packageId"), req.params("uuid"), req.params("kitId"));
    }
    private static Route getOwnsKitRoute(Service service) {
        return (req, res) -> service.ownsKit(req.params("packageId"), req.params("uuid"), req.params("kitId"));
    }

    private static Route getPurchaseKitRoute(Service service) {
        return (req, res) -> service.purchaseKit(new PurchaseKitReq(req.params("packageId"), req.params("uuid"), req.params("kitId")));
    }

    private static Route getGetPlayerKitsRoute(Service service) {
        return (req, res) -> service.getKits(req.params("packageId"), req.params("uuid"));
    }

    private static Route getPutPackageRoute(Service service) {
        return (req, res) -> {
            KitPackage kitPackage = GSON.fromJson(req.body(), KitPackage.class);
            return service.updatePackage(req.params("packageId"), kitPackage);
        };
    }

    private static Route getGetPackageRoute(Service service) {
        return (req, res) -> service.getPackage(req.params("packageId"));
    }

    private static Route getGetPackagesRoute(Service service) {
        return (req, res) -> service.getPackages();
    }

}
