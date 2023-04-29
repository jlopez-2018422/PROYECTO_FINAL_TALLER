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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.jefreylopez.bean.Vecinos;
import org.jefreylopez.bean.Vehiculos;
import org.jefreylopez.conexion.Conexion;

import org.jefreylopez.system.Principal;

public class VehiculosController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones {NUEVO,NINGUNO, ELIMINAR, EDITAR, GUARDAR, CANCELAR, ACTUALIZAR}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Vehiculos> listaVehiculos;
    @FXML private TextField txtPlaca;
    @FXML private TextField txtMarca;
    @FXML private TextField txtModelo;        
    @FXML private TextField txtTipoDeVehiculo;
    @FXML private ComboBox cmbNIT;        
    @FXML private TableView tblVehiculos;
    @FXML private TableColumn colPlaca;
    @FXML private TableColumn colMarca;
    @FXML private TableColumn colModelo;
    @FXML private TableColumn colTipoDeVehiculo ;
    @FXML private TableColumn colNIT;
    @FXML private Button btnNuevo;      
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       cargarDatos();
    }
    
    public void cargarDatos(){
        tblVehiculos.setItems(getVehiculos());
        colPlaca.setCellValueFactory(new PropertyValueFactory<Vehiculos, String>("placa"));
        colMarca.setCellValueFactory(new PropertyValueFactory<Vehiculos, String>("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory<Vehiculos,String>("modelo"));
        colTipoDeVehiculo.setCellValueFactory(new PropertyValueFactory<Vehiculos,String>("tipoDeVehiculo"));
        colNIT.setCellValueFactory(new PropertyValueFactory<Vehiculos,String>("Vecinos_NIT"));
        
    }
    
    public ObservableList<Vehiculos> getVehiculos(){
    ArrayList <Vehiculos>lista = new ArrayList<Vehiculos>();
    try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarVehiculos()}");
        ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Vehiculos(resultado.getString("placa"),
                                        resultado.getString("marca"),
                                        resultado.getString("modelo"),
                                        resultado.getString("tipoDeVehiculo"),
                                        resultado.getString("Vecinos_NIT")));
            }
        }catch(Exception e){
        e.printStackTrace();
         }   
            return listaVehiculos = FXCollections.observableArrayList(lista);
    }
    
    
    public Vecinos buscarVecinos(String NIT){
        Vecinos resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarVecino(?)}");
            procedimiento.setString(1, NIT);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Vecinos(registro.getString("NIT"),
                                        registro.getLong("DPI"),
                                        registro.getString("nombres"),
                                        registro.getString("apellidos"),
                                        registro.getString("direccion"),
                                        registro.getString("municipalidad"),
                                        registro.getInt("codigoPostal"),
                                        registro.getString("telefono"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
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
        Vehiculos registro = new Vehiculos();
        registro.setPlaca(txtPlaca.getText());
        registro.setMarca(txtMarca.getText());
        registro.setModelo(txtModelo.getText());
        registro.setTipoDeVehiculo(txtTipoDeVehiculo.getText());
         registro.setVecinos_NIT(((Vecinos)cmbNIT.getSelectionModel().getSelectedItem()).getNIT());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarVehiculo(?,?,?,?,?)}");
            procedimiento.setString(1, registro.getPlaca());
            procedimiento.setString(2, registro.getMarca());
            procedimiento.setString(3, registro.getModelo());
            procedimiento.setString(4, registro.getTipoDeVehiculo());
            procedimiento.setString(5, registro.getVecinos_NIT());               
            procedimiento.execute();
            listaVehiculos.add(registro);
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
                if(tblVehiculos.getSelectionModel().getSelectedItem()!=null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Está seguro de eliminar el registro?", "Eliminar Vehículo", JOptionPane.YES_NO_OPTION,
                             JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarVehiculo(?)}");
                             procedimiento.setString(1, ((Vehiculos)tblVehiculos.getSelectionModel().getSelectedItem()).getPlaca());
                             procedimiento.execute();
                             listaVehiculos.remove(tblVehiculos.getSelectionModel().getFocusedIndex());
                             limpiarControles();
                         }catch(Exception e){
                             e.printStackTrace();
                         }
                         if(respuesta == JOptionPane.NO_OPTION){
                             limpiarControles();
                        }
                    }
                }else{
                     JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }   
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblVehiculos.getSelectionModel().getSelectedItem() != null){
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
        try{PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarVehiculo(?,?,?,?,?)}");
            Vehiculos registro = (Vehiculos)tblVehiculos.getSelectionModel().getSelectedItem();
            registro.setMarca(txtMarca.getText());
            registro.setModelo((txtModelo.getText()));
            registro.setTipoDeVehiculo(txtTipoDeVehiculo.getText());
            registro.setVecinos_NIT(((Vecinos)cmbNIT.getSelectionModel().getSelectedItem()).getNIT());
            registro.setPlaca(txtPlaca.getText());
            procedimiento.setString(1, registro.getMarca());
            procedimiento.setString(2, registro.getModelo());
            procedimiento.setString(3, registro.getTipoDeVehiculo());
            procedimiento.setString(4, registro.getVecinos_NIT());
            procedimiento.setString(5, registro.getPlaca());               
            
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
    txtPlaca.setEditable(false);
    txtMarca.setEditable(false);        
    txtModelo.setEditable(false);
    txtTipoDeVehiculo.setEditable(false);    
    cmbNIT.setDisable(true);      
   }
   
   public void activarControles(){
    txtPlaca.setEditable(true);
    txtMarca.setEditable(true);
    txtModelo.setEditable(true);       
    txtTipoDeVehiculo.setEditable(true);
    cmbNIT.setDisable(false);           
   }
   
   public void limpiarControles(){
    txtPlaca.clear();
    txtMarca.clear();
    txtModelo.clear();    
    txtTipoDeVehiculo.clear(); 
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
