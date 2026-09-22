# TP 05: mesas de restaurante y TDA Queue

La opción 6 del menú permite registrar grupos, hacer pasar al siguiente y
liberar mesas. Hay una mesa para 2 personas y otra para 4.

## Organización

- `queueModule`: contiene la interfaz `MyQueue<E>` y su implementación propia
  con nodos, `MyLinkedQueue<E>`.
- `restaurantModule`: contiene `Grupo` (nombre y cantidad de personas), `Mesa`
  (ocupante y cola de espera), `Restaurante` (las dos mesas) y
  `RestaurantExercise` (menú y entrada por teclado).

## Cómo funciona una cola

Una Queue sigue FIFO: el primero en entrar es el primero en salir.

| Método | Función |
|---|---|
| `enqueue(element)` | Agrega al final de la cola. |
| `dequeue()` | Retira y devuelve el primero. |
| `peek()` | Devuelve el primero sin retirarlo. |
| `isEmpty()` | Indica si está vacía. |
| `size()` | Devuelve la cantidad de elementos. |
| `clear()` | Vacía la cola. |

La clase separada `LinkedNode<E>` guarda `value`, `next` (nodo siguiente) y
`prev` (nodo anterior), como en el ejemplo del profesor. `first` apunta al primero
que será atendido y `last` al último que llegó. Al agregar, se enlaza el nuevo
nodo después de `last` y se conecta su `prev` al anterior. Al retirar, `first`
pasa al siguiente nodo y su `prev` queda en `null`. Si se retira
el único elemento, ambas referencias quedan en `null`.

Ejemplo: si llegan Ana, Luis y Pedro, la cola queda
`first → Ana → Luis → Pedro ← last`. El primer `dequeue()` devuelve Ana.
No se usa `java.util.Queue` ni una colección de Java para almacenar la espera.

## Cómo se asignan las mesas

Cada mesa tiene una cola independiente:

- Grupos de 1 o 2 personas: mesa de 2.
- Grupos de 3 o 4 personas: mesa de 4.
- Cantidades menores a 1 o mayores a 4: rechazadas.

`registrar` crea un grupo y lo anota con `enqueue` en la cola correspondiente.
`hacerPasar` usa `dequeue` solamente si la mesa está libre y hay grupos esperando.
El grupo retirado queda como ocupante hasta que se utiliza `liberar`.

En pantalla se ve si cada mesa está libre u ocupada, quién la ocupa, cuántos
grupos esperan y quién sigue. Para mostrar al siguiente se utiliza `peek`,
sin cambiar el orden de la cola.

## Decisiones de uso

- Se atiende por orden de llegada dentro de cada cola. Un grupo grande no
  bloquea a los pequeños, porque espera una mesa distinta.
- Los grupos pequeños no pasan a la mesa de 4, aunque esté libre: se respeta
  la asignación indicada en la consigna.
- Anotar un grupo no lo sienta automáticamente. Se utiliza la opción 2 para
  hacer pasar al siguiente, incluso si la mesa ya estaba libre.
- Liberar y hacer pasar son acciones separadas. Esto permite al encargado
  decidir cuándo la mesa está lista para recibir al siguiente grupo.
- Una mesa ocupada no admite otro grupo. Intentar liberar una mesa libre o
  atender una cola vacía muestra un aviso sin modificar el estado.
- Se permiten nombres repetidos: representan registros de grupos distintos.
- Se precargan Ana (2), Luis (1), Carla (4) y Diego (3), todos en espera.
  Ambas mesas empiezan libres. Al salir y volver a entrar se inicia otra sesión
  con esos datos de ejemplo; no hay persistencia en archivos.

## Validaciones

La consola rechaza nombres vacíos, cantidades que no sean enteros entre 1 y 4,
mesas distintas de 2 o 4 y opciones desconocidas. Se informa el error y se
vuelve al menú sin registrar datos incompletos.

El TDA lanza `IllegalArgumentException` si se intenta agregar `null`, e
`IllegalStateException` si se hace `dequeue` o `peek` sobre una cola vacía.
La aplicación verifica el estado antes de usar esas operaciones. Los modelos
también validan sus datos para evitar grupos o mesas inválidos.

## Ejemplo para probar

1. Hacer pasar un grupo a la mesa de 2: entra Ana.
2. Intentar hacer pasar otro: se avisa que está ocupada y Luis sigue esperando.
3. Hacer pasar un grupo a la mesa de 4: entra Carla, sin afectar la otra cola.
4. Liberar la mesa de 2 y hacer pasar al siguiente: entra Luis.
5. Registrar un grupo de 5: se rechaza y las colas permanecen iguales.
