package co.edu.uniandes.dse.parcialprueba.services;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.parcialprueba.entities.ConsultaMedicaEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialprueba.repositories.ConsultaMedicaRepository;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class ConsultaMedicaService 
{
    @Autowired
    private ConsultaMedicaRepository consultaRepository;


    @Transactional
    public ConsultaMedicaEntity createConsultaMedica(ConsultaMedicaEntity consulta) throws IllegalOperationException
    {
        log.info("Inicia el proceso de creación de una consulta medica");
        Date date=consulta.getFecha();
        if (!date.after(new Date()))
        {
            throw new IllegalOperationException("La fecha que se ingreso es inválida");
        }
        ConsultaMedicaEntity nuevaConsulta=consultaRepository.save(consulta);
        log.info("La consulta se creó correctamente");
        return nuevaConsulta;
    }

}
