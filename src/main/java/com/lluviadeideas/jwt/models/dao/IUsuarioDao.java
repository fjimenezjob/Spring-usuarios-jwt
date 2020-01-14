package com.lluviadeideas.jwt.models.dao;

import com.lluviadeideas.jwt.models.entity.Usuario;

import org.springframework.data.repository.CrudRepository;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {

    public Usuario findByUsername(String username);
}