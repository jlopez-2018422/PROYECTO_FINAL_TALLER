package org.jefreylopez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javax.swing.JOptionPane;
import org.jefreylopez.bean.Vecinos;
import org.jefreylopez.conexion.Conexion;
import org.jefreylopez.system.Principal;

public class VecinosController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones {NUEVO,NINGUNO, ELIMINAR, EDITAR, GUARDAR, CANCELAR, ACTUALIZAR}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Vecinos> listaVecinos;
    @FXML private TableColumn colNIT; 
    @FXML private TableColumn colDPI;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApelldos;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colMunicipalidad;
    @FXML private TableColumn colCodigoPostal ;
    @FXML private TableColumn colTelefono;
    @FXML private TextField txtNIT;
    @FXML private TextField txtDPI;
    @FXML private TextField txtNombres;        
    @FXML private TextField txtApellidos;
    @FXML private TextField txtDireccion;    
    @FXML private TextField txtMunicipalidad;      
    @FXML private TextField txtCodigoPostal;
    @FXML private TextField txtTelefono;
    @FXML private TableView tblVecinos;
    @FXML private Button btnNuevo;      
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;       
            
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       cargarDatos();
    }
    
    
    public void seleccionarElemento(){
        if ((((Vecinos)tblVecinos.getSelectionModel().getSelectedItem()))!= null){
            txtNIT.setText((((Vecinos)tblVecinos.getSelectionModel().getSelectedItem()).getNIT()));
            txtDPI.setText(String.valueOf(((Vecinos)tblVecinos.getSelectionModel().getSelectedItem()).getDPI()));
            txtNombres.setText((((Vecinos)tblVecinos.getSelectionModel().getSelectedItem()).getNombres()));
            txtApellidos.setText((((Vecinos)tblVecinos.getSelectionModel().getSelectedItem()).getApellidos()));
            txtDireccion.setText((((Vecinos)tblVecinos.getSelectionModel().getSelectedItem()).getDireccion()));
            txtMunicipalidad.setText((((Vecinos)tblVecinos.getSelectionModel().getSelectedItem()).getMunicipalidad()));
            txtCodigoPostal.setText(String.valueOf(((Vecinos)tblVecinos.getSelectionModel().getSelectedItem()).getCodigoPostal()));
            txtTelefono.setText((((Vecinos)tblVecinos.getSelectionModel().getSelectedItem()).getTelefono()));
                
        }
    }
    
    public void cargarDatos(){
        tblVecinos.setItems(getVecinos());
        colNIT.setCellValueFactory(new PropertyValueFactory<Vecinos, String>("NIT"));
        colDPI.setCellValueFactory(new PropertyValueFactory<Vecinos, Integer>("DPI"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Vecinos,String>("nombres"));
        colApelldos.setCellValueFactory(new PropertyValueFactory<Vecinos,String>("apellidos"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Vecinos,String>("direccion"));
        colMunicipalidad.setCellValueFactory(new PropertyValueFactory<Vecinos,String>("municipalidad"));
        colCodigoPostal.setCellValueFactory(new PropertyValueFactory<Vecinos,Integer>("codigoPostal"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Vecinos,String>("telefono"));
        
    
    
    }
    
    
    public ObservableList<Vecinos> getVecinos(){
        ArrayList <Vecinos>lista = new ArrayList<Vecinos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarVecinos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Vecinos(resultado.getString("NIT"),
                                      resultado.getLong("DPI"),
                                      resultado.getString("nombres"),
                                      resultado.getString("apellidos"),
                                      resultado.getString("direccion"),
                                      resultado.getString("municipalidad"),
                                      resultado.getInt("codigoPostal"),
                                      resultado.getString("telefono")));
            }  
        }catch(Exception e){
            e.printStackTrace();
         }
    return listaVecinos = FXCollections.observableArrayList(lista);
    }   
   
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
   
   public void guardar(){
        Vecinos registro = new Vecinos();
        registro.setNIT(txtNIT.getText());
        registro.setDPI(Long.parseLong(txtDPI.getText()));
        registro.setNombres(txtNombres.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setDireccion(txtDireccion.getText());
        registro.setMunicipalidad(txtMunicipalidad.getText());
        registro.setCodigoPostal(Integer.parseInt(txtCodigoPostal.getText()));
        registro.setTelefono(txtTelefono.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarVecino(?,?,?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getNIT());
            procedimiento.setLong(2, registro.getDPI());
            procedimiento.setString(3, registro.getNombres());
            procedimiento.setString(4, registro.getApellidos());
            procedimiento.setString(5, registro.getDireccion());               
            procedimiento.setString(6, registro.getMunicipalidad());
            procedimiento.setInt(7, registro.getCodigoPostal());
            procedimiento.setString(8, registro.getTelefono());
            procedimiento.execute();
            listaVecinos.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
   
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                 desactivarControles();
                 limpiarControles();
                 btnNuevo.setText("Nuevo");
                 btnEliminar.setText("Eliminar");
                 btnEditar.setDisable(false);
                 btnReporte.setDisable(false);
                 tipoDeOperacion = operaciones.NINGUNO;
                 break;
            default:
                if(tblVecinos.getSelectionModel().getSelectedItem()!=null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Está seguro de eliminar el registro?", "Eliminar Vecino", JOptionPane.YES_NO_OPTION,
                             JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarVecino(?)}");
                             procedimiento.setString(1, ((Vecinos)tblVecinos.getSelectionModel().getSelectedItem()).getNIT());
                             procedimiento.execute();
                             listaVecinos.remove(tblVecinos.getSelectionModel().getFocusedIndex());
                             limpiarControles();
                         }catch(Exception e){
                             e.printStackTrace();
                         }
                         if(respuesta == JOptionPane.NO_OPTION){
                             limpiarControles();
                        }
                    }else{
                         limpiarControles();
                    }
                    
                }else{
                     JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }   
    }
    
     public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblVecinos.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                desactivarControles();
                limpiarControles();
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
     
     public void actualizar(){
        try{PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarVecino(?,?,?,?,?,?,?,?)}");
            Vecinos registro = (Vecinos)tblVecinos.getSelectionModel().getSelectedItem();
            registro.setNIT(txtNIT.getText());
            registro.setDPI(Long.parseLong(txtDPI.getText()));
            registro.setNombres(txtNombres.getText());
            registro.setApellidos(txtApellidos.getText());
            registro.setDireccion(txtDireccion.getText());
            registro.setMunicipalidad(txtMunicipalidad.getText());
            registro.setCodigoPostal(Integer.parseInt(txtCodigoPostal.getText()));
            registro.setTelefono(txtTelefono.getText());
            procedimiento.setString(1, registro.getNIT());
            procedimiento.setLong(2, registro.getDPI());
            procedimiento.setString(3, registro.getNombres());
            procedimiento.setString(4, registro.getApellidos());
            procedimiento.setString(5, registro.getDireccion());               
            procedimiento.setString(6, registro.getMunicipalidad());
            procedimiento.setInt(7, registro.getCodigoPostal());
            procedimiento.setString(8, registro.getTelefono());
            
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
     
     public void reporte(){
        switch(tipoDeOperacion){
            
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;  
        }
    }
     
     
   
   public void desactivarControles(){
    txtNIT.setEditable(false);
    txtDPI.setEditable(false);
    txtNombres.setEditable(false);        
    txtApellidos.setEditable(false);
    txtDireccion.setEditable(false);    
    txtMunicipalidad.setEditable(false);      
    txtCodigoPostal.setEditable(false);
    txtTelefono.setEditable(false);
       
   }
   
   public void activarControles(){
    txtNIT.setEditable(true);
    txtDPI.setEditable(true);
    txtNombres.setEditable(true);       
    txtApellidos.setEditable(true);
    txtDireccion.setEditable(true);    
    txtMunicipalidad.setEditable(true);      
    txtCodigoPostal.setEditable(true);
    txtTelefono.setEditable(true);  
       
   }
   
   public void limpiarControles(){
    txtNIT.clear();
    txtDPI.clear();
    txtNombres.clear();    
    txtApellidos.clear();
    txtDireccion.clear();;    
    txtMunicipalidad.clear();  
    txtCodigoPostal.clear();;
    txtTelefono.clear();;  
   }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }

    
    
    
    
}
