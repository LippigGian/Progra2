# TP 04: historial de navegador web

El programa simula el historial de un navegador por consola. Permite escribir
el nombre de una página, volver atrás y volver adelante. En cada paso muestra
la página actual y las opciones disponibles.

## Las dos versiones

Hay dos módulos distintos para el navegador:

| Opción del menú | Módulo | Clases | Pila que utiliza |
|---|---|---|---|
| 4 | `browserModule` | `BrowserExercise` y `Navegador` | `MyArrayStack`: arreglos |
| 5 | `linkedBrowserModule` | `LinkedBrowserExercise` y `NavegadorEnlazado` | `MyLinkedStack`: nodos enlazados |

Ambas versiones hacen lo mismo. Cambia la forma de guardar los elementos en
las pilas. Las clases `BrowserExercise` y `LinkedBrowserExercise` muestran el
menú y leen los datos; `Navegador` y `NavegadorEnlazado` manejan el historial.

Las pilas están en `stackModule`, separadas del navegador para poder
reutilizarlas. Ambas implementan la interfaz `MyStack<E>`, que declara sus
operaciones. `E` representa el tipo de elemento; el navegador usa `String`
para guardar los nombres de las páginas.

## Qué es una pila y cuáles son sus métodos

Una pila funciona con la regla **LIFO**: el último elemento que entra es el
primero que sale. El **tope** es el último agregado que todavía no fue retirado.

| Método | Qué hace |
|---|---|
| `push(element)` | Agrega un elemento al tope. |
| `pop()` | Retira y devuelve el elemento del tope. |
| `peek()` | Devuelve el elemento del tope sin retirarlo. No imprime nada. |
| `isEmpty()` | Devuelve `true` si la pila está vacía. |
| `size()` | Devuelve la cantidad de elementos guardados. |
| `clear()` | Vacía la pila. |

Por ejemplo, si agregamos A, B y C, el tope es C. `peek()` devuelve C sin
modificar la pila. `pop()` devuelve C y lo elimina; B pasa a ser el tope.

## Versión con arreglos: MyArrayStack

Los elementos se guardan en el arreglo `elements`. `size` cuenta cuántos hay;
no es la capacidad del arreglo. La capacidad inicial es 4 (`DEFAULT_SIZE`).

- `push` agrega en `elements[size]` y aumenta `size`.
- `pop` retira el elemento de `elements[size - 1]`, reduce `size` y deja en
  `null` la posición que quedó libre.
- `peek` devuelve `elements[size - 1]`.
- `clear` crea un arreglo vacío de capacidad inicial y pone `size` en cero.

Antes de agregar, `validateSize(size + 1)` verifica si hay espacio. Si el tamaño
necesario supera la capacidad, llama a `resize()`, que crea un arreglo del doble
de tamaño y copia los elementos: 4 → 8 → 16…

No hacen falta `shiftLeft` ni `shiftRight`: se agrega y se retira al final,
sin desplazar los demás elementos.

## Versión con nodos: MyLinkedStack

Cada `Node<E>` contiene un dato (`element`) y una referencia al siguiente nodo
(`next`). La variable `last` apunta al tope: el último nodo agregado.

- `push` crea un nodo que apunta al tope anterior y lo convierte en `last`.
- `pop` guarda el dato de `last`, cambia `last` por `last.next` y devuelve el dato.
- `peek` devuelve `last.element` sin cambiar los enlaces.
- `clear` pone `last` en `null` y `size` en cero.

Ejemplo después de agregar A, B y C:

```text
last → C → B → A → null
```

Si hacemos `pop()`, sale C y queda:

```text
last → B → A → null
```

No necesita verificar capacidad ni duplicar un arreglo: cada `push` crea un
nodo nuevo. `size` solo cuenta los elementos.

## Cómo funciona la navegación

Cada navegador tiene una `paginaActual` y dos pilas:

- `atras`: guarda las páginas a las que podemos retroceder.
- `adelante`: guarda las páginas a las que podemos avanzar después de retroceder.

**Visitar una página (`visitar`)**: guarda la actual en `atras` con `push`, si
existe; cambia la página actual y vacía únicamente `adelante` con `clear`.

**Volver atrás (`retroceder`)**: guarda la actual en `adelante` con `push` y
obtiene la nueva actual mediante `atras.pop()`.

**Volver adelante (`avanzar`)**: guarda la actual en `atras` con `push` y
obtiene la nueva actual mediante `adelante.pop()`.

`getPaginaActual()` devuelve la página que se muestra. `puedeRetroceder()` y
`puedeAvanzar()` consultan si la pila correspondiente tiene elementos usando
`isEmpty()`. Si no hay historial, el movimiento devuelve `false` y la consola
avisa sin cambiar la página.

### Ejemplo

1. Visitamos A, B y C. La página actual es C.
2. Volvemos atrás. La actual es B y podemos avanzar a C.
3. Desde B visitamos D. La actual pasa a ser D.
4. Ya no podemos avanzar a C, pero podemos volver atrás a B y luego a A.

Visitar una página nueva elimina **solo el historial hacia adelante**.

## Datos inválidos

Las pilas rechazan `null` en `push` y lanzan una excepción si se llama a `pop`
o `peek` estando vacías. El navegador comprueba si hay historial antes de
retirar elementos. La consola también rechaza nombres vacíos y opciones
inválidas, mostrando un mensaje para que el usuario pueda continuar.
