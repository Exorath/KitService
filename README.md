# KitService
The KitService keeps track of all game kits the player owns, and integrates with the CurrencyService. It is backed by MongoDB.

### Endpoints
#### /packages [GET] Gets all packages

**Response**
```json
{
"packages" : ["ctf", "gw"]
}
```

#### /packages/{packageId} [GET] - Gets all kits and other information of the package

**Response**
```json
{
"name": "Capture The Flag kits",
"kits": {
  "warrior": {
      "name": "Warrior Kit",
      "itemStack": {
          "material": "IRON_SWORD"
      },
      "costs": {
        "gold": 10
      },
      "meta": {
        "items" : {}
      }
    }
  }
}
```

#### /packages/{packageId} [PUT] - Updates package information

**Request**
```json
{
"name": "Capture The Flag kits",
"kits": {
  "warrior": {
      "name": "Warrior Kit",
      "itemStack": {
          "material": "IRON_SWORD"
      },
      "costs": {
        "gold": 10
      },
      "meta": {
        "items" : {}
      }
    }
  }
}
```

**Response**
```json
{
"success": true
}
```

#### /packages/{packageId}/player/{uuid}/kits [GET] - Gets the kits the player owns

**Response**
```json
{
"kits": ["warrior"]
}
```

#### /packages/{packageId}/player/{uuid}/kits/{kitId} [POST] -Purchases the said kit

**Response**

error codes:
1: Unnknown
2: Already purchased
3: Insufficient funds
4: Kit does not exist
```json
{
"success": false,
"error": "Already purchased",
"code": 2
}
```

#### /packages/{packageId}/player/{uuid}/kits/{kitId} [GET] - Returns whether or not the player owns this kit
```json
{
"owned": true
}
```

#### /packages/{packageId}/player/{uuid}/current [GET] - Gets the current kit
```json
{
"kit": "warrior"
}
```
the response will be {} if no kit is selected

#### /packages/{packageId}/player/{uuid}/current/{kitId} [PUT] - Sets the current kit
Code -2 means the player does not own the kit

```json
{
"success": "true"
}
```

## Environment
| Name | Value |
| --------- | --- |
| MONGO_URI | {mongo_uri} |
| DB_NAME | {db name to store data} |
| CURRENCY_SERVICE_ADDRESS | {ip:port} |