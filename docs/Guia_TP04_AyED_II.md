# TP 04: historial de navegador web

## Segunda versión: nodos enlazados

La opción 4 mantiene el navegador original con `MyArrayStack`. La opción 5
ejecuta `LinkedBrowserExercise` y `NavegadorEnlazado`, en `linkedBrowserModule`,
con dos pilas `MyLinkedStack`. Se mantienen archivos separados para comparar
ambas versiones, aunque la lógica de navegación es la misma.

`MyLinkedStack<E>` implementa la interfaz existente `MyStack<E>`. Cada `Node<E>`
guarda `element` y `next`. `last` apunta al tope; cuando está vacía vale `null`.

- `push`: crea un nodo que apunta al tope anterior y lo convierte en `last`.
- `pop`: devuelve `last.element` y avanza `last` al siguiente nodo.
- `peek`: devuelve `last.element` sin modificar los enlaces.
- `clear`: pone `last` en `null` y `size` en cero.

Estas operaciones cuestan O(1), sin contar la recolección de memoria. No hay
capacidad inicial ni `resize`: cada `push` crea un nodo. Se conservan las mismas
excepciones ante elementos nulos y consultas o extracciones en vacío.

Para verificar las dos versiones desde PowerShell:

```powershell
$sources = @(Get-ChildItem src -Recurse -Filter *.java | ForEach-Object FullName)
$testSources = @(Get-ChildItem tests -Filter *.java | ForEach-Object FullName)
javac -encoding UTF-8 --release 17 -d out $sources $testSources
java -cp out BrowserStackTest
java -cp out LinkedBrowserStackTest
```

Las secciones siguientes describen la versión original con arreglos.

Se implementa la primera opción de la consigna: una simulación por
consola de una pagina web. Se ingresa el nombre de una página; 

## Implementación del TDA Stack

- `MyStack<E>` especifica las operaciones de una pila genérica.
- `MyArrayStack<E>` las implementa con un arreglo dinámico propio. No utiliza
  `java.util.Stack`, `Deque` ni colecciones de Java.
- `Navegador` utiliza dos objetos declarados como `MyStack<String>`, construidos
  como `MyArrayStack<>`. La lógica depende de la interfaz del TDA.
- `BrowserExercise` hereda de `Exercise` y usa el Scanner compartido, igual que
  los ejercicios anteriores. Separa la entrada/salida de la lógica del historial.

Una pila sigue LIFO: el último elemento que entra es el primero que sale.
`push` apila, `pop` retira el tope y `peek` lo consulta sin retirarlo.
`size`, `isEmpty` y `clear` completan las operaciones.

El tope ocupa la posición `size - 1`. Cuando el arreglo se llena, se duplica su
capacidad mediante `validateSize` y `resize`. `pop` anula la referencia retirada;
`clear` reemplaza el arreglo por uno nuevo de capacidad `DEFAULT_SIZE` y pone
`size` en cero, permitiendo reutilizar la pila incluso tras vaciarla varias veces.

| Operación | Complejidad temporal |
|---|---|
| `push` | O(1) amortizado; O(n) cuando crece el arreglo |
| `pop`, `peek`, `size`, `isEmpty` | O(1) |
| `clear` | O(1), crea un arreglo de capacidad inicial fija |

## Cómo funcionan las dos pilas

La página actual se guarda aparte. La pila `atras` tiene como tope la página
anterior, y la pila `adelante`, la siguiente.

| Acción | Efecto |
|---|---|
| Visitar | Apilar la actual en `atras` si existe, cambiar la actual y vaciar `adelante` |
| Atrás | Apilar la actual en `adelante` y tomar la nueva actual con `atras.pop()` |
| Adelante | Apilar la actual en `atras` y tomar la nueva actual con `adelante.pop()` |

Ejemplo (pilas escritas desde la base hacia el tope):

| Paso | Atrás | Actual | Adelante |
|---|---|---|---|
| Visitar A, B y C | [A, B] | C | [] |
| Atrás | [A] | B | [C] |
| Atrás | [] | A | [C, B] |
| Adelante | [A] | B | [C] |
| Visitar D | [A, B] | D | [] |

C deja de estar disponible, pero A y B se conservan. Este es el caso central
que exige la consigna. Retroceder y avanzar cuestan O(1) amortizado por el
posible crecimiento de la pila destino. Visitar también cuesta O(1) amortizado:
vaciar el futuro reemplaza el arreglo en O(1), sin contar el trabajo posterior
del garbage collector.

Se usan los nombres del ejemplo del profesor: `MyArrayStack<E>`,
`MyStack<E>`, `elements`, `size`, `DEFAULT_SIZE`, `validateSize`, `resize`,
`nextArray` y `result`. Se conservan `push` y `pop` y el acceso al último elemento
para cumplir LIFO: el ejemplo recibido usa `enqueue` y `dequeue` y retira el
primero (FIFO). No se necesitan corrimientos ni validación de índices públicos
para una pila. `validateSize` crece cuando el tamaño solicitado supera la
capacidad, aprovechando todas las posiciones del arreglo.

## Validaciones y decisiones para defender

- Se comienza sin página actual ni historial. La consigna exime al navegador
  de tener datos precargados.
- En cada paso se muestran la página actual y las opciones. Se indica cuando
  atrás o adelante no están disponibles; seleccionarlos informa el motivo y
  mantiene el estado.
- La pila lanza `IllegalStateException` ante `pop` o `peek` en vacío, e
  `IllegalArgumentException` ante `push(null)`. No imprime mensajes de consola.
- La aplicación consulta si hay historial antes de desapilar, evitando esas
  excepciones durante el uso normal.
- Se rechazan nombres vacíos antes de llamar al modelo. El modelo también
  rechaza nombres vacíos o nulos antes de cambiar el historial.
- Se acepta cualquier nombre no vacío: la consigna pide nombres, no URLs
  válidas. Se eliminan espacios de los extremos.
- Visitar el mismo nombre cuenta como una nueva visita y también elimina el
  futuro. Las pilas almacenan visitas, no un conjunto de páginas únicas.
- Una opción desconocida, incluso texto, no modifica el estado. Se lee con
  `nextLine`, como en los TPs anteriores.
- La opción 0 vuelve al menú principal. Al volver a entrar se crea otra sesión
  de historial, siguiendo la creación de ejercicios de `MainProgram`.

## Verificación

Desde la raíz del proyecto, en PowerShell con JDK 17 o superior:

```powershell
$sources = @(Get-ChildItem src -Recurse -Filter *.java | ForEach-Object FullName)
javac -encoding UTF-8 --release 17 -d out $sources tests/BrowserStackTest.java
java -cp out BrowserStackTest
java -cp out application.MainProgram
```

Las pruebas verifican el orden LIFO tras varios crecimientos, las excepciones
del TDA, la reutilización tras vaciarlo, ambos límites del historial, movimientos
consecutivos atrás/adelante, la eliminación del futuro al visitar D, la
conservación del pasado y la recuperación de la consola ante entradas inválidas.
