# Process Solution Example for Camunda Platform 8 using Kotlin and Spring Boot

This repository contains a Kotlin application example for Camunda Platform 8 based on the [Process Solution Template for Camunda Platform 8](https://github.com/camunda-consulting/camunda-8-poc-template) using Spring Boot
and a [docker-compose.yaml](docker-compose.yaml) file for local development. For production setups we recommend to use the [helm charts](https://docs.camunda.io/docs/self-managed/platform-deployment/kubernetes-helm/).

- [Documentation](https://docs.camunda.io)
- [Camunda Platform SaaS](https://camunda.io)
- [Getting Started Guide](https://github.com/camunda/camunda-platform-get-started)
- [Releases](https://github.com/camunda/camunda-platform/releases)
- [Helm Charts](https://helm.camunda.io/)
- [Zeebe Workflow Engine](https://github.com/camunda/zeebe)
- [Contact](https://docs.camunda.io/contact/)

## Using the Spring Boot application

The application requires a running Zeebe engine.
You can run Zeebe locally using the instructions below for Docker Compose
or have a look at our
[recommended deployment options for Camunda Platform](https://docs.camunda.io/docs/self-managed/platform-deployment/#deployment-recommendation.).

Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Using docker-compose

> :information_source: The docker-compose file in this repository uses the latest [compose specification](https://docs.docker.com/compose/compose-file/), which was introduced with docker-compose version 1.27.0+. Please make sure to use an up-to-date docker-compose version.

> :information_source: The Docker required is 20.10.16+

To stand-up a complete Camunda Platform 8 Self-Managed environment locally the [docker-compose.yaml](docker-compose.yaml) file in this repository can be used.

The full enviornment contains these components:
- Zeebe
- Operate
- Tasklist
- Optimize
- Identity
- Elasticsearch
- KeyCloak

Clone this repo and issue the following command to start your environment:

```
docker-compose up -d
```

Wait a few minutes for the environment to start up and settle down. Monitor the logs, especially the Keycloak container log, to ensure the components have started.

Now you can navigate to the different web apps and log in with the user `demo` and password `demo`:
- Operate: [http://localhost:8081](http://localhost:8081)
- Tasklist: [http://localhost:8082](http://localhost:8082)
- Optimize: [http://localhost:8083](http://localhost:8083)
- Identity: [http://localhost:8084](http://localhost:8084)
- Elasticsearch: [http://localhost:9200](http://localhost:9200)
- KeyCloak: [http://localhost:18080](http://localhost:18080)

The workflow engine Zeebe is available using gRPC at `localhost:26500`.

To tear down the whole environment run the following command

```
docker-compose down -v
```

If Optimize, Identity, and Keycloak are not needed you can use the [docker-compose-core.yaml](docker-compose-core.yaml) instead which does not include these components:

```
docker-compose -f docker-compose-core.yaml up -d
```

Zeebe, Operate, Tasklist, along with Optimize require a separate network from Identity as you'll see in the docker-compose file.

In addition to the local environment setup with docker-compose, you can download the [Camunda Desktop Modeler](https://camunda.com/download/modeler/) to locally model BPMN diagrams for execution and directly deploy them to your local environment.
