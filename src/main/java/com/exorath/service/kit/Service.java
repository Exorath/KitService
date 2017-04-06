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

import com.exorath.service.kit.res.Kit;
import com.exorath.service.kit.res.KitPackage;
import com.exorath.service.kit.res.PurchaseKitReq;
import com.exorath.service.kit.res.Success;

import java.util.List;

/**
 * Created by Toon on 4/4/2017.
 */
public interface Service {

    /**
     * Gets a list of all packages registered to this service
     * @return  a list of all packages registered to this service
     */
    List<String> getPackages();

    /**
     * gets all kits of the specified package, and the name
     * @param packageId the package identifier
     * @return information about the kit package
     */
    KitPackage getPackage(String packageId);

    /**
     * Uploads a package to the datastore, or updates it if it already exists
     * @param packageId the identifier
     * @param pack the package contents
     * @return whether or not this upload was successful
     */
    Success updatePackage(String packageId, KitPackage pack);

    /**
     * Gets a list of all kit id's the player owns
     * @param packageId the package identifier
     * @param uuid player uuid
     * @return a list of all kit id's the player owns
     */
    List<String> getKits(String packageId, String uuid);


    /**
     * Attempts to purchase a kit for the player, given this player has sufficient currency.
     * @param req the request parameters
     * @return whether or not this was successful, see readme.md for info about error codes.
     */
    Success purchaseKit(PurchaseKitReq req);

    /**
     * checks whether or not the specified player owns the specified kit
     * @param packageId the package id
     * @param uuid the player id
     * @param kitId the kit id
     * @return whether or not the player owns this kit
     */
    boolean ownsKit(String packageId, String uuid, String kitId);
}
