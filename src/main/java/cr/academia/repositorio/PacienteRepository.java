
package cr.academia.repositorio;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import cr.academia.modelo.Paciente;

import java.util.ArrayList;
import java.util.List;

public class PacienteRepository {

    private final MongoCollection<Document> coleccion;

    public PacienteRepository(MongoDatabase database) {
        coleccion = database.getCollection("pacientes");
    }

    // REGISTRAR PACIENTE
    public void registrarPaciente(Paciente paciente) {

        Document filtro = new Document("cedula", paciente.getCedula());

        if (coleccion.find(filtro).first() != null) {
            System.out.println("El paciente ya está registrado.");
            return;
        }

        Document documento = new Document("cedula", paciente.getCedula())
                .append("nombre", paciente.getNombre())
                .append("fechaNacimiento", paciente.getFechaNacimiento())
                .append("genero", paciente.getGenero())
                .append("telefono", paciente.getTelefono())
                .append("correo", paciente.getCorreo())
                .append("tipoSangre", paciente.getTipoSangre())
                .append("consultas", new ArrayList<Document>());

        coleccion.insertOne(documento);
        System.out.println("Paciente registrado correctamente.");
    }

    // CONSULTAR PACIENTES
    public void listarPacientes() {

        for (Document paciente : coleccion.find()) {
            System.out.println("----------------------------");
            System.out.println("Cédula: " + paciente.getString("cedula"));
            System.out.println("Nombre: " + paciente.getString("nombre"));
            System.out.println("Fecha de nacimiento: " + paciente.getString("fechaNacimiento"));
            System.out.println("Género: " + paciente.getString("genero"));
            System.out.println("Teléfono: " + paciente.getString("telefono"));
            System.out.println("Correo: " + paciente.getString("correo"));
            System.out.println("Tipo de sangre: " + paciente.getString("tipoSangre"));
        }
    }

    // ACTUALIZAR TELEFONO
    public void actualizarTelefono(String cedula, String nuevoTelefono) {

        Document filtro = new Document("cedula", cedula);

        Document actualizacion = new Document(
                "$set",
                new Document("telefono", nuevoTelefono)
        );

        var resultado = coleccion.updateOne(filtro, actualizacion);

        if (resultado.getMatchedCount() > 0) {
            System.out.println("Paciente actualizado correctamente.");
        } else {
            System.out.println("No se encontró el paciente.");
        }
    }

    // ELIMINAR PACIENTE
    public void eliminarPaciente(String cedula) {

        Document filtro = new Document("cedula", cedula);

        var resultado = coleccion.deleteOne(filtro);

        if (resultado.getDeletedCount() > 0) {
            System.out.println("Paciente eliminado correctamente.");
        } else {
            System.out.println("No se encontró el paciente.");
        }
    }

    // REGISTRAR CONSULTA MEDICA
    public void registrarConsulta(
            String cedula,
            String fecha,
            String medico,
            String especialidad,
            String motivo,
            String diagnostico) {

        // Evitar registrar dos veces la misma consulta en una fecha
        Document filtro = new Document("cedula", cedula)
                .append("consultas.fecha", fecha);

        if (coleccion.find(filtro).first() != null) {
            System.out.println("Ya existe una consulta registrada en esa fecha.");
            return;
        }

        Document consulta = new Document("fecha", fecha)
                .append("medico", medico)
                .append("especialidad", especialidad)
                .append("motivo", motivo)
                .append("diagnostico", diagnostico)
                .append("recetas", new ArrayList<Document>());

        var resultado = coleccion.updateOne(
                new Document("cedula", cedula),
                new Document("$push", new Document("consultas", consulta))
        );

        if (resultado.getMatchedCount() > 0) {
            System.out.println("Consulta médica registrada correctamente.");
        } else {
            System.out.println("No se encontró el paciente.");
        }
    }

    // REGISTRAR RECETA MEDICA
    public void registrarReceta(
            String cedula,
            String fechaConsulta,
            String medicamento,
            String dosis,
            String frecuencia,
            String duracion) {

        Document filtro = new Document("cedula", cedula)
                .append("consultas.fecha", fechaConsulta);

        Document receta = new Document("medicamento", medicamento)
                .append("dosis", dosis)
                .append("frecuencia", frecuencia)
                .append("duracion", duracion);

        var resultado = coleccion.updateOne(
                filtro,
                new Document("$push",
                        new Document("consultas.$.recetas", receta))
        );

        if (resultado.getMatchedCount() > 0) {
            System.out.println("Receta registrada correctamente.");
        } else {
            System.out.println("No se encontró la consulta.");
        }
    }

    // CONSULTAR HISTORIAL MEDICO
    public void consultarHistorial(String cedula) {

        Document paciente = coleccion.find(
                new Document("cedula", cedula)
        ).first();

        if (paciente == null) {
            System.out.println("Paciente no encontrado.");
            return;
        }

        System.out.println("\n================================");
        System.out.println("HISTORIAL MEDICO");
        System.out.println("Paciente: " + paciente.getString("nombre"));
        System.out.println("Cédula: " + paciente.getString("cedula"));
        System.out.println("================================");

        List<Document> consultas =
                paciente.getList("consultas", Document.class);

        if (consultas == null || consultas.isEmpty()) {
            System.out.println("El paciente no tiene consultas registradas.");
            return;
        }

        for (Document consulta : consultas) {

            System.out.println("\n----------------------------");
            System.out.println("Fecha: " + consulta.getString("fecha"));
            System.out.println("Médico: " + consulta.getString("medico"));
            System.out.println("Especialidad: " + consulta.getString("especialidad"));
            System.out.println("Motivo: " + consulta.getString("motivo"));
            System.out.println("Diagnóstico: " + consulta.getString("diagnostico"));

            List<Document> recetas =
                    consulta.getList("recetas", Document.class);

            System.out.println("\nRECETAS MEDICAS:");

            if (recetas == null || recetas.isEmpty()) {
                System.out.println("No hay recetas registradas.");
            } else {
                for (Document receta : recetas) {
                    System.out.println("Medicamento: "
                            + receta.getString("medicamento"));
                    System.out.println("Dosis: "
                            + receta.getString("dosis"));
                    System.out.println("Frecuencia: "
                            + receta.getString("frecuencia"));
                    System.out.println("Duración: "
                            + receta.getString("duracion"));
                }
            }
        }
    }
}
