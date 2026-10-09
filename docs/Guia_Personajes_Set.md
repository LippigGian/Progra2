# Análisis de personajes de videojuegos con TDA Set

## Ejecutar y probar

Ejecutar `application.MainProgram` y elegir **7**. Se conservan los ejercicios anteriores.
En PowerShell, desde la carpeta `Progra2`, con JDK 17 o superior:

```powershell
$sources = @(Get-ChildItem src -Recurse -Filter *.java | ForEach-Object FullName)
javac -encoding UTF-8 --release 17 -d out @sources
java -cp out application.MainProgram
```

Pruebas automáticas sin bibliotecas adicionales:

```powershell
$sources = @(Get-ChildItem src -Recurse -Filter *.java | ForEach-Object FullName)
javac -encoding UTF-8 --release 17 -d out @sources test/CharacterSetTest.java
java -cp out CharacterSetTest
```

## Diseño y uso del TDA

- `setModule/MySet`: contrato genérico de conjunto, sin elementos repetidos.
- `setModule/MyArraySet`: implementación propia sobre `MyArrayList`, que ya usa
  un arreglo dinámico. No se utiliza `java.util.Set` ni `HashSet`.
- `characterModule/Habilidad` y `Personaje`: modelos con nombres inmutables.
  Cada personaje tiene su propio `MySet<Habilidad>`.
- `characterModule/CharacterExercise`: catálogos de personajes y habilidades
  también almacenados en Set; menús, validación y datos precargados.

El Set compara mediante `equals`. En ambos modelos, la identidad es el nombre:
se eliminan espacios exteriores, se colapsan espacios internos repetidos y se
ignoran mayúsculas usando `Locale.ROOT`. Se conservan los acentos: `Magia` y
`Mágia` son nombres distintos. `equals` y `hashCode` usan el mismo criterio.
Cambiar las habilidades no cambia la identidad del personaje.

El contrato considera inválidos `null`, agregar un elemento existente y quitar
uno ausente: lanza `IllegalArgumentException`. Consultar un elemento válido
ausente devuelve `false`. Los conjuntos vacíos son válidos. Unión, intersección
y diferencia producen conjuntos nuevos y no modifican los originales.
`toList` devuelve una copia del contenedor, útil para numerar las opciones;
el Set no expone índices como parte de su semántica. Las copias son superficiales.
`getHabilidades` también devuelve un contenedor nuevo; sus habilidades son
inmutables, por lo que el consumidor no puede alterar el conjunto interno.

## Comparaciones para defender en el parcial

Si A son las habilidades del primer personaje y B las del segundo:

| Resultado | Operación | Significado |
|---|---|---|
| En común | A ∩ B | Habilidades que tienen ambos |
| Solo el primero | A − B | Están en A y no en B |
| Solo el segundo | B − A | Están en B y no en A |
| Todas las distintas | A ∪ B | Habilidades presentes en al menos uno |

Se muestran las dos diferencias porque la diferencia no es conmutativa.
La unión complementa la comparación, sin sumar habilidades repetidas.
No se calcula un ganador: tener más habilidades no implica ser mejor.

## Base de datos preprogramada

Es una base en memoria, con habilidades simplificadas para fines académicos:

| Personaje | Habilidades |
|---|---|
| Link | Espada, Arco |
| Geralt | Espada, Magia |
| Lara Croft | Arco, Sigilo |
| Novato | Ninguna |

Link/Geralt permite verificar intersección `Espada`, diferencia A−B `Arco`,
diferencia B−A `Magia` y unión `Espada, Arco, Magia`.
Geralt/Lara permite verificar conjuntos disjuntos; Novato permite probar vacíos.
Se pueden crear personajes con las mismas habilidades para probar igualdad.

## Decisiones de experiencia de usuario

- Las acciones del submenú permiten listar, crear, asignar, quitar y comparar.
- Los nombres vacíos y duplicados se explican y devuelven al menú sin modificar datos.
- Las selecciones se hacen por números de listas visibles. Cada lista se numera
  nuevamente: al comparar, el segundo selector excluye al primer personaje.
- Al asignar se ofrecen solo habilidades del catálogo aún no asignadas; al quitar,
  solo habilidades del personaje. Esto evita duplicados y bajas inválidas antes
  de llamar al TDA. Quitar no elimina la habilidad del catálogo ni de otros personajes.
- `0` cancela cada selección o alta (queda reservado y no es un nombre válido).
  En el submenú vuelve al menú principal; allí `0` termina el programa.
- Texto, decimales, negativos, números enormes y opciones fuera de rango no
  producen crashes. Una selección inválida se vuelve a solicitar. Toda lectura
  del módulo verifica fin de entrada; una operación incompleta no modifica datos.
- Los resultados vacíos se muestran como `(ninguna)`.
- Los datos se cargan una sola vez por ejecución y se conservan al salir y volver
  al módulo. Al cerrar el programa se pierden: no se requiere persistencia en disco.
- El orden de inserción facilita una presentación estable, pero el significado
  matemático del conjunto no depende de ese orden.

## Complejidad y alcance

Con n elementos, `contains`, `add` y `remove` son O(n); la capacidad crece
dinámicamente. `toList` es O(n). Para operandos de tamaños n y m, unión es
O((n+m)²) en el peor caso. Intersección y diferencia son O(n·m+n²), pues
también se comprueban duplicados al construir el resultado. Se priorizan
simplicidad y reutilización del TDA List para catálogos pequeños del parcial.

Las pruebas cubren crecimiento, errores del contrato, álgebra con conjuntos
vacíos y con el mismo operando, copias, identidad, altas, asignación, baja,
comparación, entradas inválidas y fin de entrada durante cada operación.
Para comprobar la conservación de datos manualmente: crear un personaje,
volver al menú principal con `0`, entrar con `7` y listar con `1`.
