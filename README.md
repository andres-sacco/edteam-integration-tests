# Edteam - Curso: Pruebas de integración con Java

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Este repositorio contiene todo lo necesario para poder seguir el curso desde el inicio hasta el final.

## Tabla de contenidos

Los suguientes son los topicos o puntos mas relevantes de este archivo:
- [Requerimientos](#Requerimientos)
- [Comprobar requerimientos](#Comprobar-requerimientos)
- [Arquitectura](#Arquitectura)
  - [Microservicios](#Microservicios)
  - [Capas de la aplicacion](#Capas-de-la-aplicacion)
- [Documentacion de los endpoints](#Documentacion-de-los-endpoints)
- [Consideraciones](#Consideraciones)
- [Preguntas frecuentes](#Preguntas-frecuentes)

## Requerimientos

Para poder utilizar el código de este proyecto deberás tener las siguientes herramientas instaladas:

- [Java](https://www.oracle.com/ar/java/technologies/downloads/)
- [Maven](https://maven.apache.org/)
- [Git](https://git-scm.com/)
- [Docker](https://www.docker.com/)

Si no tienes algunas de estas herramientas instaladas en tu computadora, sigue las instrucciones en la documentación oficial de cada herramienta.

## Comprobar requerimientos


Si instaló en su computadora algunas de estas herramientas anteriormente o instalaste todas las herramientas ahora, verifica si todo funciona bien.

- Para comprobar que version de Java tenes en tu computadora podes usar este comando:
   ````
   % java -version
  openjdk 17.0.6 2023-01-17 LTS
  OpenJDK Runtime Environment Microsoft-7209853 (build 17.0.6+10-LTS)
  OpenJDK 64-Bit Server VM Microsoft-7209853 (build 17.0.6+10-LTS, mixed mode, sharing)
   ````

- Comprueba si la version de Maven es 3.8.0 o superior. Puedes comprobar la version de Maven con el siguiente comando:
   ````
   % mvn --version
   Apache Maven 3.8.3
   Maven home: /usr/share/maven
   ````

- Comprueba si la version de Docker que se encuentra en tu computadora es 18.09.0 o superior. Puedes comprobar la version de Docker con el siguiente comando:
   ````
   % docker --version
  Docker version 24.0.2, build cb74dfc
   ````

## Arquitectura

Los microservicios son una abstracción de todo el flujo para crear reservas de una agencia que vende vuelos.

#### Microservicios

El sistema se compone de los siguientes microservicios:
* **api-catalog** este microservicio contiene toda la informacion relacionada con un catalogo de ciudades.


* **api-reservations** este microservicio contiene toda la informacion de las reservas de una empresa que vende pasajes aereos.

### Capas de la aplicacion

Todos los microservicios se dividen en diferentes capas que solo tienen acceso a otras capas. La siguiente figura muestra la estructura común en todos los microservicios:

![Layers](.images/Microservices-Layers.png)

Ahora, un poco sobre lo que contiene cada capa:

| Capa          | Descripcion                                                                                                                             | Paquetes                                             |
|---------------|-----------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------|
| Controllers   | En esta capa encontraras toda la lógica para realizar las distintas acciones.                                                           | *.controller and *.controller.documentation          |
| Services      | Contiene toda la definición de los servicios y la implementación.                                                                       | *.service and *.service.impl                         |
| Validators    | Contiene toda la lógica para validar una Solicitud de un DTO.                                                                           | *.validator                                          |
| Repositories  | Esta capa contiene la definición usando interfaces y en algunos casos contiene la especificación para realizar una consulta particular. | *.repository, *.repository.impl, and *.specification |
| Connectors    | Dentro de esta capa se encuentran todas las configuraciones y los endpoints para comunicarse con servicios externos.                    | *.connector and *.connector.configuration            |
| Helpers       | Todas las clases que ayudan en diferentes cosas en todos los microservicios (por ejemplo, calcular la duración de un vuelo).            | *.helper                                             |
| Configuration | Toda la lógica para configurar diferentes aspectos de los microservicios (por ejemplo, formato de respuesta, puertos).                  | *.configuration                                      |
| Exceptions    | Contiene todas las excepciones que cada microservicio puede generar durante la ejecución de una solicitud.                              | *.exception                                          |
| Model         | Esta capa particular contiene todas las entidades que acceden a las bases de datos.                                                     | *.model                                              |
| Enums/DTO     | En esta capa puede encontrar las clases y la enumeración que se utilizan en las diferentes capas.                                       | *.enums and *.dto                                    |


## Documentacion de los endpoints

Cada API tiene documentación para comprender qué parámetros son necesarios y la URL para invocarlos. Para ver la documentación de la API es necesario ejecutar cada proyecto y acceder a:
- [Swagger - Reservation](http://localhost:8080/api/flights/reservations/documentation)
- [Swagger - Catalog](http://localhost:6070/api/flights/catalog/documentation)

Opcionalmente puedes usar [**Postman**](https://www.postman.com/) para realizar request desde los diferentes microservicios, el repositorio incluye un archivo con una coleccion con todos los endpoints.

## Consideraciones

Para ejecutar todos los microservicios en la misma máquina, debes considerar que los siguientes puertos deben estar disponibles para usarlo:

| Name             | Application | Database    |
|------------------|-------------| ----------- |
| api-reservations | 8080        | 3312        |
| api-catalog      | 6070        | 3310        |

## Preguntas frecuentes

**¿Qué versión del JDK puedo usar en este proyecto?**

No hay restricción sobre qué versión en particular debes considerar, ya que existen diferentes alternativas al JDK:

* **OracleJDK**: Esta versión era gratuita hasta Java 11, después de esta versión puedes usarla para entornos de desarrollo/prueba, pero debes pagar una licencia para usarla en producción. Esta versión del JDK le ofrece los parches de errores más recientes y nuevas funciones porque Oracle es el propietario del lenguaje.


* **OpenJDK**: Cuando Oracle compró Sun Microsystems, creó esto como una alternativa de código abierto que todos los desarrolladores pueden usar en cualquier entorno sin restricciones. El principal problema de esta versión es que los parches de los errores tardan en aparecer en los casos que no son críticos.

Tenga en cuenta que existen otras alternativas, pero según el [Informe Snyk 2021](https://res.cloudinary.com/snyk/image/upload/v1623860216/reports/jvm-ecosystem-report-2021.pdf), la mayoría de los desarrolladores utilizan OpenJDK.

**¿Qué herramientas puedo utilizar para el desarrollo?**

Hay un sin fin de herramientas para el desarrollo, quizas las dos mas importantes son:
- [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/) – IntelliJ es el IDE más utilizado para el desarrollo.

- [Eclipse](https://www.eclipse.org/downloads/) – Eclipse es otra opción IDE para el desarrollo. La mayoría de los complementos son gratuitos y cuentan con una amplia comunidad de desarrolladores que los actualizan con frecuencia.

Tenga en cuenta que existen otros IDE, pero según el [Informe Snyk 2021](https://res.cloudinary.com/snyk/image/upload/v1623860216/reports/jvm-ecosystem-report-2021.pdf), la mayoría de Los desarrolladores de JVM utilizan Eclipse e Intellij, pero el uso del código de Visual Studio está creciendo en el último año.