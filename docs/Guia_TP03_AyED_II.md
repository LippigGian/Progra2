# Guía razonada — TP 03 (Implementación del TDA List + Playlist)

Este TP tiene un salto de dificultad. Hasta ahora usabas una lista; ahora **la construís vos**. Y encima tenés que desarrollar una aplicación completa sobre ella.

La consigna esconde una idea de diseño muy fuerte en las observaciones:

> *Las implementaciones del TDA en sí deben generar excepciones si reciben comandos o datos inválidos, es la misma aplicación la que debe asegurar que eso no ocurra.*

Traducido: **la estructura de datos no perdona, la aplicación protege**. Volvemos sobre esto en la sección 2, porque es lo que más te van a preguntar.

---

## 1. Arquitectura: tres capas

| Capa | Package | Clases | Qué sabe |
|---|---|---|---|
| **TDA** | `listModule` | `MyList<T>`, `MyArrayList<T>` | Nada de canciones. Guarda objetos genéricos. |
| **Modelo** | `playlistModule` | `Song`, `Playlist` | Qué es una canción y qué significa reproducir. **No imprime nada.** |
| **Consola** | `playlistModule` | `PlaylistExercise` | Cómo hablar con el usuario. **No tiene lógica de negocio.** |

Las dependencias van en una sola dirección:

```
PlaylistExercise  →  Playlist  →  MyList  ←  MyArrayList
     (consola)       (modelo)      (TDA)     (implementación)
```

`MyArrayList` no sabe que existen las canciones. `Playlist` no sabe que existe la consola. Esa separación es lo que te permite defender el diseño: si mañana el profesor te dice "cambialo a interfaz gráfica", reescribís `PlaylistExercise` y nada más. Si te dice "usá una lista enlazada", escribís `MyLinkedList implements MyList` y cambiás **una línea** del constructor de `Playlist`.

Un error muy común es meter los `System.out.println` adentro de `Playlist`. Compila y funciona, pero rompe la separación: la clase de negocio pasa a depender de la consola y ya no es reutilizable. Si ves un `println` fuera de `PlaylistExercise`, algo está mal ubicado.

---

## 2. La regla de oro: quién valida y quién lanza

Esta es la parte que la consigna marca explícitamente y la que más se presta a preguntas.

**El TDA es exigente.** `MyArrayList.get(7)` sobre una lista de 3 elementos lanza `IndexOutOfBoundsException`. No devuelve `null`, no devuelve `-1`, no imprime un mensaje: rompe. Y está bien que rompa.

**La aplicación es la que se agacha.** Antes de llamar a `removeAt(index)`, `PlaylistExercise` ya verificó que la playlist no esté vacía y que el número esté dentro del rango.

¿Por qué así y no al revés?

1. **Una estructura de datos no sabe qué hacer con un error.** ¿Debería ignorarlo? ¿Devolver null? Cada aplicación quiere algo distinto. Lo único honesto que puede hacer es avisar que se violó el contrato y dejar que el que llamó decida.
2. **Si el TDA "arreglara" los errores silenciosamente, los bugs se esconderían.** Un `get()` que devuelve `null` en vez de romper hace que el programa siga andando con datos mal, y el `NullPointerException` te explota tres métodos más adelante, lejos de la causa.
3. **La validación depende del contexto.** "Índice inválido" para la lista es un error; para la aplicación es "el usuario se equivocó, hay que pedirle de nuevo". Son dos niveles distintos y cada uno responde a su altura.

Esto se llama **programación por contrato**: cada método declara qué espera recibir (precondición) y qué garantiza devolver (postcondición). Si el cliente incumple la precondición, la culpa es del cliente.

Consecuencia práctica: **en un uso normal de tu programa no debería lanzarse nunca una excepción del TDA**. Si al probar te salta una, no significa que el TDA esté mal: significa que a la aplicación le faltó una validación.

En el código esto se ve en dos lugares:

```java
// En MyArrayList: exige y rompe
private void checkIndex(int index) {
    if (index < 0 || index >= size) {
        throw new IndexOutOfBoundsException("Índice " + index + " fuera de rango...");
    }
}

// En PlaylistExercise: verifica y avisa
if (number < 1 || number > playlist.size()) {
    System.out.println("Ese numero no corresponde a ninguna cancion.");
} else {
    playlist.removeAt(number - 1);
}
```

Fijate que el `try/catch` aparece en un solo lugar: `readNumber()`, para atrapar `NumberFormatException`. Ahí sí hace falta, porque no hay forma de saber si un texto es numérico sin intentar convertirlo. Para los índices no hace falta ningún `catch`: se validan con un `if`. **Las excepciones son para lo inesperado, no para el control de flujo normal.**

---

## 3. Capa 1 — El TDA

### La interfaz `MyList<T>`

```java
public interface MyList<T> {
    void add(T element);
    T get(int index);
    T remove(int index);
    boolean remove(T element);
    int indexOf(T element);
    int size();
    boolean isEmpty();
    void clear();
    ...
}
```

Una interfaz es **la especificación pura**: solo firmas, sin nada de código. Es el contrato escrito.

Dos detalles que valen la pena:

- **`<T>` es un parámetro de tipo.** La lista no sabe qué guarda. Cuando escribís `MyList<Song>`, el compilador reemplaza mentalmente cada `T` por `Song` y te obliga a respetarlo. Sin genéricos tendrías `Object` en todos lados y castearías a mano en cada `get`.
- **`capacity()` no está en la interfaz.** La capacidad del arreglo interno solo tiene sentido para una implementación con arreglo; una lista enlazada no tiene capacidad. Poner `capacity()` en la interfaz sería filtrar un detalle de implementación hacia la especificación. Este es un ejemplo chiquito pero muy citable de dónde va la frontera entre las dos cosas.

### `MyArrayList<T>` — el arreglo dinámico

#### Los dos campos y el invariante

```java
private T[] elements;   // el arreglo, con lugares de sobra
private int size;       // cuántos lugares están realmente ocupados
```

**`size` y `elements.length` son cosas distintas y confundirlas es el error número uno.**

- `elements.length` es la **capacidad**: cuánto entra.
- `size` es el **tamaño**: cuánto hay.

Un arreglo de Java tiene tamaño fijo desde que se crea. Como la lista tiene que crecer, la técnica es reservar de más y llevar la cuenta aparte de hasta dónde llegan los datos reales.

El invariante que mantiene toda la clase:

```
posiciones 0 .. size-1        → elementos válidos
posiciones size .. length-1   → null (basura, no mirar)
size <= elements.length
```

Todos los recorridos van hasta `size`, nunca hasta `elements.length`. Si alguna vez ves un `for (int i = 0; i < elements.length; i++)` en tu código, casi seguro es un bug.

#### El casteo raro del constructor

```java
@SuppressWarnings("unchecked")
public MyArrayList() {
    this.elements = (T[]) new Object[INITIAL_CAPACITY];
}
```

Esto se ve mal pero es la forma canónica. `new T[10]` **no compila en Java**, por el *type erasure*: los genéricos existen solo en tiempo de compilación, y en tiempo de ejecución `T` ya no está, así que la JVM no sabría qué tipo de arreglo crear.

La salida es crear un `Object[]` (que sí se puede) y castearlo. El casteo es seguro porque los únicos que escriben en ese arreglo son los métodos de la clase, y todos reciben `T`. `@SuppressWarnings("unchecked")` le dice al compilador "ya sé que no podés verificarlo, me hago cargo". Si querés ver cómo lo resuelve la biblioteca estándar, `java.util.ArrayList` hace exactamente lo mismo.

#### El crecimiento

```java
private void ensureCapacity() {
    if (size < elements.length) {
        return;
    }
    T[] bigger = (T[]) new Object[elements.length * 2];
    for (int i = 0; i < size; i++) {
        bigger[i] = elements[i];
    }
    elements = bigger;
}
```

Cuando el arreglo se llena: creo uno del doble, copio todo, y hago que `elements` apunte al nuevo. El viejo queda sin referencias y el garbage collector lo recoge.

**¿Por qué duplicar y no sumar 1?** Es la diferencia entre O(n) amortizado y O(n²):

- Si agrandás de a 1, cada `add` copia todo el arreglo: n inserciones cuestan 1+2+3+…+n = **O(n²)**.
- Si duplicás, las copias son cada vez más raras (en la 4, la 8, la 16, la 32…). El total de copias para n inserciones es 1+2+4+…+n ≈ 2n, o sea **O(n) en total**, que da **O(1) amortizado** por inserción.

"Amortizado" es exactamente esto: casi todas las operaciones son baratas, unas pocas son caras, y el promedio es constante. Es un concepto que vas a volver a ver en complejidad, así que tenerlo con un ejemplo propio te sirve.

Puse `INITIAL_CAPACITY = 4` a propósito, más chico que el 10 de Java: así, con la base de datos de 5 canciones, el arreglo ya tiene que crecer durante la carga y podés demostrarlo con `capacity()`.

#### Insertar y borrar: los corrimientos

```java
// remove(int index): tapar el hueco corriendo a la izquierda
for (int i = index; i < size - 1; i++) {
    elements[i] = elements[i + 1];
}
elements[size - 1] = null;
size--;
```

Un arreglo no tiene huecos: las posiciones son contiguas por definición. Si sacás el elemento 2 de 5, los que estaban en 3 y 4 tienen que bajar a 2 y 3.

Prestá atención a la **dirección** del recorrido, que es donde se cometen los errores:

- Al **borrar** vas de izquierda a derecha (cada uno pisa al anterior).
- Al **insertar** vas de derecha a izquierda (`for (int i = size; i > index; i--)`). Si lo hicieras al revés, el primer elemento que movés pisaría al siguiente y terminarías replicando el mismo valor n veces. Probalo si querés verlo.

**`elements[size - 1] = null;`** es la línea que más se olvida y la más interesante para defender. Sin ella el programa funciona igual (nadie mira más allá de `size`), pero el arreglo sigue guardando una referencia al objeto borrado y el garbage collector no puede liberarlo. Es una fuga de memoria silenciosa. `java.util.ArrayList` tiene esa misma línea con el comentario *"clear to let GC do its work"*.

#### `indexOf` y el `equals`

```java
if (elements[i].equals(element)) {
    return i;
}
```

La búsqueda es **secuencial**: recorre desde el principio hasta encontrar. O(n), y no hay forma de mejorarlo en una lista sin orden.

Usa `.equals()`, no `==`. Por eso `Song` sobrescribe `equals`: sin eso heredaría el de `Object`, que compara direcciones de memoria, y `contains` daría `false` aunque la canción estuviera. Es el mismo punto del TP 02 sobre "remover por referencia", pero ahora del lado de adentro.

Regla de Java: **si sobrescribís `equals`, sobrescribí también `hashCode`**. No lo usás en este TP, pero es el contrato del lenguaje y si no lo hacés te rompe el día que uses un `HashMap`.

#### Complejidad de tu implementación

Tenela lista, es pregunta segura:

| Operación | Costo | Por qué |
|---|---|---|
| `get(i)` / `set(i, e)` | **O(1)** | Acceso directo por dirección |
| `add(e)` al final | **O(1) amortizado** | Salvo cuando toca duplicar |
| `add(0, e)` | O(n) | Corre todos los elementos |
| `remove(i)` | O(n) | Corre los posteriores |
| `indexOf` / `contains` / `remove(T)` | O(n) | Búsqueda secuencial |
| `size` / `isEmpty` | **O(1)** | Es un contador |
| `clear` | O(n) | Anula las referencias |

---

## 4. Capa 2 — El modelo

### `Song`

Objeto de datos, inmutable (los campos son `final`, no hay setters). ¿Por qué inmutable? Porque una canción no cambia de título; si querés otra, creás otra. Los objetos inmutables no tienen estado que se pueda corromper.

`toString()` sobrescrito para que `System.out.println(song)` imprima algo legible en vez de `playlistModule.Song@1b6d3586`. Java llama a `toString()` automáticamente cada vez que concatenás un objeto con un `String`.

### `Playlist` — el modelo con estado

Esta clase es el corazón del ejercicio y lo que lo diferencia del TP 02. Guarda:

```java
private MyList<Song> songs;   // los datos
private int currentIndex;     // -1 = ninguna seleccionada
private boolean playing;
private boolean shuffle;      // bonus
private boolean loop;         // bonus
```

**Fijate el tipo declarado: `MyList<Song>`, no `MyArrayList<Song>`.** Mismo principio del TP 02, pero ahora con tu propia interfaz. Es lo que hace que la implementación sea intercambiable.

**`currentIndex = -1` como "ninguna".** Un valor centinela: un `int` no puede ser `null`, así que necesitás un valor imposible para representar la ausencia. -1 es la convención de Java (`indexOf` la usa). La alternativa sería un `boolean hasSelection` aparte, pero son dos variables que se pueden desincronizar; con el centinela hay una sola fuente de verdad.

#### El método más delicado: `adjustAfterRemoval`

```java
private void adjustAfterRemoval(int removedIndex) {
    if (songs.isEmpty()) {
        currentIndex = -1;
        playing = false;
    } else if (removedIndex == currentIndex) {
        currentIndex = -1;
        playing = false;
    } else if (removedIndex < currentIndex) {
        currentIndex--;
    }
}
```

Este método existe por un problema real: **`currentIndex` apunta a una posición, y borrar mueve las posiciones.**

Imaginá que estás escuchando la canción 4 y borrás la 1. Sin este ajuste, `currentIndex` sigue valiendo 3, pero ahora en el índice 3 hay otra canción: la que estabas escuchando pasó al 2. El reproductor "saltaría" de tema solo. El `currentIndex--` corrige eso.

Y si borrás la que estás escuchando, no hay a dónde apuntar: se detiene la reproducción.

Es el tipo de bug que no aparece hasta que probás en serio, y es exactamente lo que la consigna pide documentar y defender.

#### El bonus: `next()`

```java
public void next() {
    if (songs.isEmpty()) return;

    if (shuffle) {
        currentIndex = randomIndex();
        playing = true;
    } else if (currentIndex + 1 < songs.size()) {
        currentIndex++;
        playing = true;
    } else if (loop) {
        currentIndex = 0;
        playing = true;
    } else {
        playing = false;
    }
}
```

Las dos opciones del bonus son **independientes** y se combinan en cuatro comportamientos:

| shuffle | loop | Qué hace al llegar al final |
|---|---|---|
| off | off | Se detiene |
| off | on | Vuelve a la primera |
| on | off | Nunca "llega al final": siempre salta a otra al azar |
| on | on | Igual que la anterior |

Con `shuffle` activo el `loop` deja de tener efecto, porque el orden aleatorio no tiene un final. Es una consecuencia lógica, no un olvido, y conviene saber decirlo.

`randomIndex()` usa un `do/while` para no repetir la canción actual:

```java
do {
    candidate = random.nextInt(songs.size());
} while (candidate == currentIndex);
```

`nextInt(n)` devuelve entre 0 y n-1, justo el rango de índices válidos. El `if (songs.size() == 1) return 0;` de arriba es imprescindible: con una sola canción, el bucle nunca encontraría un índice distinto y el programa se colgaría para siempre. Un **bucle infinito por una condición imposible de cumplir** es un error clásico y este es un buen lugar para mostrarlo controlado.

---

## 5. Capa 3 — La consola

Misma máquina de estados del TP 02, con una diferencia que conviene poder justificar: **no todas las opciones tienen fase**.

El criterio es simple: *una fase existe para recordar en qué estás entre vueltas del bucle*. Si la operación pide datos al usuario y puede repetirse, necesita fase. Si se resuelve entera en una sola vuelta sin pedir nada, no.

| Opción | ¿Fase? | Por qué |
|---|---|---|
| Agregar, remover (x2), elegir canción | Sí | Piden datos y pueden repetirse |
| Play/Stop, Siguiente, Anterior, shuffle, loop | No | Un solo click, sin entrada, sin repetición |

Poner fases para todo también sería defendible, pero agregaría cuatro casos que se resuelven y vuelven a 0 inmediatamente.

### La conversión 1-based / 0-based

```java
System.out.println(marker + (i + 1) + ". " + playlist.getSong(i));   // mostrar
playlist.removeAt(number - 1);                                        // usar
```

Los humanos cuentan desde 1; los arreglos desde 0. La decisión es mostrar 1-based (nadie dice "la canción cero") y convertir en la frontera.

**La conversión ocurre en un solo lugar: la capa de consola.** `Playlist` y `MyArrayList` trabajan siempre en 0-based. Si mezclás las dos convenciones adentro del modelo, los errores off-by-one son inevitables.

Notá también que la validación se hace en las coordenadas del usuario (`number < 1 || number > size()`) y recién después se convierte. Es más fácil de leer y el mensaje de error queda en los términos que el usuario entiende.

### `readNumber()`

```java
private int readNumber() {
    String input = scanner.nextLine().trim();
    try {
        return Integer.parseInt(input);
    } catch (NumberFormatException e) {
        System.out.println("Debe ingresar un numero entero.");
        return -1;
    }
}
```

Devuelve -1 ante texto inválido, y como -1 nunca es un número de canción válido, la validación posterior lo rechaza sola. Un solo camino de error en vez de dos.

Seguimos sin usar `nextInt()`, por el problema del `\n` que queda en el buffer (el bug del TP 01).

### El marcador visual

```java
String marker = "   ";
if (i == playlist.getCurrentIndex()) {
    marker = playlist.isPlaying() ? " > " : " || ";
}
```

La consigna pide indicar cuál se está reproduciendo. Tres estados distinguibles: `>` reproduciendo, `||` seleccionada pero detenida, espacios el resto. Usé ASCII y no `▶` a propósito: la consola de Windows puede no estar en UTF-8 y te mostraría símbolos rotos.

**Sobre los acentos:** por el mismo motivo dejé los textos de consola sin tildes. Si querés ponerlas, andá a `Run > Run Configurations > Common > Encoding` en Eclipse y seleccioná UTF-8. Si ves `Ã³` en vez de `ó`, es esto.

---

## 6. La base de datos pre-programada

```java
public void loadSampleData() {
    addSong(new Song("De Musica Ligera", "Soda Stereo"));
    ...
}
```

Vive en `Playlist` (es un dato del modelo, no de la consola) y se llama desde el constructor de `PlaylistExercise`.

La consigna la pide "a modo de facilitar el testeo", y el motivo es concreto: sin datos precargados, cada vez que querés probar "remover la canción 3" tenés que cargar tres canciones a mano primero. Con la carga inicial, arrancás el programa y probás directo.

Si el profesor te pregunta por qué no está en un archivo: porque la persistencia no es contenido de este TP y agregar lectura de archivos sumaría manejo de `IOException` sin aportar nada al TDA. Es una respuesta válida y muestra que la decisión fue consciente.

---

## 7. Decisiones a documentar y defender

La consigna dice explícitamente que las decisiones de UX se defienden en el parcial. Estas son las tuyas, con su justificación:

| Decisión | Justificación |
|---|---|
| Numeración desde 1 en pantalla, desde 0 adentro | Natural para el usuario; la conversión se hace en un solo punto |
| Al borrar la canción en reproducción, se detiene | La alternativa (pasar a la siguiente) sorprende al usuario: pidió borrar, no cambiar de tema |
| Al borrar una canción anterior a la actual, se ajusta el índice | Sin el ajuste el reproductor cambiaría de tema solo |
| `next()` en la última sin loop: se detiene, no vuelve al principio | Volver al principio es justamente lo que hace el loop; si lo hiciera igual, la opción no serviría de nada |
| `previous()` en la primera sin loop: no hace nada | No hay canción anterior; retroceder al final sería sorprendente |
| `previous()` ignora el modo aleatorio | Un "anterior" fiel requeriría guardar un historial de reproducción. Se optó por la simplicidad; documentado como limitación conocida |
| El aleatorio nunca repite la canción actual | Que "siguiente" te deje en la misma canción se percibe como un bug |
| Remover por nombre no distingue mayúsculas | El usuario escribe a mano; exigir la capitalización exacta es hostil |
| Remover por nombre borra solo la primera coincidencia | Coincide con la semántica de `remove` del TDA; borrar todas sería una operación distinta |
| Entrada vacía cancela la operación | Da una salida al usuario que entró por error, sin necesidad de una opción "cancelar" |
| El artista es opcional (queda "Desconocido") | La consigna solo pide el nombre; el artista suma información sin volverse obligatorio |
| Toda validación en la consola, ninguna en el TDA | Lo pide la consigna: el TDA lanza, la aplicación previene |

---

## 8. Conectar con `MainProgram`

```java
import playlistModule.PlaylistExercise;
```

```java
case "3":
    exercise = new PlaylistExercise(scanner);
    validOption = true;
    break;
```

Tercer TP, tercera vez que agregás un `case` y nada más. Vale la pena decirlo en la defensa: el diseño del TP 01 se sostuvo sin modificaciones a lo largo de tres ejercicios de complejidad creciente.

---

## 9. Plan de pruebas

Probá esta secuencia completa antes de entregar:

**Del TDA**
- Agregá 5 canciones más a mano: el arreglo tiene que duplicarse sin perder nada (imprimí `capacity()` si querés verlo).
- Borrá la primera y verificá que el orden del resto se mantenga.
- Borrá la última.
- Vaciá la playlist y agregá una nueva: tiene que funcionar normal.

**De validación (nada debe crashear)**
- Número 0, negativo, mayor al tamaño, `abc`, Enter vacío.
- Remover por nombre inexistente.
- Remover con la playlist vacía.
- Play, next y previous con la playlist vacía.
- Responder cualquier cosa a "¿repetir?" hasta que aceptes s o n.

**Del estado de reproducción**
- Reproducí la 4, borrá la 1 → tiene que seguir sonando **la misma canción**.
- Reproducí la 3, borrá la 3 → la reproducción se detiene.
- Next hasta la última, next de nuevo sin loop → "Fin de la playlist".
- Activá loop, next en la última → vuelve a la primera.
- Previous en la primera sin loop → no pasa nada.
- Activá loop, previous en la primera → salta a la última.

**Del aleatorio**
- Activá aleatorio con 5 canciones y dale next varias veces: nunca debe quedarse en la misma.
- Dejá una sola canción, activá aleatorio y dale next: **no se tiene que colgar**.

---

## 10. Preguntas de defensa probables

1. **¿Por qué el TDA lanza excepciones en vez de manejar el error?**
   Porque una estructura de datos genérica no puede saber qué es lo correcto para cada aplicación. Lanza para avisar que se violó su contrato, y quien la usa decide qué hacer. Además, esconder el error retrasaría la aparición del bug.

2. **¿Cuál es la diferencia entre `size` y la longitud del arreglo?**
   `size` es cuántos elementos hay; la longitud es cuántos entran. El arreglo se reserva de más para no tener que agrandarlo en cada inserción.

3. **¿Por qué no se puede hacer `new T[10]`?**
   Por *type erasure*: los genéricos desaparecen al compilar y en tiempo de ejecución la JVM no sabe qué tipo de arreglo crear. Se crea un `Object[]` y se castea.

4. **¿Por qué duplicás la capacidad en vez de sumar una posición?**
   Duplicar da O(1) amortizado por inserción; crecer de a uno daría O(n²) para n inserciones.

5. **¿Para qué sirve `elements[size - 1] = null` si igual nadie lee esa posición?**
   Para que el garbage collector pueda liberar el objeto borrado. Sin esa línea el arreglo conserva una referencia y hay fuga de memoria.

6. **¿Cuál es la complejidad de `remove(int)` en tu implementación?**
   O(n): hay que correr todos los elementos posteriores. Con una lista enlazada sería O(1) una vez ubicado el nodo, pero ubicarlo cuesta O(n).

7. **¿Por qué `Song` sobrescribe `equals`?**
   Porque `indexOf`, `contains` y `remove(T)` comparan con `.equals()`. Sin sobrescribirlo se compararían referencias y nunca encontrarían nada.

8. **¿Qué pasa si borrás la canción que se está reproduciendo?**
   La reproducción se detiene y no queda ninguna seleccionada. Si se borra una anterior, se ajusta el índice para no cambiar de tema.

9. **¿Por qué `Playlist` no imprime nada?**
   Para que el modelo sea independiente de la interfaz. Si mañana se cambia la consola por una ventana gráfica, `Playlist` no se toca.

10. **¿Cómo cambiarías a una lista enlazada?**
    Escribiendo `MyLinkedList<T> implements MyList<T>` y cambiando una línea del constructor de `Playlist`. Ningún otro archivo se entera: eso es lo que significa depender de la especificación y no de la implementación.
