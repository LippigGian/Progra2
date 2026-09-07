# Guía razonada — TP 02 (TDA List)

En el TP 01 construiste la infraestructura. Este TP es el primero que la usa: vas a agregar un ejercicio nuevo **sin tocar ni una línea de `MainProgram.run()` ni de `Exercise`**. Si lo lográs, el diseño del TP 01 estaba bien.

Además, es el primer contacto real con la materia: acá aparece la distinción entre **especificación** e **implementación** de un TDA, que es el eje conceptual que más se evalúa en toda la cursada.

---

## 1. El concepto central: qué es un TDA y por qué `List` es uno

Un **Tipo de Dato Abstracto** son dos cosas separadas:

| | Qué es | En este TP |
|---|---|---|
| **Especificación** | *Qué* operaciones existen y qué hace cada una | La interfaz `List<E>`: `add`, `remove`, `get`, `size`, `isEmpty`, `clear`… |
| **Implementación** | *Cómo* se guardan los datos en memoria | `ArrayList` (array dinámico) o `LinkedList` (nodos enlazados) |

La palabra clave es **abstracto**: quien usa una `List` sabe qué puede pedirle, pero no necesita saber cómo está construida por dentro. Eso es lo que te permite escribir esta línea:

```java
private List<String> list;
```

y después decidir en el constructor si adentro hay un array o una cadena de nodos. Todo el resto del código —los cuatro métodos de operación, el menú, el `printElements`— funciona idéntico con cualquiera de las dos. **Ese intercambio es la demostración práctica de qué significa un TDA**, y por eso la consigna te pide probar con ambas.

Nunca declares `private ArrayList<String> list;`. Compila, pero te atás a la implementación: si mañana querés cambiar a `LinkedList` tenés que tocar la declaración, el constructor y cualquier lugar donde aparezca el tipo. La regla se llama *programar contra la interfaz, no contra la implementación*.

### ArrayList vs LinkedList

Ambas cumplen el mismo contrato pero con costos distintos. Esto conecta directo con complejidad algorítmica:

| Operación | `ArrayList` | `LinkedList` | Por qué |
|---|---|---|---|
| `get(i)` | **O(1)** | O(n) | El array calcula la dirección; la lista enlazada tiene que caminar nodo por nodo |
| `add(e)` al final | O(1) amortizado | **O(1)** | El ArrayList a veces tiene que agrandar el array y copiar todo |
| `add(0, e)` / `remove(0)` | O(n) | **O(1)** | El ArrayList corre todos los elementos; la enlazada solo reengancha punteros |
| `remove(Object)` | O(n) | O(n) | En los dos casos hay que buscar primero |
| Memoria | Menos: solo el array | Más: cada nodo guarda 2 referencias extra | |

"Amortizado" significa que casi siempre es O(1), pero cada tanto una inserción cuesta O(n) porque hay que duplicar el array. El promedio sigue siendo constante.

Cuando pruebes las dos implementaciones, el programa se va a comportar **exactamente igual**. Esa es justamente la conclusión que tenés que poder explicar: cambia el rendimiento, no el comportamiento.

---

## 2. Paso 0 — Crear el package y la clase

1. Click derecho sobre `src` > `New > Package` > nombre `listModule`.
2. Dentro, `New > Class` > nombre `ListExercise`.

**¿Por qué un package aparte?** Porque a medida que sumes TPs, `application` se llenaría de clases sin relación entre sí. `application` queda como la infraestructura (menú, clase base) y cada tema tiene su propio módulo.

Esto trae una consecuencia técnica importante: como `ListExercise` está en **otro package**, necesita importar la clase base:

```java
import application.Exercise;
```

Y funciona porque en el TP 01 declaraste `public abstract class Exercise` y `protected` sus campos. Detalle fino que vale saber: **`protected` permite el acceso desde una subclase aunque esté en otro package**. Si hubieras usado el modificador por defecto (sin escribir nada), `ListExercise` no compilaría. Los modificadores de acceso del TP 01 recién ahora muestran para qué servían.

Y en `MainProgram` vas a necesitar:

```java
import listModule.ListExercise;
```

---

## 3. Los campos

```java
private int currentPhase = 0;
private boolean firstTime = true;
private List<String> list;
```

### `currentPhase` — cuidado, acá hay una trampa

`Exercise` ya tiene un `protected int currentPhase = 1`. La consigna te pide declarar **otro** en `ListExercise`. Eso se llama **ocultamiento de campo** (*field hiding*): ahora existen dos variables con el mismo nombre en el mismo objeto, y dentro de `ListExercise` el nombre `currentPhase` se refiere siempre a la del hijo.

Consecuencias concretas:

- El `run()` de `Exercise` hace `currentPhase = 1`, pero toca **el del padre**. El de `ListExercise` arranca en `0` y ahí se queda. Por suerte es lo que querés: fase 0 = menú.
- El `repeatOperationCheck()` heredado también modifica el del padre. Por eso en este TP conviene no usarlo (ver punto 6).
- Si alguna vez necesitás el del padre, sería `super.currentPhase`. No lo vas a necesitar, pero si el profesor pregunta, esa es la sintaxis.

Ojo con la diferencia respecto de `running`: ese **no** lo redeclarás, así que cuando escribís `running = false` estás tocando el del padre, que es justo lo que hace falta para salir del ejercicio y volver al menú principal.

### `firstTime`

Un flag que se apaga y no se vuelve a encender. Distingue "es la primera vez que muestro el menú" (bienvenida) de "ya estuve acá antes" (mostrar el estado de la lista). No podés usar `currentPhase` para esto porque volvés a la fase 0 muchas veces.

### `list`

Declarada con el tipo de la interfaz, por lo explicado en la sección 1.

`<String>` son **genéricos**. Le dicen al compilador que esta lista solo contiene `String`. Ganás dos cosas: si intentás meter un `int` no compila, y cuando sacás un elemento ya viene como `String` sin necesidad de castear. Sin genéricos (`List list`, estilo Java 1.4) todo sale como `Object` y tendrías que escribir `(String) list.get(0)`.

---

## 4. El constructor

```java
public ListExercise(Scanner scanner) {
    super(scanner);
    this.list = new ArrayList<>();
}
```

`super(scanner)` primero y obligatorio, igual que en `TestExercise`: el `Scanner` lo sigue administrando `MainProgram`, y acá solo se pasa hacia arriba.

`new ArrayList<>()` — el `<>` vacío se llama *diamond operator*. El compilador deduce que es `<String>` mirando la declaración de la variable. Escribir `new ArrayList<String>()` es igual de válido, solo más largo.

**Para la entrega**: ejecutá el programa con `ArrayList`, comprobá el comportamiento, después cambiá esa única línea por `new LinkedList<>()` (agregando `import java.util.LinkedList;`), volvé a ejecutar y verificá que todo funciona igual. Ese "todo funciona igual" es el resultado del experimento.

---

## 5. `exerciseLogic()` — la máquina de estados

```java
@Override
protected void exerciseLogic() {
    switch (currentPhase) {
        case 0: menuLogic();          break;
        case 1: addElement();         break;
        case 2: removeByIndex();      break;
        case 3: removeByReference();  break;
        case 4: clearList();          break;
        default: currentPhase = 0;
    }
}
```

Acordate de cómo funciona el padre:

```java
while (running) {
    exerciseLogic();   // se llama una y otra vez
}
```

`exerciseLogic()` **no es el programa entero: es una sola vuelta**. En cada vuelta mira en qué fase está y ejecuta el método que corresponde. Los métodos, al terminar, dejan escrita la próxima fase. Eso es una máquina de estados, y es exactamente lo que la consigna quiere decir con *"permite que el programa pueda comportarse distinto en distintos momentos"*.

El flujo típico es un ida y vuelta:

```
fase 0 (menú) → el usuario elige "1" → fase 1
fase 1 (agregar) → ¿repetir? sí → sigue en fase 1
fase 1 (agregar) → ¿repetir? no → fase 0
fase 0 (menú) → el usuario elige "0" → running = false → se termina el ejercicio
```

El `default` es una red de seguridad: si por un bug la fase quedara en un número que no existe, en vez de no hacer nada para siempre volvés al menú.

---

## 6. `menuLogic()`

```java
private void menuLogic() {
    if (firstTime) {
        System.out.println("\n===== Ejercicio de TDA List =====");
        System.out.println("Implementación en uso: " + list.getClass().getSimpleName());
        firstTime = false;
    } else {
        showListState();
    }
    ...
}
```

`list.getClass().getSimpleName()` te imprime `ArrayList` o `LinkedList` según lo que hayas puesto en el constructor. No es obligatorio, pero es un lindo detalle: demuestra en pantalla que el mismo código corre sobre dos implementaciones distintas.

Después vienen las opciones. Fijate que **el menú no ejecuta la operación**: solo asigna la fase y termina. La operación se ejecuta en la vuelta siguiente del bucle. Si llamaras directamente a `addElement()` desde acá, `currentPhase` no serviría para nada y estarías incumpliendo la consigna.

La opción `0` no es una fase: hace `running = false`, que corta el `while` de `Exercise.run()` y te devuelve al menú de `MainProgram`. Sin esta opción quedarías encerrado en el ejercicio.

El `default` del `switch` avisa del error y no cambia la fase, así que en la vuelta siguiente vuelve a mostrarse el menú. Cumple con "si la entrada no es válida, se repite".

### Por qué no uso el `repeatOperationCheck` heredado

Tentador, pero **hace algo distinto de lo que necesitás acá**: pone `running = false`, o sea sale del ejercicio entero. Vos querés volver al menú del ejercicio, que es `currentPhase = 0`. Por eso `ListExercise` tiene su propio helper:

```java
private boolean askRepeat() { ... return answer.equals("s"); }
```

Devuelve un `boolean` en vez de modificar estado, y quien lo llama decide qué hacer. Es una diferencia de diseño que conviene tener clara: **un método que responde una pregunta es más reutilizable que uno que toma la decisión por vos**.

(Si preferís reutilizar el del padre, también es defendible: llamalo y después hacé `if (!running) { running = true; currentPhase = 0; }`. Funciona, pero es más confuso de leer.)

---

## 7. Las cuatro operaciones

Las cuatro siguen la misma estructura, que sale directo de la consigna:

1. Verificar que la operación sea posible.
2. Pedir el dato.
3. Modificar la lista.
4. Mostrar los elementos.
5. Preguntar si repite (salvo limpiar).

### `addElement()`

La única validación es que no ingrese una cadena vacía. `list.add(e)` en una `List` siempre puede ejecutarse: acepta duplicados y no tiene tamaño máximo. Devuelve `boolean`, pero en `List` siempre es `true` (el `boolean` existe porque la firma viene de `Collection`, donde un `Set` sí puede rechazar un duplicado).

### `removeByIndex()` y `removeByReference()` — la trampa clásica de Java

Estos dos métodos existen para que veas la diferencia entre las dos sobrecargas de `remove`:

```java
list.remove(2);     // int    → remove(int index)  → borra la POSICIÓN 2
list.remove("2");   // Object → remove(Object o)   → borra el ELEMENTO "2"
```

Java elige la sobrecarga por el **tipo del argumento**, no por lo que vos quisiste decir. Con una `List<String>` la ambigüedad se nota poco, pero con una `List<Integer>` es un bug muy común: `lista.remove(2)` borra la tercera posición, no el número 2. Para forzar el otro comportamiento hay que escribir `lista.remove(Integer.valueOf(2))`.

Diferencias prácticas:

| | Por índice | Por referencia |
|---|---|---|
| Firma | `E remove(int index)` | `boolean remove(Object o)` |
| Devuelve | el elemento borrado | si borró o no |
| Si falla | lanza `IndexOutOfBoundsException` | devuelve `false`, no lanza nada |
| Alcance | esa posición exacta | **solo la primera aparición** |

Dos aclaraciones importantes:

- **"Por referencia" es un nombre engañoso.** `remove(Object)` no compara direcciones de memoria: usa `.equals()`. Por eso funciona con `String` escritos por teclado, que son objetos distintos con el mismo contenido. Si comparara con `==` no borraría nunca nada.
- **Solo borra la primera aparición.** Si la lista tiene `"a", "b", "a"` y removés `"a"`, queda `"b", "a"`.

Sobre las validaciones que pide la consigna ("contemplar que la operación sea posible"):

- Lista vacía → avisar y volver al menú sin pedir nada.
- Índice fuera de rango → `if (index < 0 || index >= list.size())`. Chequearlo **antes** es mejor que dejar que explote y atrapar la excepción: las excepciones son para lo inesperado, no para el control de flujo normal.
- Texto que no es número → `Integer.parseInt` lanza `NumberFormatException`, y ahí sí hace falta `try/catch`, porque no tenés forma de saber si un `String` es numérico sin intentar convertirlo.

Acordate de por qué seguimos leyendo con `nextLine()` + `parseInt` en lugar de `nextInt()`: `nextInt()` deja el `\n` en el buffer y hace que el siguiente `nextLine()` devuelva vacío sin esperar. Es el bug del TP 01.

### `clearList()`

La excepción a la estructura general: no pide dato y no pregunta si repetir (repetir un `clear` no tendría sentido). Va directo a `currentPhase = 0`. `list.clear()` sobre una lista vacía no falla, pero el mensaje queda más prolijo si distinguís los dos casos.

### `printElements()`

```java
System.out.println("Elementos: " + String.join(", ", list));
```

`String.join` toma un separador y cualquier colección de textos, y arma la cadena poniendo el separador **entre** elementos (no al final). Es la forma corta de hacer esto:

```java
for (int i = 0; i < list.size(); i++) {
    System.out.print(list.get(i));
    if (i < list.size() - 1) System.out.print(", ");
}
```

Las dos versiones son válidas; si el profesor quiere ver el recorrido explícito, usá la larga. Un detalle de complejidad: la versión con `get(i)` es O(n) en `ArrayList` pero **O(n²) en `LinkedList`**, porque cada `get` vuelve a caminar la lista desde el principio. Con un `for-each` (`for (String s : list)`) es O(n) en las dos. Es un ejemplo perfecto de por qué la implementación importa aunque la especificación sea la misma.

---

## 8. Conectar el ejercicio con `MainProgram`

Acá se cobra la inversión del TP 01. Dos cambios y nada más:

```java
import listModule.ListExercise;   // arriba de todo
```

```java
case "2":
    exercise = new ListExercise(scanner);
    validOption = true;
    break;
```

(Y agregá la línea `System.out.println("2 - Ejercicio de List");` al menú.)

No tocaste `run()`, no tocaste `Exercise`, no tocaste `TestExercise`. `MainProgram` sigue sin saber qué es un `ListExercise`: solo sabe que es un `Exercise` y que tiene `run()`. **Eso es polimorfismo**, y es la respuesta a la pregunta "¿para qué servía declarar `private Exercise exercise` en vez de `private TestExercise exercise`?".

---

## 9. Prueba manual antes de entregar

Corré esta secuencia y verificá cada resultado:

| Acción | Qué tiene que pasar |
|---|---|
| Entrar al ejercicio | Bienvenida + menú (sin estado, la lista no existe todavía para el usuario) |
| Remover por índice con lista vacía | Avisa que está vacía y vuelve al menú sin pedir índice |
| Agregar `hola`, repetir, agregar `chau` | Muestra `hola, chau` |
| Volver al menú | Ahora sí muestra estado: elementos, cantidad 2, no vacía |
| Remover índice `5` | "Índice fuera de rango", no rompe |
| Remover índice `abc` | "Debe ingresar un número entero", no rompe |
| Remover índice `0` | Borra `hola`, queda `chau` |
| Remover por referencia `xxx` | "El elemento no se encuentra" |
| Agregar `chau` de nuevo, remover por referencia `chau` | Borra **una sola** aparición |
| Contestar `k` a "¿repetir?" | Vuelve a preguntar hasta que pongas s o n |
| Limpiar | Vacía y vuelve al menú **sin** preguntar si repite |
| Opción `0` | Vuelve al menú principal de la aplicación |
| Cambiar a `LinkedList` y repetir todo | Resultados idénticos |

---

## 10. Preguntas de defensa probables

1. **¿Qué es un TDA?**
   Un tipo definido por sus operaciones y el comportamiento de esas operaciones, independientemente de cómo estén implementadas. `List` es la especificación; `ArrayList` y `LinkedList`, dos implementaciones.

2. **¿Por qué declarás `List<String>` y no `ArrayList<String>`?**
   Para depender de la especificación y no de la implementación. Cambiar de estructura es una sola línea y ningún otro método se entera.

3. **¿Cuándo conviene `ArrayList` y cuándo `LinkedList`?**
   `ArrayList` si accedés mucho por índice; `LinkedList` si insertás o borrás mucho al principio o en el medio con un iterador.

4. **¿Qué diferencia hay entre `remove(2)` y `remove("2")`?**
   La primera borra por posición, la segunda por contenido usando `equals()`. Java elige por el tipo del argumento.

5. **¿Por qué hay dos `currentPhase`?**
   Porque el de `ListExercise` es `private` y oculta al heredado. Dentro de la subclase el nombre siempre refiere al propio; al del padre se accede con `super.currentPhase`.

6. **¿Para qué sirve la fase si podrías llamar los métodos directo desde el menú?**
   Porque `exerciseLogic()` se ejecuta una vez por vuelta del bucle del padre. La fase es lo que hace que cada vuelta sepa qué le toca, y permite repetir una operación sin volver a mostrar el menú.

7. **¿Por qué `ListExercise` compila si está en otro package?**
   Porque `Exercise` es `public` y sus miembros son `protected`, que habilita el acceso desde subclases aunque estén en otro package.
