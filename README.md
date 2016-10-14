# Schema Repo - DynamoDB Backend

A backend adapter for [schema-repo] that writes data 
to AWS DynamoDB.
 
## Requirements

 * maven 3
 * java 7
 
## Setup

### Configuration

```
# FQCN of the Dynamo-based backend:
schema-repo.class=io.fineo.schemarepo.dynamo.DynamoDBRepository

schema-repo.dynamo.region_or_url=us-east-1

schema-repo.dynamo.table.name=tablename
# If we should attempt to create the table on startup
schema-repo.dynamo.table.create=[true|false]

# Select one of the below to specify credentials
schema-repo.dynamo.credentials.type=[default|profile|static]
schema-repo.dynamo.credentials.profile.name=(profile name)
schema-repo.dynamo.credentials.static.key=(AWS ACCESS KEY)
schema-repo.dynamo.credentials.static.secret=(AWS SECRETY KEY)

```

Unfortunately, all of the above must be specified when starting the service. This is because we 
are trying to keep guice (how the RepositoryServer loads classes) out of this implementation.

Fields in `()` are optional, but their key must be specified.
Files in `[]` require a value, but can be one of the `|` separated choices.

See [schema-repo] for standard configuration options.


### Running

```
$ java -cp dynamo-bundle-[version]-withdeps.jar org.schemarepo.server.RepositoryServer [config file.properties]
```

Basically, this is the code from [schema-repo]'s ```run.sh``` file, but pointing at the dynamodb 
jar you want.

[schema-repo]: https://github.com/schema-repo/schema-repo

## Building

```
$ mvn clean install
```
