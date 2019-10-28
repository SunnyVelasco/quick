package com.example.marip.quick;

public class usuariobd {
    String usuarioid, usuario, correo, cp, numTel, password;
    public usuariobd(String usuarioid, String usuario ,String correo, String cp, String numTel, String password) {
        this.usuario = usuario;
        this.usuarioid = usuarioid;
        this.correo = correo;
        this.cp = cp;
        this.numTel = numTel;
        this.password = password;
    }


    public String getUsuarioid() {
        return usuarioid;
    }
    public String getUsuario(){

        return usuario;
    }

    public String getCorreo() {
        return correo;
    }

    public String getCp() {
        return cp;
    }

    public String getNumTel() {
        return numTel;
    }

    public String getPassword() {
        return password;
    }
}
