/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jchat;

/**
 *
 * @author pablonoguera
 */
import java.net.*;
import java.io.*;
import java.util.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Flujo extends Thread {
String nombre;
    Socket nsfd;
    DataInputStream FlujoLectura;
    DataOutputStream FlujoEscritura;

    public Flujo(Socket sfd) {
        nsfd = sfd;
        try {
            FlujoLectura = new DataInputStream(new BufferedInputStream(sfd.getInputStream()));
            FlujoEscritura = new DataOutputStream(new BufferedOutputStream(sfd.getOutputStream()));
        } catch (IOException ioe) {
            System.out.println("IOException(Flujo): " + ioe);
        }
    }

    public void run() {

    String nombre = "Usuario"; 
    DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm");
    try {
        nombre = FlujoLectura.readUTF(); 
    } catch (IOException e) {
        nombre = "Usuario";
    }
    Servidor.usuarios.add(this); 

    broadcast("[SERVIDOR] " + nombre + " se ha conectado");
        while (true) {
            try {
                String linea = FlujoLectura.readUTF();

                if (!linea.trim().isEmpty()) {
                    linea = "[" + LocalTime.now().format(formato) + "] [" + nombre + "]: " + linea;
                    broadcast(linea);
                }
            } catch (IOException ioe) {
                Servidor.usuarios.remove(this);
                broadcast("[SERVIDOR] " + nombre + " se ha desconectado");

                try {
                    nsfd.close();
                } catch (IOException e) {
                    System.out.println("Error cerrando socket");
                }

                break;
            }
        }
    }

    public void broadcast(String mensaje) {
        synchronized (Servidor.usuarios) {
            Enumeration e = Servidor.usuarios.elements();
            while (e.hasMoreElements()) {
                Flujo f = (Flujo) e.nextElement();
                try {
                    synchronized (f.FlujoEscritura) {
                        f.FlujoEscritura.writeUTF(mensaje);
                        f.FlujoEscritura.flush();
                    }
                } catch (IOException ioe) {
                    System.out.println("Error: " + ioe);
                }
            }
        }
    }
}
