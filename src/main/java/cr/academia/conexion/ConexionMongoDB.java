
package cr.academia.conexion;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class ConexionMongoDB {

    private static final String URI = "mongodb://localhost:27017";

    public static void main(String[] args) {

        try (MongoClient cliente = MongoClients.create(URI)) {

            MongoDatabase database =
                    cliente.getDatabase("SistemaPacientesCCSS");

            database.runCommand(new Document("ping", 1));

            System.out.println("¡Conexión con MongoDB exitosa!");

        } catch (Exception e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }
}
