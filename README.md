# Parcial 1 - Grupo XX

Proyecto de Algoritmos y Estructuras de Datos II (UADE). Contiene los TPs 01, 02 y 03
sobre una misma aplicación de consola.

## Cómo abrirlo en IntelliJ IDEA

1. Descomprimir el zip.
2. (Opcional) Renombrar la carpeta con tu número de grupo. Es seguro hacerlo
   antes de abrirla; la configuración no depende del nombre de la carpeta.
3. En IntelliJ: `File > Open...` y seleccionar **la carpeta del proyecto**
   (no un archivo suelto). IntelliJ detecta la carpeta `.idea` y lo abre listo.
4. Si aparece el aviso *"Project SDK is not defined"*, hacer clic en `Setup SDK`
   y elegir un JDK 17 o superior. Si no tenés ninguno instalado, en el mismo
   diálogo se puede descargar (`Download JDK`).

Para ejecutar: el desplegable de arriba a la derecha ya trae la configuración
**MainProgram**. Botón verde o `Shift + F10`.

## Estructura

```
src/
  application/          Infraestructura reutilizable (TP 01)
    MainProgram.java      Punto de entrada, menú principal, dueño del Scanner
    Exercise.java         Clase base abstracta de todos los ejercicios
    TestExercise.java     Ejercicio de prueba

  listModule/           TDA List (TP 02 y 03)
    ListExercise.java     TP 02: prueba de java.util.List
    MyList.java           TP 03: especificación del TDA (interfaz)
    MyArrayList.java      TP 03: implementación sobre arreglo dinámico

  playlistModule/       Aplicación de playlist (TP 03)
    Cancion.java          Modelo de una canción
    Playlist.java         TDA y estado de reproducción
    PlaylistExercise.java Capa de consola integrada al menú principal

docs/                   Guías explicativas de cada TP
```

## Menú

| Opción | Contenido |
|---|---|
| 1 | TP 01 — Ejercicio de prueba (herencia y clase abstracta) |
| 2 | TP 02 — TDA List de Java (ArrayList / LinkedList) |
| 3 | TP 03 — Playlist musical |

## Notas

- Para cambiar la implementación usada en el TP 02, editar una línea del constructor
  de `ListExercise` (`new ArrayList<>()` o `new LinkedList<>()`).
- El TP 03 usa las clases `Cancion` y `Playlist`, y permite agregar, remover por
  nombre o número, reproducir, detener y navegar entre canciones.
- El proyecto está configurado en UTF-8 y la configuración de ejecución ya incluye
  `-Dfile.encoding=UTF-8`. Si aun así ves caracteres raros en la consola, revisar
  `Settings > Editor > File Encodings`.
- La entrada por teclado funciona normalmente en la ventana **Run** de IntelliJ:
  se escribe directamente ahí y se confirma con Enter.
