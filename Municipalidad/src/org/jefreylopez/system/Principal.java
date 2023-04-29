/*
    Jefrey Eduardo López Ampérez 
    IN5AM
    2018422
    Fecha de creacion: 26/9/2022
    fecha de modificacion 

*/


package org.jefreylopez.system;

import java.awt.Image;
import java.io.InputStream;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jefreylopez.controller.MenuPrincipalController;
import org.jefreylopez.controller.VecinosController;
import org.jefreylopez.controller.VehiculosController;


public class Principal extends Application {
    private final String PAQUETE_VISTA = "/org/jefreylopez/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    
    @Override
     public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Municipalidad");
        ventanaVecinos ();
        escenarioPrincipal.show();
    }

    
     public void menuPrincipal(){
        try{
            MenuPrincipalController ventanaMenu = (MenuPrincipalController)cambiarEscena("MenuView.fxml",600,400);
            ventanaMenu.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
     
     public void ventanaVecinos(){
        try{
            VecinosController vecinos = (VecinosController)cambiarEscena("VecinosView.fxml",1342,552);
            vecinos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
     
     public void ventanaVehiculos(){
        try{
            VehiculosController vehiculos = (VehiculosController)cambiarEscena("VehiculosView.fxml",775,510);
            vehiculos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();        
        return resultado;
    }

}

    
    

    

