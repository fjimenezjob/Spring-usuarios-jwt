package com.lluviadeideas.jwt.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lluviadeideas.jwt.models.dao.IClienteDao;
import com.lluviadeideas.jwt.models.dao.IFacturaDao;
import com.lluviadeideas.jwt.models.dao.IProductoDao;
import com.lluviadeideas.jwt.models.entity.Cliente;
import com.lluviadeideas.jwt.models.entity.Factura;
import com.lluviadeideas.jwt.models.entity.Producto;

@Service
public class ClienteServiceImpl implements IClienteService {

    @Autowired
    private IClienteDao clienteDao;

    @Autowired
    private IProductoDao productoDao;

    @Autowired
    private IFacturaDao facturaDao;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return (List<Cliente>) clienteDao.findAll();
    }

    @Override
    @Transactional
    public void save(final Cliente cliente) {
        clienteDao.save(cliente);

    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findOne(final Long id) {
        return clienteDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(final Long id) {
        clienteDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> findAll(final Pageable pageable) {

        return clienteDao.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findByNombre(final String term) {

        return productoDao.findByNombreLikeIgnoreCase("%" + term + "%");
    }

    @Override
    @Transactional
    public void saveFactura(Factura factura) {
        facturaDao.save(factura);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto finProductoById(Long id) {

        return productoDao.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public Factura findFacturaById(Long id) {
        return facturaDao.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void deleteFacturaById(Long id) {
        facturaDao.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Factura fetchFacturaByIdWithClienteWithItemFacturaWithProducto(Long id) {

        return facturaDao.fetchByIdWithClienteWithItemFacturaWithProducto(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Cliente fetchByIdWithFacturas(Long id) {
        return clienteDao.fetchByIdWithFacturas(id);
    }
}