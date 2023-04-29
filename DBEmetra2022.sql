Drop database if exists DBEmetra2022;
Create database DBEmetra2022;

Use DBEmetra2022;

Create table Vecinos(
	NIT varchar(15) not null,
    DPI bigint(13) not null,
    nombres varchar(100) not null,
    apellidos varchar(100) not null,
    direccion varchar(200) not null,
    municipalidad varchar(45) not null,
    codigoPostal int not null,
    telefono varchar(8) not null,
	primary key PK_NIT (NIT)
);

Create table Vehiculos(
	placa varchar(15) not null,
    marca varchar(45) not null,
    modelo varchar(45) not null,
    tipoDeVehiculo varchar(60) not null,
    Vecinos_NIT varchar(15) not null,
    primary key PK_placa (placa),
    constraint FK_Vehiculos_Vecinos foreign key (Vecinos_NIT) references Vecinos(NIT) 
);




-- -------------------------------------------- PROCEDIMIENTOS ALMACENADOS VECINOS ----------------------------------------
-- ---------------LISTAR--------------------
	DELIMITER $$
		Create procedure sp_ListarVecinos() 
			begin 
				Select 
                Ve.NIT,
                Ve.DPI,
                Ve.nombres,
                Ve.apellidos,
                Ve.direccion,
                Ve.municipalidad,
                Ve.codigoPostal,
                ve.telefono
                from vecinos Ve;
		End$$
    DELIMITER ;
    
    call sp_ListarVecinos() ;

-- ---------------GUARDAR-------------------
	DELIMITER $$
		Create procedure sp_AgregarVecino(in NIT varchar(15),  in DPI bigint(13),in nombres varchar(100) ,
		in apellidos varchar(100) ,in direccion varchar(200) , in municipalidad varchar(45) , in codigoPostal int ,
		telefono varchar(8) )
			begin 
				insert into vecinos(NIT,DPI,nombres,apellidos,direccion,municipalidad,codigoPostal, telefono)
					values(NIT,DPI,nombres,apellidos,direccion,municipalidad,codigoPostal, telefono);
		End$$
	DELIMITER ;
    
    call sp_AgregarVecino('123456789112345','1284568750101','Jefrey Eduardo','López Ampérez','zona 12','municipalidad de zona 12','45632112','45368975');
	call sp_AgregarVecino('455456789112345','1284568750101',' Eduardo',' Ampérez','zona 18','municipalidad de zona 12','45632112','45368975');
	call sp_AgregarVecino('321456789112345','1284568750101','Ste ','López ','zona 1','municipalidad de zona 12','45632112','45368975');
-- --------------ELIMINAR------------------
	DELIMITER $$
		Create procedure sp_EliminarVecino(in numNit varchar(15))
    
			begin 
				Delete from Vecinos 
                where NIT = numNit;
		End$$
    DELIMITER ;
    
    
    

-- ---------------BUSCAR -------------------
	DELIMITER $$
		Create procedure sp_BuscarVecino(in numNit varchar(15))
			begin 
				Select 
					Ve.NIT,
					Ve.DPI,
					Ve.nombres,
					Ve.apellidos,
					Ve.direccion,
					Ve.municipalidad,
					Ve.codigoPostal,
					ve.telefono
					from vecinos Ve 
                    where NIT = numNit;				
		End$$
    
    /*priva stage escena
    private scene escena
    
    this.escenario = escenarioP
    this.escenario.settitle("")
    escenarioprincipal.geticons.add()new image*/
    
    DELIMITER ;
    
    call sp_BuscarVecino (455456789112345);
-- ---------------Actualizar--------------------
	DELIMITER $$
		Create procedure sp_ActualizarVecino(in numNit varchar(15), in numDPI bigint(13), in venombres varchar(100) ,
		in veapellidos varchar(100) ,in direc varchar(200) , in muni varchar(45) , in codPostal int,
		tel varchar(8))
			begin 
				Update Vecinos 
					set
						DPI=numDPI,
						nombres = venombres,
						apellidos = veapellidos,
						direccion = direc,
						municipalidad = muni,
						codigoPostal = codPostal,
						telefono = tel
							where NIT = numNit;
				
		End$$
    DELIMITER ;
     call sp_actualizarVecino('455456789112345','1284568750101','Carlos','López ','zona 6','municipalidad de zona 14','45632112','45368975' );
    
    


-- -------------------------------------------- PROCEDIMIENTOS ALMACENADOS VEHICULOS ----------------------------------------
-- ---------------LISTAR--------------------
	DELIMITER $$
		Create procedure sp_ListarVehiculos() 
			begin 
				Select 
					veh.placa,
					veh.marca ,
					veh.modelo ,
					veh.tipoDeVehiculo ,
					veh.Vecinos_NIT 
					from vehiculos veh ;
		End$$
    DELIMITER ;
    
    call sp_ListarVehiculos() ;

-- ---------------GUARDAR-------------------
	DELIMITER $$
		Create procedure sp_AgregarVehiculo ( in placa varchar(15), in marca varchar(45) ,
		in modelo varchar(45), in tipoDeVehiculo varchar(60), in Vecinos_NIT varchar(15))
			begin 
				insert into vehiculos(placa, marca, modelo, tipoDeVehiculo, Vecinos_NIT)
					values(placa, marca, modelo, tipoDeVehiculo, Vecinos_NIT);
		End$$
	DELIMITER ;
    
    call sp_AgregarVehiculo('PD869XQ','Mazda','3','hashback','455456789112345');
	call sp_AgregarVehiculo('PD878XQ','Mazda','2','hashback','123456789112345');
	call sp_AgregarVehiculo('PD111XQ','Mazda','1','hashback','123456789112345');
-- --------------ELIMINAR------------------
	DELIMITER $$
		Create procedure sp_EliminarVehiculo(in numplaca varchar(15))
    
			begin 
				Delete from Vehiculos 
					where placa = numplaca;
		End$$
    DELIMITER ;
    

    
    

-- ---------------BUSCAR -------------------
	DELIMITER $$
		Create procedure sp_BuscarVehiculo (in numplaca varchar(15))
			begin 
				Select 
					veh.placa,
					veh.marca ,
					veh.modelo ,
					veh.tipoDeVehiculo ,
					veh.Vecinos_NIT 
						from vehiculos veh 
							where placa = numplaca;				
		End$$
    
    DELIMITER ;
    
    call sp_BuscarVehiculo('PD111XQ');
-- ---------------EDITAR--------------------
	DELIMITER $$
		Create procedure sp_EditarVehiculo(in numplaca varchar(15), in aumarca varchar(45) ,
		in aumodelo varchar(45), in autipoDeVehiculo varchar(60), in Veci_NIT varchar(15))
			begin 
				Update vehiculos 
					set
						marca = aumarca,
						modelo = aumodelo,
						tipoDeVehiculo = autipoDeVehiculo,
						Vecinos_NIT = Veci_NIT
							where placa = numplaca;
				
		End$$
    DELIMITER ;
    
    call sp_EditarVehiculo('PD111XQ','Mazda','88','convertible','321456789112345');

