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

package com.exorath.service.kit.service;

import com.exorath.service.currency.api.CurrencyServiceAPI;
import com.exorath.service.currency.res.IncrementReq;
import com.exorath.service.currency.res.MultiIncrementReq;
import com.exorath.service.kit.Service;
import com.exorath.service.kit.res.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Updates.push;

/**
 * Created by Toon on 4/5/2017.
 */
public class MongoService implements Service {
    private static final Gson GSON = new Gson();
    private CurrencyServiceAPI currencyServiceAPI;
    private MongoCollection<Document> playersCollection;
    private MongoCollection<Document> kitPackagesCollection;

    public MongoService(MongoClient client, String databaseName, CurrencyServiceAPI currencyServiceAPI) {
        this.currencyServiceAPI = currencyServiceAPI;
        MongoDatabase db = client.getDatabase(databaseName);
        this.playersCollection = db.getCollection("players");
        this.kitPackagesCollection = db.getCollection("packages");
    }

    @Override
    public GetPackagesRes getPackages() {
        List<String> packages = new ArrayList<>();
        kitPackagesCollection.find().projection(new Document("_id", 1)).iterator()
                .forEachRemaining(packageDoc -> packages.add(packageDoc.getString("_id")));
        return new GetPackagesRes(packages);
    }

    //Maybe move to pojo mapper/split this method up, for now I'm lazy
    @Override
    public KitPackage getPackage(String packageId) {
        Document packageDoc = kitPackagesCollection.find(new Document("_id", packageId)).first();
        if (packageDoc == null)
            return null;
        HashMap<String, Kit> kits = new HashMap<>();
        if (packageDoc.containsKey("kits")) {
            Document kitsDoc = packageDoc.get("kits", Document.class);
            for (Map.Entry<String, Object> entry : kitsDoc.entrySet()) {
                Document kitDoc = (Document) entry.getValue();
                HashMap<String, Integer> costs = new HashMap<>();
                if (kitDoc.containsKey("costs"))
                    for (Map.Entry<String, Object> costEntry : kitDoc.get("costs", Document.class).entrySet())
                        costs.put(costEntry.getKey(), (Integer) costEntry.getValue());
                Kit kit = new Kit(kitDoc.getString("name"), costs, GSON.fromJson(kitDoc.getString("meta"), JsonObject.class));

                if (kitDoc.containsKey("itemStack"))
                    kit.setItemStack(GSON.fromJson(kitDoc.getString("itemStack"), JsonObject.class));
                kits.put(entry.getKey(), kit);
            }
        }
        return new KitPackage(packageDoc.getString("name"), kits);
    }

    @Override
    public Success updatePackage(String packageId, KitPackage pack) {
        try {
            Document packageDoc = new Document();
            packageDoc.put("name", pack.getName());
            if (pack.getKits() != null && pack.getKits().size() != 0) {
                Document kitsDoc = new Document();
                for (Map.Entry<String, Kit> kitEntry : pack.getKits().entrySet()) {
                    Document kitDoc = new Document("name", kitEntry.getValue().getName());
                    if (kitEntry.getValue().getItemStack() != null)
                        kitDoc.put("itemStack", kitEntry.getValue().getItemStack().toString());
                    if (kitEntry.getValue().getMeta() != null)
                        kitDoc.put("meta", GSON.toJson(kitEntry.getValue().getMeta()));
                    if (kitEntry.getValue().getCosts() != null) {
                        Document costsDoc = new Document();
                        for (Map.Entry<String, Integer> costsEntry : kitEntry.getValue().getCosts().entrySet())
                            costsDoc.put(costsEntry.getKey(), costsEntry.getValue());
                        kitDoc.put("costs", costsDoc);
                    }
                    kitsDoc.put(kitEntry.getKey(), kitDoc);
                }
                packageDoc.put("kits", kitsDoc);
            }
            UpdateResult result = kitPackagesCollection.updateOne(new Document("_id", packageId), new Document("$set", packageDoc), new UpdateOptions().upsert(true));
            return new Success(result.getMatchedCount() > 0);
        } catch (Exception e) {
            e.printStackTrace();
            return new Success(e.getMessage(), -1);
        }
    }

    @Override
    public GetPlayerKitsResponse getKits(String packageId, String uuid) {
        Document document = playersCollection.find(new Document("packageId", packageId).append("uuid", uuid)).first();
        if (document == null || !document.containsKey("kits"))
            return new GetPlayerKitsResponse(new ArrayList<>());
        return new GetPlayerKitsResponse(document.get("kits", List.class));
    }

    //TODO: Integrate and implement this method with the currencyServices
    @Override
    public Success purchaseKit(PurchaseKitReq req) {
        KitPackage kitPackage = getPackage(req.getPackageId());
        if (kitPackage == null)
            return new Success("Package not found", -1);
        Kit kit = kitPackage.getKits().get(req.getKitId());
        if (kit == null)
            return new Success("Kit not found", -1);
        if (ownsKit(req.getPackageId(), req.getUuid(), req.getKitId()).isOwned())
            return new Success("Player already owns this kit", -1);
        MultiIncrementReq multiIncrementReq = new MultiIncrementReq(req.getUuid());
        kit.getCosts().forEach((currency, cost) -> multiIncrementReq.addIncrement(currency, -cost, cost));
        com.exorath.service.currency.res.Success success = currencyServiceAPI.multiIncrement(multiIncrementReq);
        if (!success.isSuccess())
            return new Success(success.getError(), success.getCode());
        if (success.isSuccess()) {
            playersCollection.updateOne(new Document("packageId", req.getPackageId()).append("uuid", req.getPackageId()), push("kits", req.getKitId()), new UpdateOptions().upsert(true));
        }
        return new Success(success.isSuccess(), success.getError(), success.getCode());
    }

    @Override
    public OwnsKitRes ownsKit(String packageId, String uuid, String kitId) {
        return new OwnsKitRes(getKits(packageId, uuid).getKits().contains(kitId));
    }
}
