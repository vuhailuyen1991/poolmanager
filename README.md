# Usage

A simple PoolManager REST API application written in scala and based on Play framework.

A pool basically contains two properties: a unique `poolId` and an array of integers `poolValues`

This PoolManager application exposes three public APIs as following:

1. Get Pool by `poolId`
    - uri: `/api/pools/:poolId`
    - method: GET
    - parameters: `:poolId` of type integer
    - responses:
      - 200 if pool exists. Response  body is a json array of `poolValues` e.g. `[1, 2, 3, 4]`
      - 404 if pool does not exist
      - 400 if request is malformed i.e. `poolId` is not an integer
2. Create Pool
   - uri: `/api/pools`
   - method: POST
   - body of type json:
   ```json
   {
      "poolId": <integer>,
      "poolValues": <[integers]>
   }
   ```
   - note: 
     - if `poolId` already exists, `poolValues` in the request will be appended to the existing poolValues.
     - if `poolValues` has more than 10 values then the request is considered malformed.
   - responses:
      - 201 if pool is created 
      - 204 if pool is updated
      - 400 if request is malformed i.e. `poolValues` contains values of type other than integer
3. Calculate Pool's percentile
   - uri: `/api/pools/percentile`
   - method: POST
   - body of type json:
   ```json
   {
      "poolId": <integer>,
      "percentile": <float>
   }
   ```
   `percentile` must be in range [1:100]
    - responses:
        - 200 if pool exists. Response  body is a json object contains the calculated value and the size of `poolValues`
         e.g. ``` {"count": 4, "value": 2 }```
        - 404 if pool does not exist
        - 400 if request is malformed i.e. `poolId` or `percentile` is not a number

# Installation
- Install Java 8 and sbt. 
- Navigate to `poolmanager` folder and execute `sbt run`
- PoolManager can be accessible at `localhost:9000`
