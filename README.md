## Bank API

### Technologies used

* **Ktor:** Small kotlin web micro-framework.
* **Consul:** For service discovery between the three services.
* **Docker:** All applications are dockerized and composed through `docker-compose`.
* **Spek:** Kotlin testing framework.
* **Kodein:** Ktor doesn't come with a DI solution so I went with Kodein.

### Usage

The simplest way to test the application is to stand up `docker-compose`. 
There is a small script which stands builds all services, calls `docker-compose` and creates some test data.

Simply run:

```
./run.sh
```

> The script expects you to have `docker` installed.

To get some mock data in the system run:

```
./test_data.sh
```

> The script expects you to have `jq` and `curl` installed.

### Structure

* **bank:** Root project.
    * **buildSrc:** Built on start, contains external libs and versions.
    * **lib:** Self contained libraries used in the rest of the application.
        * **config:** Reads the ktor configuration and places it ready to be injected through Kodein. 
        * **consul:** Module to configure consul for each service.
        * **error:** Automated interception of exceptions and conversion into meaningful bodies and HTTP statuses.
        * **identifier:** Generation of unique identifiers (UUID for now). 
        * **service:** Common configuration for all Ktor services.
        * **test:** Utilities to test json, corroutines and ktor applications. 
        * **time:** Time utilities.
    * **account:** The account library.
    * **balance:** The balance library.
    * **transfer:** The transfer library.
    
Account, balance and transfer tend to contain three modules:

* **api:** The publicly API (DTOs and models).
* **client:** The HTTP client implementation to be used by other services.
* **service:** The actual service with the rest endpoints and implementations.
    * **controller:** Definition of the endpoints.
    * **data:** Data layer currently implemented in memory.
    * **domain:** Actual logic and factories.
        
### Services

There are three services that interact through consul. 

To get the IP of each service you can check in consul (typically `localhost:8500`) or in each `application.conf`.

#### Account

Currently it's pretty small since it only contains `accountId` and `currency` management and it's used the transfer service. 

##### Endponts

**`POST /api/v1/accounts`:** Create an account.

Body:
```
{
    "currency":"EUR"
}
```

Response:
```
{
    "id":"abc",
    "currency":"EUR"
}
```

**`GET /api/v1/accounts`:** Get all accounts.

Response:
```
[
    {
        "id":"abc",
        "currency":"EUR"
    }
]
```

**`GET /api/v1/accounts/:id`:** Get a specific account.

Response:
```
{
    "id":"abc",
    "currency":"EUR"
}
```

#### Balance

It doesn't depend on any other service and does very little checks on input. It expects the caller to verify things like `accountId` and currencies since it's meant to be used internally. 

Acts as a _bucket_ of arbitrary money movements agnostic of currency. This is intentional but a less agnostic or more clever service could have also been an option.

##### Endponts

**`GET /api/v1/accounts/:accountId/balances`:** Get all balance movements.

Response:
```
[
    {
        "id":"def",
        "accountId":"abc",
        "amount":"1000",
        "total":"1000",
        "summary":"I give you 10 euros",
        "date":"2007-04-05T12:30-02:00"
    }
]
```

**`POST /api/v1/accounts/:accountId/balances`:** Put a balance movement.

Body:
```
{
    "amount":"1000",
    "summary":"I give you 10 euros"
}
```

Response:
```
{
    "id":"def",
    "accountId":"abc",
    "amount":"1000",
    "total":"1000",
    "summary":"I give you 10 euros",
    "date":"2007-04-05T12:30-02:00"
}
```

**`DELETE /api/v1/accounts/:accountId/balances/:id`:** Revert a balance movement.

Response:
```
{
    "id":"gij",
    "accountId":"abc",
    "amount":"-1000",
    "total":"0",
    "summary":"[REVERT] def I give you 10 euros",
    "date":"2007-04-05T12:30-02:00"
}
```

**`GET /api/v1/accounts/:accountId/balances/:id`:** Get a specific balance movement.

Response:
```
{
    "id":"def",
    "accountId":"abc",
    "amount":"1000",
    "total":"1000",
    "summary":"I give you 10 euros",
    "date":"2007-04-05T12:30-02:00"
}
```


**`GET /api/v1/accounts/:accountId/balances/last`:** Get last / current balance.

Response:
```
{
    "id":"gij",
    "accountId":"abc",
    "amount":"-1000",
    "total":"0",
    "summary":"[REVERT] def I give you 10 euros",
    "date":"2007-04-05T12:30-02:00"
}
```

#### Transfer

It takes care of money transfers between accounts. It uses the balance service as the backing mechanism for money movements.

#### Endpoints

**`POST /api/v1/transfers`:** Create a new transfer.

Body:
```
{
    "fromAccountId":"abc",
    "toAccountId":"def",
    "amount":"123"
}
```

Response:
```
{
    "id":"ghi",
    "fromAccountId":"abc",
    "fromAccountBalanceId":"jkl",
    "toAccountId":"def",
    "toAccountBalanceId":"mno",
    "amount":"123"
    "date":"2007-04-05T12:30-02:00"
}
```

**`GET /api/v1/transfers`:** Gets all transfers.
Response:
```
[
    {
        "id":"ghi",
        "fromAccountId":"abc",
        "fromAccountBalanceId":"jkl",
        "toAccountId":"def",
        "toAccountBalanceId":"mno",
        "amount":"123"
        "date":"2007-04-05T12:30-02:00"
    }
]
```

**`GET /api/v1/transfers/:id`:** Gets a specific transfer.
Response:
```
{
    "id":"ghi",
    "fromAccountId":"abc",
    "fromAccountBalanceId":"jkl",
    "toAccountId":"def",
    "toAccountBalanceId":"mno",
    "amount":"123"
    "date":"2007-04-05T12:30-02:00"
}
```

### API

To get the IP of each service you can check in consul (typically `localhost:8500`) or in each `application.conf`.

### Decisions

* I have focused in the microservice structure and trying things like Consul and Ktor. To limit the scope I've neglected other
important aspects such as authentication and the data layer. 
* The data layer is implemented as in memory but it should be trivial to add other systems by implementing the repository interfaces.
* As I mentioned, the balance service is purposely _dumb_, if requirements change we could add more logic into it. 
* Ktor is great but has very limited support for openapi / swagger which is the reason why I didn't introduce it.
* All services are technically public and I have skipped writing a gateway for this example. 
* For simplicity, all modules are under one project. This is clearly not the way to go for a real life application but the modular structure followed enables breaking it up in the future.
* Some operations would benefit from events (ie: should we delete the balance when we delete an account?). I skipped that part for simplicity.
* Endpoints returning lists should be paged in production, since it would be unrealistic to return all DB.
* Some literal string are hardcoded in english which is not a good strategy for real life application.
* Transfers and balances are currency agnostic (minor units), which means that to display the result to the user the currency should be picked up from the account service. This is not arbitrary. I tried to keep currency complexity as scoped as possible.