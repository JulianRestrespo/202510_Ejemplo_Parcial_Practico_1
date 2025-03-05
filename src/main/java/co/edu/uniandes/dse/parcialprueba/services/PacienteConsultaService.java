package co.edu.uniandes.dse.parcialprueba.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import co.edu.uniandes.dse.parcialprueba.entities.ConsultaMedicaEntity;
import co.edu.uniandes.dse.parcialprueba.entities.PacienteEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialprueba.repositories.ConsultaMedicaRepository;
import co.edu.uniandes.dse.parcialprueba.repositories.PacienteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class PacienteConsultaService 
{
    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultaMedicaRepository consultaRepository;

    @Transactional
    public ConsultaMedicaEntity addConsulta(Long idPaciente, Long idConsulta) throws EntityNotFoundException, IllegalOperationException
    {
        log.info("Inicia el proceso para asociar una consulta a un paciente");
        Optional<PacienteEntity> paciente=pacienteRepository.findById(idPaciente);
        if (!paciente.isPresent())
        {
            throw new EntityNotFoundException("El paciente con el id {"+idPaciente+"} no existe");
        }
        Optional<ConsultaMedicaEntity> consulta=consultaRepository.findById(idConsulta);
        if (!consulta.isPresent())
        {
            throw new EntityNotFoundException("La consulta con el id {"+idConsulta+"} no existe");
        }
        for (ConsultaMedicaEntity consultaRevisar: paciente.get().getConsultas())
        {
            Date date1= consultaRevisar.getFecha();
            Date date= consulta.get().getFecha();
            if (date1.equals(date))
            {
                throw new IllegalOperationException("El paciente tiene asignada otra cita a esa hora");
            }
        }

        paciente.get().getConsultas().add(consulta.get());
        log.info("Se ha asociado la consulta al paciente");
        return consulta.get();

    }

    @Transactional
    public List<ConsultaMedicaEntity> getConsultasProgramadas(Long idPaciente) throws EntityNotFoundException
    {
        log.info("Inicia el proceso consultar las citas programadas por el paciente");
        Optional<PacienteEntity> paciente=pacienteRepository.findById(idPaciente);
        if (!paciente.isPresent())
        {
            throw new EntityNotFoundException("El paciente con el id {"+idPaciente+"} no existe");
        }
        List<ConsultaMedicaEntity> consultas= new ArrayList<>();
        for (ConsultaMedicaEntity consultaRevisar: paciente.get().getConsultas())
        {
            Date date1= consultaRevisar.getFecha();
            if (date1.after(new Date()))
            {
                consultas.add(consultaRevisar);
            }
        }
        return consultas;

    }

}
