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

package com.exorath.service.kit.res;

import com.google.gson.JsonObject;

import java.util.HashMap;

/**
 * Created by Toon on 4/4/2017.
 */
public class Kit {
    private String name;
    private HashMap<String, Integer> costs;
    private JsonObject meta;
    private JsonObject itemStack;


    public Kit(String name, HashMap<String, Integer> costs, JsonObject meta) {
        this.name = name;
        this.costs = costs;
        this.meta = meta;
    }

    public JsonObject getItemStack() {
        return itemStack;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Integer> getCosts() {
        return costs;
    }

    public JsonObject getMeta() {
        return meta;
    }
}
