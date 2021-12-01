package com.appsmoviles.crudsqlite.objetos;

public class Caballero {
    private String id;
    private String nombre;
    private String lugarNacimiento;

    public Caballero(String id, String nombre, String lugarNacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.lugarNacimiento = lugarNacimiento;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLugarNacimiento() {
        return lugarNacimiento;
    }

    public void setLugarNacimiento(String lugarNacimiento) {
        this.lugarNacimiento = lugarNacimiento;
    }
}
