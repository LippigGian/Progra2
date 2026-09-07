# Guía razonada — TP 01 (Fundamentos de Java, Eclipse y Herencia)

El objetivo del TP no es "hacer un menú de consola". Es construir un **esqueleto reutilizable**: una aplicación que va a alojar todos los ejercicios de los TPs siguientes sin que tengas que reescribir el menú, el `Scanner` ni el bucle principal. Cada decisión de diseño de la consigna apunta a eso.

---

## 1. Panorama general: quién hace qué

Tres clases, tres responsabilidades bien separadas:

| Clase | Responsabilidad | Analogía |
|---|---|---|
| `MainProgram` | Arranca la app, muestra el menú, elige un ejercicio y lo ejecuta. | El **recepcionista** |
| `Exercise` | Define *cómo se comporta* cualquier ejercicio (el bucle, el Scanner, el "¿repetir?"). No sabe *qué* hace ninguno en particular. | El **molde** |
| `TestExercise` | Un ejercicio concreto. Rellena el hueco que dejó el molde. | La **pieza fundida** |

Flujo de llamadas:

```
main()
  └─> new MainProgram().run()
        └─> while (running)
              ├─> selectExercise(scanner)   ← asigna this.exercise
              └─> exercise.run()            ← definido en Exercise
                    └─> while (running)
                          └─> exerciseLogic()   ← definido en TestExercise
```

Fijate en el detalle importante: **hay dos bucles anidados y dos variables `running` distintas**.

- El `running` de `MainProgram` controla si la aplicación sigue viva.
- El `running` de `Exercise` controla si seguís dentro de un ejercicio.

Salir del ejercicio (`running = false` en `Exercise`) te devuelve al menú. Salir del menú (`running = false` en `MainProgram`) termina el programa. Son variables independientes en objetos distintos; no se pisan.

---

## 2. Paso 0 — Preparar el proyecto en Eclipse

1. `File > New > Java Project`. Nombre exacto: `Parcial 1 - Grupo XX` (reemplazá XX por el número de tu equipo).
2. Click derecho sobre `src` > `New > Package`. Nombre: `application`.
3. Dentro del package, creá las tres clases.

**¿Por qué un package?** Porque `application` se convierte en el *namespace* de tus clases. Sin package, todo cae en el "default package", que Java tolera pero que impide que otras clases (de otros packages) importen las tuyas. Además todas las clases van a arrancar con la línea `package application;` — esa línea no es decorativa: le dice al compilador en qué carpeta física vive el `.class`.

**Orden de creación recomendado:** `Exercise` → `TestExercise` → `MainProgram`. No es caprichoso: `TestExercise` no compila sin `Exercise`, y `MainProgram` no compila sin las otras dos. Construir de abajo hacia arriba te evita ver 20 errores rojos que no significan nada.

---

## 3. La clase `Exercise` — el molde abstracto

```java
package application;

import java.util.Scanner;

public abstract class Exercise {

    protected boolean running = true;
    protected int currentPhase = 1;
    protected Scanner scanner;

    public Exercise(Scanner scanner) {
        this.scanner = scanner;
    }

    public void run() {
        running = true;
        currentPhase = 1;
        while (running) {
            exerciseLogic();
        }
    }

    protected abstract void exerciseLogic();

    protected void repeatOperationCheck(String promptMessage) {
        System.out.println(promptMessage);
        System.out.print("¿Desea repetir la operación? (s/n): ");
        String answer = scanner.nextLine().trim().toLowerCase();

        while (!answer.equals("s") && !answer.equals("n")) {
            System.out.print("Opción inválida. Ingrese 's' o 'n': ");
            answer = scanner.nextLine().trim().toLowerCase();
        }

        if (answer.equals("n")) {
            running = false;
        } else {
            currentPhase = 1;
        }
    }
}
```

### Línea por línea

**`public abstract class Exercise`**
`abstract` significa: *esta clase no se puede instanciar*. `new Exercise(scanner)` es error de compilación. Y tiene todo el sentido: ¿qué haría un "ejercicio genérico"? Nada, porque no tiene lógica propia. La clase existe únicamente para que otras hereden de ella. Es el corazón del TP, que es un TP de herencia.

**`protected boolean running = true;`**
`protected` en lugar de `private` porque las subclases **necesitan** poder escribirla (para cortar el bucle y volver al menú). Con `private` la heredarían pero no podrían tocarla. Se inicializa en `true` porque cuando arranca un ejercicio, por definición, está corriendo.

**`protected int currentPhase = 1;`**
Un contador de "en qué etapa del ejercicio estoy". En `TestExercise` casi no se usa, pero está previsto para los TPs siguientes, donde un ejercicio puede tener varias fases (pedir datos → procesar → mostrar resultados). Es una pequeña **máquina de estados**: en vez de anidar `if`s infinitos, el `exerciseLogic()` puede hacer `switch (currentPhase)`.

**`protected Scanner scanner;`**
No se inicializa acá. Se recibe. Ver el punto siguiente.

**`public Exercise(Scanner scanner) { this.scanner = scanner; }`**
Esta es la decisión de diseño más importante del TP y la que más te pueden preguntar.

El ejercicio **no crea su propio `Scanner`**: lo recibe desde afuera. Esto se llama *inyección de dependencias*. ¿Por qué?

- `System.in` es un único flujo de entrada compartido por todo el programa. Si varias clases crean su propio `new Scanner(System.in)`, se pelean por leer el mismo buffer y perdés líneas.
- Cerrar un `Scanner` construido sobre `System.in` **cierra `System.in` para siempre**. Si `TestExercise` cerrara el suyo, al volver al menú `MainProgram` reventaría con `NoSuchElementException`.

Regla: **el que crea el `Scanner` es el único que lo cierra**. Acá lo crea y lo cierra `MainProgram`. `Exercise` solo lo usa prestado.

`this.scanner` es obligatorio porque el parámetro se llama igual que el campo. Sin `this.`, estarías asignando el parámetro a sí mismo (error clásico, y el compilador no te avisa).

**`public void run()`**
El bucle principal del ejercicio. Es `public` porque `MainProgram` lo llama desde afuera.

Las dos primeras líneas (`running = true; currentPhase = 1;`) son un agregado defensivo que la consigna no pide explícitamente, pero razonalo: si el usuario entra al ejercicio, sale (dejando `running = false`) y vuelve a entrar eligiendo la misma opción del menú, el `while` no se ejecutaría nunca y el ejercicio se "colgaría" en silencio. Reiniciar el estado al entrar lo resuelve. (En este TP no ocurre porque `selectExercise` crea un objeto nuevo cada vez, pero es buena práctica y demuestra que entendiste el ciclo de vida del objeto.)

**`protected abstract void exerciseLogic();`**
Método abstracto: **declarado pero sin cuerpo**. Ojo con el punto y coma final y con que **no lleva llaves** — si escribís `{}` deja de ser abstracto y pasa a ser un método vacío.

Su existencia obliga a que toda subclase lo implemente (si no, la subclase también tiene que ser abstracta). Este patrón —una clase base que define el algoritmo general y delega los pasos variables a las subclases— es el **Template Method**. `run()` es la plantilla; `exerciseLogic()` es el hueco.

Es `protected` y no `public` porque solo lo llama la propia jerarquía (`run()` desde la clase base). Nadie de afuera debería invocar la lógica salteándose el bucle.

**`repeatOperationCheck(String promptMessage)`**
Es el mecanismo por el cual el usuario sale del ejercicio. Vive en la clase base porque **todos** los ejercicios lo van a necesitar: escribirlo una vez acá evita repetirlo en cada subclase (principio DRY).

- `scanner.nextLine()` lee la línea completa, incluido el `Enter`. Devuelve `String`, así que nunca lanza excepción por más raro que escriba el usuario.
- `.trim()` saca espacios accidentales; `.toLowerCase()` hace que `"S"` y `"s"` valgan lo mismo.
- El `while` interno cumple con "si el usuario entra un texto inválido, repite" — no avanza hasta que la respuesta sea válida.
- Compará strings con `.equals()`, **nunca con `==`**. `==` compara referencias de memoria, no contenido; con entrada de teclado te va a dar `false` aunque el texto sea idéntico.
- `running = false` es la única línea que efectivamente corta el bucle de `run()`.

---

## 4. La clase `TestExercise` — la pieza concreta

```java
package application;

import java.util.Scanner;

public class TestExercise extends Exercise {

    public TestExercise(Scanner scanner) {
        super(scanner);
    }

    @Override
    protected void exerciseLogic() {
        System.out.println("\n=== Bienvenido al ejercicio de prueba ===");
        System.out.println("Este ejercicio existe solo para verificar que el programa funciona.");
        System.out.println("Fase actual: " + currentPhase);

        repeatOperationCheck("Fin de la operación de prueba.");
    }
}
```

### Línea por línea

**`extends Exercise`**
Acá aparece la herencia. `TestExercise` recibe gratis: `running`, `currentPhase`, `scanner`, `run()` y `repeatOperationCheck()`. No escribe ni una línea de esa infraestructura. Eso es exactamente lo que se evalúa en este TP.

**`public TestExercise(Scanner scanner) { super(scanner); }`**
Los constructores **no se heredan**. Como `Exercise` no tiene constructor sin parámetros, Java no puede llamarlo implícitamente, y estás obligado a declarar un constructor que haga `super(scanner)`.

`super(scanner)` invoca el constructor del padre, que es el que realmente guarda la referencia. Tiene que ser la **primera línea** del constructor: el objeto padre debe estar completamente construido antes de que el hijo empiece a inicializar lo suyo.

**`@Override`**
No es obligatoria pero ponela siempre. Es una anotación que le pide al compilador: "verificá que este método realmente sobrescriba uno del padre". Si te equivocás en el nombre o la firma (`exerciseLogic()` vs `exercicelogic()`), te avisa en vez de crear silenciosamente un método nuevo que nunca se ejecuta.

**El cuerpo**
Cumple lo que pide la consigna: da la bienvenida y ofrece volver al menú. La bienvenida se imprime en cada vuelta del bucle porque `exerciseLogic()` se llama repetidamente — es el comportamiento esperado acá, pero tenelo presente para los TPs futuros, donde vas a querer usar `currentPhase` para no repetir la introducción.

---

## 5. La clase `MainProgram` — el punto de entrada

```java
package application;

import java.util.Scanner;

public class MainProgram {

    private boolean running = true;
    private Exercise exercise;

    public static void main(String[] args) {
        MainProgram program = new MainProgram();
        program.run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);

        while (running) {
            selectExercise(scanner);

            if (exercise != null) {
                exercise.run();
            }
        }

        scanner.close();
        System.out.println("El programa ha finalizado.");
    }

    private void selectExercise(Scanner scanner) {
        exercise = null;
        boolean validOption = false;

        while (!validOption) {
            System.out.println("\n===== MENÚ PRINCIPAL =====");
            System.out.println("1 - Ejercicio de prueba");
            System.out.println("0 - Salir");
            System.out.print("Seleccione una opción: ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    exercise = new TestExercise(scanner);
                    validOption = true;
                    break;
                case "0":
                    running = false;
                    validOption = true;
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
    }
}
```

### Línea por línea

**`private boolean running = true;` y `private Exercise exercise;`**
Acá sí `private`, porque nadie hereda de `MainProgram` ni necesita tocar su estado.

Fijate en el **tipo declarado**: `Exercise`, no `TestExercise`. Esto es **polimorfismo** y es el segundo concepto clave del TP. La variable puede apuntar a *cualquier* subclase de `Exercise`. Cuando `MainProgram` escribe `exercise.run()`, no sabe ni le importa qué ejercicio es: Java resuelve en tiempo de ejecución cuál `exerciseLogic()` ejecutar. Por eso agregar el TP 02 se reduce a sumar un `case` al `switch`; `run()` no se toca nunca más. Eso es el "material reutilizable" que menciona la consigna.

**`public static void main(String[] args)`**
Es la firma que busca la JVM para arrancar. Es `static`, o sea que **pertenece a la clase, no a un objeto**. Y ahí está el problema que la consigna te hace resolver: un método estático no puede acceder a `running` ni a `exercise`, porque esos campos solo existen dentro de una instancia.

Por eso `main` hace únicamente dos cosas: **crear un objeto** (`new MainProgram()`) y **pedirle que trabaje** (`program.run()`). A partir de esa línea ya estás en contexto de instancia y tenés acceso a todo. Mantener `main` mínimo es una convención muy extendida en Java.

**`private void run()`**
`private` porque solo lo llama `main`, que está en la misma clase. No hay razón para exponerlo.

`new Scanner(System.in)` se crea **una sola vez, acá**, y se pasa hacia abajo. Es el dueño del recurso.

`selectExercise(scanner)` se le pasa el scanner por parámetro (así lo pide la consigna) aunque podría accederlo de otra forma. Ventaja: el método declara explícitamente lo que necesita para funcionar.

**`if (exercise != null)`**
Esta guarda es imprescindible y es el error más común del TP. Pensá la secuencia: el usuario elige `0`, `selectExercise` pone `running = false` y **no asigna ningún ejercicio**. Si después llamaras `exercise.run()` sin verificar, obtendrías un `NullPointerException` justo al salir del programa. El chequeo evita ejecutar un ejercicio que nunca se eligió.

(Alternativa igual de válida: `if (running) { exercise.run(); }`. Elegí una y sabé explicar por qué.)

**`scanner.close();`**
Después del bucle, cuando ya nadie va a leer. Libera el recurso. Si tu IDE te marca un warning de "resource leak" cuando no cerrás un `Scanner`, es exactamente esto lo que te está reclamando.

**`selectExercise` — `exercise = null;` al inicio**
Limpia la elección anterior. Sin esto, si el usuario elige `0` para salir, la variable seguiría apuntando al ejercicio de la vuelta anterior y el `if (exercise != null)` lo dejaría pasar: entrarías otra vez al ejercicio en lugar de terminar. Es un bug sutil y difícil de encontrar.

**`String input = scanner.nextLine();` en lugar de `nextInt()`**
Decisión deliberada. `nextInt()` tiene dos problemas:

1. Si el usuario escribe `"hola"`, lanza `InputMismatchException` y el programa muere. Cumplir con "si el usuario entra un texto inválido, repite la selección" te obligaría a envolver todo en `try/catch`.
2. `nextInt()` consume el número pero **deja el `\n` en el buffer**. El siguiente `nextLine()` lee ese `\n` sobrante y devuelve una cadena vacía sin esperar al usuario. Es el bug número uno de los principiantes con `Scanner` en Java.

Leyendo siempre con `nextLine()` y comparando strings, ninguno de los dos problemas existe. Si preferís trabajar con enteros, la conversión segura es `Integer.parseInt(input)` dentro de un `try/catch (NumberFormatException e)`.

**`switch (input)`**
Java 7 en adelante permite `switch` sobre `String`. Cada `case` necesita su `break`, si no cae en el siguiente (*fall-through*). El `default` cubre el requisito de entrada inválida: informa y, como `validOption` sigue en `false`, el `while` vuelve a mostrar el menú.

**`new TestExercise(scanner)`** le pasa el mismo `Scanner` del programa principal, cerrando el circuito que describimos en la sección 3.

---

## 6. Traza completa de una ejecución

Seguí esto mentalmente (o con el debugger de Eclipse, poniendo un breakpoint en cada método):

| # | Qué pasa | Estado |
|---|---|---|
| 1 | JVM llama a `main` | — |
| 2 | `new MainProgram()` | `running=true`, `exercise=null` |
| 3 | `run()` crea el `Scanner` | — |
| 4 | Entra al `while` (running es true) | — |
| 5 | `selectExercise` muestra el menú | — |
| 6 | Usuario escribe `xyz` | `default` → mensaje de error, repite |
| 7 | Usuario escribe `1` | `exercise = new TestExercise(scanner)` |
| 8 | Vuelve a `run()`, `exercise != null` → `exercise.run()` | `running` del ejercicio = true |
| 9 | `run()` de `Exercise` llama a `exerciseLogic()` | polimorfismo: ejecuta el de `TestExercise` |
| 10 | Se imprime la bienvenida, se llama a `repeatOperationCheck` | — |
| 11 | Usuario escribe `s` | `running` del ejercicio sigue true → vuelve al paso 9 |
| 12 | Usuario escribe `n` | `running` del ejercicio = false → sale del bucle |
| 13 | Vuelve al bucle de `MainProgram` | `running` de MainProgram sigue true |
| 14 | Menú otra vez, usuario escribe `0` | `running` de MainProgram = false, `exercise = null` |
| 15 | `if (exercise != null)` es falso → no ejecuta nada | — |
| 16 | Sale del `while`, cierra el `Scanner`, imprime el mensaje final | Fin |

Si algo en tu programa no coincide con esta traza, el problema está en el paso donde se desvía.

---

## 7. Errores frecuentes (revisá esto antes de entregar)

- **`NullPointerException` al salir** → falta el `if (exercise != null)`.
- **El programa se cuelga sin pedir nada** → mezclaste `nextInt()` con `nextLine()`.
- **`NoSuchElementException`** → cerraste el `Scanner` en algún lado que no sea el final de `run()`.
- **El ejercicio no arranca al segundo intento** → `running` quedó en `false` (por eso el reset en `Exercise.run()`).
- **`Cannot instantiate the type Exercise`** → intentaste `new Exercise(...)`. Es abstracta, esa es la idea.
- **`Implicit super constructor Exercise() is undefined`** → falta `super(scanner)` en el constructor de `TestExercise`.
- **Comparar con `==` en vez de `.equals()`** → el `if` nunca entra.
- **Método abstracto con `{}`** → deja de ser abstracto y no obliga a la subclase.

---

## 8. Preguntas de defensa probables

Tené la respuesta lista para estas:

1. **¿Por qué `main` es `static` y por qué crea una instancia en lugar de hacer todo ahí?**
   Porque la JVM necesita invocarlo sin tener un objeto, y un método estático no puede acceder a campos de instancia como `running` o `exercise`.

2. **¿Por qué `Exercise` es abstracta?**
   Porque define el comportamiento común pero le falta la lógica concreta. Instanciarla no tendría sentido, y `abstract` hace que el compilador lo impida.

3. **¿Por qué el `Scanner` se pasa por constructor y no se crea en cada clase?**
   `System.in` es un recurso único y compartido; cerrarlo en una clase lo inutiliza para todas. Un solo dueño (`MainProgram`) crea y cierra.

4. **¿Qué diferencia hay entre los dos `running`?**
   Son campos de objetos distintos: uno controla la vida de la aplicación, el otro la del ejercicio en curso.

5. **¿Qué pasa si agrego el TP 02?**
   Creo una clase que extienda `Exercise`, implemento `exerciseLogic()` y agrego un `case` al `switch`. Nada más cambia: eso es polimorfismo.

6. **¿Para qué sirve `currentPhase` si acá casi no se usa?**
   Para que un ejercicio con varias etapas pueda controlarlas con un `switch` en vez de anidar condicionales.
