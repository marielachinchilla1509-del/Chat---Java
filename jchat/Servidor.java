/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jchat;

/**
 *
 * @author pablonoguera
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor extends Thread
{
  public static Vector<Flujo> usuarios = new Vector<>();
  public static void main (String args[])
  {
    ServerSocket sfd = null;

try {
    sfd = new ServerSocket(Integer.parseInt(System.getenv("PORT")));
} catch (IOException ioe) {
    System.out.println("Comunicación rechazada." + ioe);
    System.exit(1);
}
        while (true)
    {
      try
      {
        Socket nsfd = sfd.accept();
        System.out.println("Conexion aceptada de: "+nsfd.getInetAddress());
	Flujo flujo = new Flujo(nsfd);
        flujo.start();
      }
      catch(IOException ioe)
      {
        System.out.println("Error: "+ioe);
      }
    }
  }
}


