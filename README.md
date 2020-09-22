# Creación de una API REST con Java Spring

### Creamos la API y la desplegamos en Heroku

#### Para esto utilicé las siguientes dependencias:

- Spring Boot Data JPA (Crud con la Base de Datos)
- Spring Boot Security (Seguridad con Login)
- JsonWebToken (Crear Token)
- MapStruct (Mappear Objetos)
- Swagger (Documentar API)
- PostgreSQL (Base de Datos)

**La documentación de la API la hice usando [Swagger](https://swagger.io/ "Swagger") y la pueden ver entrando [aquí](https://apimarket.herokuapp.com/market/api/swagger-ui.html#/ "aquí"). Esta es la versión de la API con JWT, es decir, necesitamos ingresar un usuario y una contraseña para generar el TOKEN y poder usar la API.**

**El formato para autenticarse con el usuario y la contraseña es el siguiente:**

    {
      "password": "market123",
      "username": "marketuser"
    }

Por otro lado, también hice una API REST sin autenticación para que cualquiera pueda usarla sin problemas en caso de que necesite una API para desarrollo. Esta API sin autenticación la pueden ver **[AQUÍ](https://apimarket-sintoken.herokuapp.com/market/api/products/all "AQUí")** y tiene el mismo formato que la API con autenticación, entonces se pueden guiar con la misma documentación de [Swagger](https://swagger.io/ "Swagger").
