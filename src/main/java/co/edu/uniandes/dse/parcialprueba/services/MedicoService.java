package co.edu.uniandes.dse.parcialprueba.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.parcialprueba.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialprueba.repositories.MedicoRepository;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class MedicoService 
{
    @Autowired
    private MedicoRepository medicoRepository;

    //Valide que el registro médico inicie solo con los caracteres "RM" (e.g., RM1745).
    public void validarRegistroMedico(String registroMedico) throws IllegalOperationException 
    {
        if (!registroMedico.startsWith("RM")) {
            throw new IllegalOperationException("El registro médico debe iniciar con RM");
        }
    }

    //Crear un médico
    @Transactional
    public MedicoEntity createMedico(MedicoEntity medico) throws IllegalOperationException 
    {
        log.info("Inicia proceso de creación de médico");
        validarRegistroMedico(medico.getRegistroMedico());
        MedicoEntity newMedico = medicoRepository.save(medico);
        log.info("Termina proceso de creación de médico");
        return newMedico;
    }
}
