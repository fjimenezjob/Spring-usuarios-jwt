package com.lluviadeideas.jwt.models.dao;

import com.lluviadeideas.jwt.models.entity.Cliente;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

//No es necesario poner @Component porque la clase crud ya forma parte de spring
public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long>{
    
    @Query("select c from Cliente c left join fetch c.facturas f where c.id =?1")
    public Cliente fetchByIdWithFacturas(Long id);

}