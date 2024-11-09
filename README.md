# Sales service assignment

This project is a Java application that cover sales service assignment

## Requirements

### Local application setup

- Java 21
- Maven 3.9.2+

If you are using Intellij, you will find a run configuration called `run-local`, which you can use
to run the application without the database attached to it.

### Docker application setup

- Docker
- Docker-compose

If you are using docker, all you have to do is run `mvn clean package` and
then `docker-compose up -d --build`.

### Initial user creation

For the first admin user, you have to execute the POST endpoint located at `/api/v1/user/initial-user`. It has no authentication, and it will create the first user with the role `ADMIN`.
