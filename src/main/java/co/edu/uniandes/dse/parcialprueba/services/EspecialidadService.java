package co.edu.uniandes.dse.parcialprueba.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.parcialprueba.entities.EspecialidadEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialprueba.repositories.EspecialidadRepository;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class EspecialidadService 
{
    @Autowired
    private EspecialidadRepository especialidadRepository;

    //Valide que el la desprción tenga mínimo 10 letras.
    public void validarDescripcion(String descripcion) throws IllegalOperationException 
    {
        if (descripcion.length() < 10) {
            throw new IllegalOperationException("La descripción debe tener mínimo 10 letras");
        }
    }

    //Crear una especialidad
    @Transactional
    public EspecialidadEntity createEspecialidad(EspecialidadEntity especialidad) throws IllegalOperationException 
    {
        log.info("Inicia proceso de creación de especialidad");
        validarDescripcion(especialidad.getDescripcion());
        EspecialidadEntity newEspecialidad = especialidadRepository.save(especialidad);
        log.info("Termina proceso de creación de especialidad");
        return newEspecialidad;
    }


}
