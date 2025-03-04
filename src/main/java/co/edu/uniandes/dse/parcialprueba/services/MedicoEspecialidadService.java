package co.edu.uniandes.dse.parcialprueba.services;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.parcialprueba.entities.EspecialidadEntity;
import co.edu.uniandes.dse.parcialprueba.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialprueba.repositories.EspecialidadRepository;
import co.edu.uniandes.dse.parcialprueba.repositories.MedicoRepository;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class MedicoEspecialidadService 
{
    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EspecialidadRepository especialidadRepository;

    //agregar una especialidad a un médico
    @Transactional
    public MedicoEntity addEspecialidad(Long medicoId, Long especialidadId) throws IllegalOperationException 
    {
        log.info("Inicia proceso de agregar especialidad a médico");
        Optional<MedicoEntity> medico = medicoRepository.findById(medicoId);
        if (!medico.isPresent()) {
            throw new IllegalOperationException("El médico no existe");
        }
        Optional<EspecialidadEntity> especialidad = especialidadRepository.findById(especialidadId);
        if (!especialidad.isPresent()) {
            throw new IllegalOperationException("La especialidad no existe");
        }
        medico.get().getEspecialidades().add(especialidad.get());
        log.info("Termina proceso de agregar especialidad a médico");

        return medico.get();
    }

}
