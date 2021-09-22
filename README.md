# portier-server

The server software driving the backend of Discord Portier.

## Compilation

To compile the software, you need to have a Java JDK 11 (or newer) and run `./gradlew build`.

For the tests to pass, you also need to set up a Postgres instance. A functioning example is provided in the
[`docker-compose.dev.yaml`](./docker-compose.dev.yaml) file: `docker-compose -f docker-compose.dev.yaml up -d`.

## Contribution

You are free to claim any issue and contribute to the project, so long as you release any change under
the [Mozilla Public Licence 2.0](./LICENCE).

## API

The API is provided in Websocket and REST form. To access the API, you will need a user account with read permissions.
It also requires authentication with HTTP Basic authentication with said user, and you are (often) limited only to your
own user's scope.

The websocket API can be accessed over `/v1/ws` with a websocket client; it too must be authenticated properly. The REST
API can be accessed over `/v1`.

### Websocket (v1)

The websocket API is simply for subscriptions. If you attempt to write to the API, it will disconnect you accordingly:

```json
> hello, world!
< {"type": "error", "code": "WEB_SOCKET_CANNOT_RECEIVE"}
Disconnected (code: 1003, reason: "")
```

All websocket responses are tagged properly with a `type`.

### REST (v1)

The REST API is for fetching and modifying data that is not done in real-time. If you attempt to exceed your
permissions, you will be told off accordingly.
