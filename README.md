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

