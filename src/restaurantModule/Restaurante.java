package restaurantModule;

/** Gestiona una mesa de cada capacidad y distribuye los grupos. */
public class Restaurante {
    private final Mesa mesaDos = new Mesa(2);
    private final Mesa mesaCuatro = new Mesa(4);

    public Mesa getMesa(int capacidad) {
        if (capacidad == 2) {
            return mesaDos;
        }
        if (capacidad == 4) {
            return mesaCuatro;
        }
        throw new IllegalArgumentException("Seleccione una mesa de 2 o 4 personas.");
    }

    public void registrar(String nombre, int personas) {
        Grupo grupo = new Grupo(nombre, personas);
        getMesa(personas <= 2 ? 2 : 4).anotar(grupo);
    }
}
