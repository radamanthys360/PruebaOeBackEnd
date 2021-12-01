package com.beans;

import com.entidades.Productos;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

// beans de jsf y tambien cliente de web service se podria tener aparte un cliente generico para reutilizar
@ManagedBean(name = "ProductosWsBean")
@RequestScoped
public class ProductosWsBean {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/tiendaws/webresources/";//parametrizar

    public ProductosWsBean() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("productosServices");//parametrizar
    }

    /**
     * @param responseType Class representing the response
     * @param requestEntity request data@return response object (instance of responseType class)
     */
    public void postJson(Object requestEntity) throws ClientErrorException {
         webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    /**
     * @param responseType Class representing the response
     * @return response object (instance of responseType class)
     */
    public <T> T getJson(Class<T> responseType) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void close() {
        client.close();
    }
    
    private Long id;
    private String nombre;
    private String descripcion;
    private Date fecha;

    public Long getId() {
         return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }
    // busccar una manera mas optima de limpiar los estados
    private void reset(){
        this.id = null;
        this.nombre = null;
        this.descripcion = null;
        this.fecha = null;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    //procedimiento para guardar el registro
    public String guardar() {
        FacesMessage message = null;
        String outcome = null;
        Productos productos = new Productos();
        productos.setNombre(getNombre());
        productos.setDescripcion(getDescripcion());
        productos.setFecha(getFecha());
        this.postJson(productos);//buscar una forma de validar la respuesta de la operacion si esta bien con mensaje 200
        message = new FacesMessage("Guardado Correctamente.");
        outcome = "index";
        FacesContext.getCurrentInstance().addMessage(null, message);
        reset();
        return outcome;
    }
    
}
