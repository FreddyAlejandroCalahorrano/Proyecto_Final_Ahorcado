# Proyecto Final Desarrollo de Aplicaciones Móviles

## Autor

### Freddy Calahorrano 

## Resumen

Esta aplicación nos permite realizar un chat grupal entre usuarios los cuales para poder chatear deben gistrarse a travez de la paguína de login, donde tambien tienen la opcion de iniciar seción.


##  Herramientas

Las herramientas utilizadas para el desarollo de esta aplicación son las sigueintes:

* Node JS.
* IONIC.
* Cordova.
* Angular.
* Firebase.
* Visual Estudio Code.

## Desarrollo

Primero debemos crear el proyecto con las herramientas iniciales a utilizar, esto lo aremos ubicandonos en la carpeta donde estará guardado nuestro Proyecto y ejecutando una Terminal donde agregaremos los siguientes comandos.

```
ionic start "Nombre del proyecto" --cordova --angular
```

Instalaremos las librerias de firebase
```
npm install firebase
```
```
npm i @angular/fire
```
Instalaremos las librerias de chat
```
npm i chat-service
```
Instalaremos las librerias necesarias para la encryptacion de los datos
```
npm i crypto-js
```
Luego crearemos las páginas de Login y Chat
```
ionic generate page login
```
```
ionic generate page chat
```

## Link de video explicativo de la realización del proyecto

https://youtu.be/BXaduVtA3x0

## Link de video explicativo del funcionamiento de la aplicación

https://youtu.be/kzTHu1YpPQ0

## Creacion de la APK

Para finalizar deberemos crear el archivo .APK el cual nos permitira ejecutar la aplicacion en el dispositivo movil android que desiemos.
Es necesario tener integrado cordova, no aseguraremos de esto con el siguiente comando.
```
ionic enable cordova
```
Luego debemos agregar las dependencias de cordova al nuestro proyecto, en este caso para Android.
```
ionic cordova plataform add android
```
Para finalizar deberemos ejecutar el comando que nos permitira construir la aplicación.
```
ionic cordova build android
```

