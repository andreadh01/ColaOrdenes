/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package colaordenes;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 *
 * @author Federico Cirett Galán
 * @date   Sep 13, 2021
 */
class Cliente {
    String name;
    int id;

    public Cliente(int id, String name ) {
        this.name = name;
        this.id = id;
    }
    public void display(){
        System.out.println("Cliente:"+this.id);
        System.out.println("Nombre:"+this.name);
    }
}
class Articulo {
    int id;
    int quantity;
    String nombre;
    int precio;

    public Articulo(int id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Articulo(int id, String nombre, int precio){
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public void display(){
        System.out.println("     "+this.id+"|"+this.quantity);
    }
    public void displayArticulo(){
        System.out.println("     "+this.id+"|"+this.nombre+"|"+this.precio);
    }
}
class Orden {
    int id;
    Cliente cliente;
    Queue<Articulo> articulos;

    public Orden(int id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
        this.articulos = new LinkedList<Articulo>();
    }
    public void display() {
        System.out.println("Orden:"+this.id+"|"+this.cliente.name);
        if (this.articulos.isEmpty()== false) {
            Iterator muestra = this.articulos.iterator();
            while(muestra.hasNext()) {
                Articulo a = (Articulo) muestra.next();
                a.display();
            }
        }
    }
}

public class ColaOrdenes {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        if(args.length==2){
            Queue<String> cola1 = cargaArchivo(args[0]);
            despliega(cola1);
            Queue<Orden> ordenes = separaOrdenes(cola1);
            despliegaOrdenes(ordenes);

            Queue<String> cola2 = cargaArchivo(args[1]);
            despliega(cola2);
            Queue<Articulo> articulos = separaArticulos(cola2);
            despliegaArticulos(articulos);
        }
        else{
            System.out.println("Ingrese el nombre de los archivos como argumentos");
        }

    }
    public static Queue<String> cargaArchivo(String archivo){
        Queue<String> colaRegistros = new LinkedList<>();
        int contador = 0;
        try {
            Scanner scanfile = new Scanner (new File(archivo));
            // ciclo para cargar 
            while( scanfile.hasNext() == true) {
                String line = scanfile.next();
                if (contador > 0) {
                    if (line.isEmpty()){
                        continue;
                    }
                    colaRegistros.add(line);
                }
                contador++;
            }
        } catch (Exception e) {
          System.err.println(e.getMessage());
        }
        return colaRegistros;
    }
    public static void despliega(Queue<String> cola){
        System.out.println("Cola:"+cola);
    }
    public static Queue<Orden> separaOrdenes(Queue<String> queue){
        Queue<Orden> queueOrden = new LinkedList<>();
        ArrayList<Cliente> arrayClientes = new ArrayList<>();
        //ciclar en cola "queue"
        int id_cliente = 0;
        while (queue.isEmpty()==false) {
            //extraer valores
            String cadena = queue.remove();
            //separar los valores y crear objetos como Cliente y Articulo
            String[] particion = cadena.split(",");
            int id_orden = Integer.parseInt(particion[0]);
            String nombre= particion[1];
            int id_articulo = Integer.parseInt(particion[2]);
            int cantidad = Integer.parseInt(particion[3]);
            
            Cliente cliente = agregarCliente(arrayClientes,id_cliente,nombre);
            Articulo articulo = new Articulo(id_articulo, cantidad);
            //agregarlos a queueOrden
            queueOrden = agregarOrden(queueOrden, id_orden, cliente, articulo);
            
            id_cliente++;
        }
        return queueOrden;
    }

    public static Queue<Articulo> separaArticulos(Queue<String> queue){
        Queue<Articulo> queueArticulos = new LinkedList<>();
        //ciclar en cola "queue"
        while (queue.isEmpty()==false) {
            //extraer valores
            String cadena = queue.remove();
            //separar los valores y crear objetos como Cliente y Articulo
            String[] particion = cadena.split(",");
            int id_articulo = Integer.parseInt(particion[0]);
            String nombre= particion[1];
            int precio = Integer.parseInt(particion[2]);

            //agregarlos a queueOrden
            queueArticulos = agregarArticulo(queueArticulos, id_articulo, nombre, precio);
        }
        return queueArticulos;
    }

    public static Cliente agregarCliente(ArrayList<Cliente> array, int id, String nombre){
        Cliente c;
        for (int i = 0; i < array.size(); i++) {
            c = array.get(i);
            if (c.name.contentEquals(nombre)) {
                return c;
            }
        }
        c = new Cliente( id, nombre);
        array.add(c);
        return c;
    }

    public static Queue<Orden> agregarOrden(Queue<Orden> q, int id_orden, Cliente cliente, Articulo articulo){
        Queue<Orden> temp = new LinkedList<Orden>();
        Orden o;
        boolean orden_encontrada = false;
        // si la cola está vacía, agregar una orden
        if(q.isEmpty()== true){
            o = new Orden(id_orden, cliente);
            o.articulos.add(articulo);
            temp.add(o);
        } else {
            // si la cola tiene ordenes, buscar la orden y actualizarla, sino, crear la orden
            while(q.isEmpty()==false){
                o = q.remove();
                if (o.id== id_orden) {
                    // orden existe, actualizarla
                    o.articulos.add(articulo);
                    orden_encontrada = true;
                }
                temp.add(o);
            }
            //orden no existe, crear la orden
            if(orden_encontrada == false) {
                o = new Orden(id_orden,cliente);
                o.articulos.add(articulo);
                temp.add(o);
            }
        }
        return temp;
    }

    public static Queue<Articulo> agregarArticulo(Queue<Articulo> q, int id_articulo, String nombre, int precio){
        Queue<Articulo> temp = new LinkedList<Articulo>();
        Articulo articulo;
        boolean orden_encontrada = false;
        // si la cola está vacía, agregar una orden
        while(q.isEmpty()==false){
            articulo = new Articulo(id_articulo, nombre, precio);
            temp.add(articulo);
        }
        return temp;
    }

    public static void despliegaOrdenes(Queue<Orden> cola){
        Iterator iterador = cola.iterator();
        while(iterador.hasNext()){
            Orden o = (Orden) iterador.next();
            o.display();
        }
    }
    public static void despliegaArticulos(Queue<Articulo> cola){
        Iterator iterador = cola.iterator();
        while(iterador.hasNext()){
            Articulo articulo = (Articulo) iterador.next();
            articulo.displayArticulo();
        }
    }
}
