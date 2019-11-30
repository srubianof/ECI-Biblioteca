/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.cvds.managedbeans;

import com.google.inject.Inject;
import edu.eci.cvds.entities.CustomScheduleEvent;
import edu.eci.cvds.entities.EstadoReserva;
import edu.eci.cvds.entities.Recurso;
import edu.eci.cvds.entities.Reserva;
import edu.eci.cvds.entities.TipoReserva;
import edu.eci.cvds.services.BibliotecaException;
import edu.eci.cvds.services.BibliotecaServices;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import static org.primefaces.behavior.validate.ClientValidator.PropertyKeys.event;

import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 * @author Ing Pipe
 */
@ManagedBean(name = "reservaBean", eager = true)
@SessionScoped
public class ReservaBean extends BasePageBean implements Serializable {

    //    @ManagedProperty(value = "#{param.recursoID}")
    private int recursoID = 0;
    private static final long serialVersionUID = 3594009161252782831L;

    @Inject
    private BibliotecaServices serviciosBiblioteca;

    private ScheduleModel eventModel = new DefaultScheduleModel();
    private ScheduleEvent event = new DefaultScheduleEvent();
    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private TipoReserva frecuencia;
    private String duracion;
    private String fretiempo;
    private Reserva reserva;

    public int getRecursoID() {
        return recursoID;
    }

    public void setRecursoID(int recursoID) {
        this.recursoID = recursoID;
    }

    public TipoReserva getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(TipoReserva frecuencia) {
        this.frecuencia = frecuencia;
    }

    private int datee;

    public TipoReserva[] getTipos() {
        return TipoReserva.values();
    }

    public int getDatee() {
        return datee;
    }

    public void setDatee(int datee) {
        this.datee = datee;
    }

    public void crearEvento(Date start, Date end, String usuario, int idRecurso, String recurrencia, String duracion) throws BibliotecaException {

        start.setYear(start.getYear() + 2000);
        Date dateActual = new Date();
        duracion = duracion.replaceAll("\\D+", "");
        int numero = Integer.parseInt(duracion);
        int a = Integer.parseInt(recurrencia);
        if (numero == 1) {
            end = sumaFecha(end, TipoReserva.Ninguno);
        } else if (numero == 2) {
            end = sumaFecha(end, TipoReserva.Ninguno);
            end = sumaFecha(end, TipoReserva.Ninguno);
        }
        if (validarInsercionFechas(start, end, idRecurso)) {
            serviciosBiblioteca.registrarReserva(new Reserva(usuario, idRecurso, frecuencia.toString()+ " -> " + 0, dateActual, start, end, false, this.frecuencia,EstadoReserva.EnCurso,a));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "PAILAASSSS KRAKKK", null));
        }

        for (int i = a; i > 1; i--) {
            start = sumaFecha(start, this.frecuencia);
            end = sumaFecha(end, this.frecuencia);
            if (validarInsercionFechas(start, end, idRecurso)) {
                  int ii=i-1;
                  int iii = a-ii;
                serviciosBiblioteca.registrarReserva(new Reserva(usuario, idRecurso, frecuencia.toString()+ " -> " +iii, dateActual, start, end, false, this.frecuencia,EstadoReserva.EnCurso,ii));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "PAILAASSSS KRAKKK", null));
                return;
            }
        }
    }
    
    public void calcularSiguienteAparicion(TipoReserva tipo, Date fecha, int frecuencia){
        Date siguienteFecha = sumaFecha(fecha, tipo);
    }

    private boolean validarInsercionFechas(Date start, Date end, int idrecurso) throws BibliotecaException {
        //Falta pensar si la horafin es menor
        List<Reserva> reservas = serviciosBiblioteca.listarReservasRecurso(idrecurso);
        for (Reserva res: reservas){
            System.out.println(start + " -> " + end + "Recurso -> " + res.getDataInicio() + "Recurso F -> "+ res.getDataFim());
            System.out.println(start.equals(res.getDataInicio()));
            System.out.println(end.equals(res.getDataFim()));
            
            if ((start.equals(res.getDataInicio())&& end.equals(res.getDataFim()))
                    || (start.after(res.getDataInicio())&& end.before(res.getDataFim())) ){
                System.out.println("FOKIU");
                return false;
            }
        }
        return true;
    }

    private Date sumaFecha(Date fecha, TipoReserva res) {
        dateFormat.format(fecha);
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        switch (res) {
            case Ninguno:
                c.add(Calendar.HOUR, 1);
                break;
            case Diario:
                c.add(Calendar.DATE, 1);
                break;
            case Semanal:
                c.add(Calendar.DATE, 7);
                break;
            case Mensual:
                c.add(Calendar.MONTH, 1);
                break;
        }
        Date result = c.getTime();
        dateFormat.format(result);
        return result;
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
    }

    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", sumaFecha((Date) selectEvent.getObject(), TipoReserva.Diario), sumaFecha((Date) selectEvent.getObject(), TipoReserva.Diario));

    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public void onEventMove(ScheduleEntryMoveEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

        addMessage(message);
    }

    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

        addMessage(message);
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void deleteEvent() {
        eventModel.deleteEvent(event);
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public void addEvent(String usuario) throws BibliotecaException {
        if (event.getId() == null) {
            //addEvent(String Usuario, int idRecurso,TipoReserva res, int Duracion)
            /* En esta linea se deben llamar al metodo recursion,con los parametros especificados
            es decir addEvent tambien deberia tener tipoReserva y Duracion todo depende de los campos 
            que los de vista deben hacer :V
             */
            int numero = Integer.parseInt(fretiempo);
            recursion(usuario, recursoID, frecuencia, numero);
        } else {
            eventModel.updateEvent(event);
        }

        event = new DefaultScheduleEvent();
    }

    public void recursion(String usuario, int idRecurso, TipoReserva res, int duracion) throws BibliotecaException {
        Date dateActual = new Date();
        if (validarInsercion(event, idRecurso)) {
            eventModel.addEvent(event);
            Date f = event.getStartDate();
            String duraacion = this.duracion.replaceAll("\\D+", "");
            int numero = Integer.parseInt(duraacion);
            if (numero == 1) {
                f = sumaFecha(f, TipoReserva.Ninguno);
            } else if (numero == 2) {
                f = sumaFecha(f, TipoReserva.Ninguno);
                f = sumaFecha(f, TipoReserva.Ninguno);
            }
            // Hecho por Santiago Rubiano :D
            serviciosBiblioteca.registrarReserva(new Reserva(usuario, idRecurso, event.getTitle(), dateActual, event.getStartDate(), f, false, res,EstadoReserva.EnCurso,numero));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "PAILAASSSS KRAKKK", null));
            return;
        }
        if (res != TipoReserva.Ninguno) {
            Date startDate;
            Date endDate;
            // Reserva Recursiva que es la Anterios + todas las reservas que faltan acorde a la duracion
            for (int i = duracion; i >1; i--) {
                startDate = sumaFecha(event.getStartDate(), res);
                endDate = sumaFecha(event.getEndDate(), res);
                String duraacion = this.duracion.replaceAll("\\D+", "");
                int numero = Integer.parseInt(duraacion);
                if (numero == 1) {
                    endDate = sumaFecha(endDate, TipoReserva.Ninguno);
                } else if (numero == 2) {
                    endDate = sumaFecha(endDate, TipoReserva.Ninguno);
                    endDate = sumaFecha(endDate, TipoReserva.Ninguno);
                }
                event = new DefaultScheduleEvent(event.getTitle() + " -> " + i, startDate, endDate);
                if (validarInsercion(event, idRecurso)) {
                    serviciosBiblioteca.registrarReserva(new Reserva(usuario, idRecurso, event.getTitle(), dateActual, startDate, endDate, false, res,EstadoReserva.EnCurso,i));
                    eventModel.addEvent(event);
                } else {
                    System.out.println("No se le inserta PERROOOOOOOOOOOOOOOO");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "PAILAASSSS KRAKKK", null));
                    return;
                }
            }
        }

    }

    private boolean validarInsercion(ScheduleEvent evento, int idrecurso) throws BibliotecaException {
        //Falta pensar si la horafin es menor
        List<Reserva> reservas = serviciosBiblioteca.listarReservasRecurso(idrecurso);
        for (Reserva res: reservas){
            if ((evento.getStartDate().equals(res.getDataInicio())&&evento.getEndDate().equals(res.getDataFim()))
                    || (evento.getStartDate().after(res.getDataInicio())&&evento.getEndDate().before(res.getDataFim())) ){
                return false;
            }
        }
        return true;
    }

    public void loadEvents() throws BibliotecaException {
        eventModel = new DefaultScheduleModel();
        List<Reserva> reservas = serviciosBiblioteca.listarReservasRecurso(recursoID);
        //Mouseky herramienta misteriosa 
        reservas.stream().map((reserva) -> {
            event = new DefaultScheduleEvent(reserva.getTitulo(), reserva.getDataInicio(), reserva.getDataFim());
            return reserva;
        }).forEachOrdered((_item) -> {
            eventModel.addEvent(event);
        });

    }

    public Reserva obtenerR() {
        try {
            return serviciosBiblioteca.getInfoReserva(recursoID, event.getStartDate(), event.getEndDate());
        } catch (BibliotecaException ex) {
            System.out.println("JIJIJIJI");
        }
        return null;
    }
    
    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getFretiempo() {
        return fretiempo;
    }

    public void setFretiempo(String fretiempo) {
        this.fretiempo = fretiempo;
    }
}
