
package cr.academia;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import cr.academia.modelo.Paciente;
import cr.academia.repositorio.PacienteRepository;

public class Main {

    public static void main(String[] args) {

        String uri = "mongodb://localhost:27017";

        try (MongoClient cliente = MongoClients.create(uri)) {

            MongoDatabase database =
                    cliente.getDatabase("SistemaPacientesCCSS");

            PacienteRepository repositorio =
                    new PacienteRepository(database);

            // DATOS FICTICIOS PARA PRUEBAS
            Paciente paciente = new Paciente(
                    "TEST001",
                    "Paciente de prueba",
                    "1995-06-02",
                    "Femenino",
                    "88888888",
                    "paciente.prueba@email.com",
                    "O+"
            );


            System.out.println("\n1. REGISTRAR PACIENTE");
            repositorio.registrarPaciente(paciente);


            System.out.println("\n2. LISTA DE PACIENTES");
            repositorio.listarPacientes();

            System.out.println("\n3. ACTUALIZAR TELEFONO");
            repositorio.actualizarTelefono(
                    "TEST001",
                    "70000000"
            );


            System.out.println("\n4. REGISTRAR CONSULTA");
            repositorio.registrarConsulta(
                    "TEST001",
                    "2026-11-11",
                    "Dr. Carlos Rodríguez",
                    "Medicina General",
                    "Dolor de cabeza",
                    "Migraña"
            );


            System.out.println("\n5. REGISTRAR RECETA");
            repositorio.registrarReceta(
                    "TEST001",
                    "2026-11-11",
                    "Paracetamol",
                    "500 mg",
                    "Cada 8 horas",
                    "3 días"
            );

            System.out.println("\n6. HISTORIAL MEDICO");
            repositorio.consultarHistorial("TEST001");


            System.out.println("\n7. DATOS ACTUALIZADOS");
            repositorio.listarPacientes();


        } catch (Exception e) {
            System.out.println("Error al ejecutar el sistema: "
                    + e.getMessage());
            e.printStackTrace();
        }
    }
}
