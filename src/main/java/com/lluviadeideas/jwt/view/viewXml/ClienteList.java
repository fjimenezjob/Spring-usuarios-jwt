package com.lluviadeideas.jwt.view.viewXml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.lluviadeideas.jwt.models.entity.Cliente;

@XmlRootElement(name ="clientesList")
public class ClienteList {

    @XmlElement(name ="cliente")
    public List<Cliente> clientes;

    public ClienteList(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public ClienteList() {}

    public List<Cliente> getClientes() {
        return clientes;
    }

}