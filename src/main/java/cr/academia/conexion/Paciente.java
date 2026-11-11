
package cr.academia.modelo;

public class Paciente {

    private String cedula;
    private String nombre;
    private String fechaNacimiento;
    private String genero;
    private String telefono;
    private String correo;
    private String tipoSangre;

    public Paciente(String cedula, String nombre,
                    String fechaNacimiento, String genero,
                    String telefono, String correo,
                    String tipoSangre) {

        this.cedula = cedula;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.telefono = telefono;
        this.correo = correo;
        this.tipoSangre = tipoSangre;
    }

    public String getCedula() { return cedula; }
    public String getNombre() { return nombre; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public String getGenero() { return genero; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public String getTipoSangre() { return tipoSangre; }
}
