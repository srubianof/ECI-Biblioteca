package edu.eci.cvds.services;

import edu.eci.cvds.entities.EstadoReserva;
import edu.eci.cvds.entities.Recurso;
import edu.eci.cvds.entities.Reserva;
import edu.eci.cvds.entities.Usuario;
import java.util.Date;
import java.util.List;


public interface BibliotecaServices {

	public void registrarRecurso(Recurso rec) throws BibliotecaException;
        
        public List<Recurso> consultarRecursos()throws BibliotecaException;
        
        public List<Recurso> consultarRecursosDisponibles()throws BibliotecaException;
        
        public Recurso consultarRecurso(int id) throws BibliotecaException;
        
        public Usuario consultarUsuario(String correo) throws BibliotecaException;
        
        public void updateRecurso(int id, String nuevoEstado) throws BibliotecaException;
        
        public void registrarReserva(Reserva re) throws BibliotecaException;
        
        public void eliminarReserva(int re) throws BibliotecaException;
        
        public List<Reserva> listarReservasRecurso(int ru) throws BibliotecaException;
        
        public Reserva getInfoReserva(int recursoId, Date inicio, Date fin) throws BibliotecaException;
        
        public void modificarReserva(Reserva res, EstadoReserva estado) throws BibliotecaException; 
        
       
        

        
        
        
        

}
