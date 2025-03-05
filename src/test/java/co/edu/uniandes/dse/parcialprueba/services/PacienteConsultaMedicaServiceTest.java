package co.edu.uniandes.dse.parcialprueba.services;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.parcialprueba.entities.ConsultaMedicaEntity;
import co.edu.uniandes.dse.parcialprueba.entities.PacienteEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@DataJpaTest
@Transactional
@Import({PacienteConsultaService.class , PacienteService.class, ConsultaMedicaService.class})
public class PacienteConsultaMedicaServiceTest 
{
    @Autowired
    private PacienteConsultaService pacienteConsultaService;

    @Autowired
    private ConsultaMedicaService consultaService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private PacienteEntity paciente= new PacienteEntity();
    private List<ConsultaMedicaEntity> consultas= new ArrayList<>();

    @BeforeEach
    public void setUp() 
    {
        clearData();
        insertData();
    }

    public void clearData() 
    {
        entityManager.getEntityManager().createQuery("delete from ConsultaMedicaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PacienteEntity").executeUpdate();
    }

    public void insertData()
    {
        paciente= factory.manufacturePojo(PacienteEntity.class);
        entityManager.persist(paciente);

        for (int i = 0; i < 3; i++) 
        {
            ConsultaMedicaEntity conuslta= factory.manufacturePojo(ConsultaMedicaEntity.class);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date()); 
            calendar.add(Calendar.DATE, +40*(i+1));
            conuslta.setFecha(calendar.getTime());
            entityManager.persist(conuslta);
            conuslta.setPaciente(paciente);
            paciente.getConsultas().add(conuslta);
            consultas.add(conuslta);
        }     
    }
    @Test
    public void testAddConsulta() throws IllegalOperationException
    {
        ConsultaMedicaEntity consulta= factory.manufacturePojo(ConsultaMedicaEntity.class);
        consulta.setPaciente(paciente);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date("2025/12/12")); 
        consulta.setFecha(calendar.getTime());
        consultaService.createConsultaMedica(consulta);

        ConsultaMedicaEntity consultaAgregada=pacienteConsultaService.addConsulta(paciente.getId(), consulta.getId());

        assertNotNull(consultaAgregada);
        assertEquals(consulta.getId(), consultaAgregada.getId());
        assertEquals(consulta.getCausa(), consultaAgregada.getCausa());

    }

    @Test
    public void testAddConsultaConFechaMala() throws IllegalOperationException 
    {
        ConsultaMedicaEntity consulta= factory.manufacturePojo(ConsultaMedicaEntity.class);
        consulta.setPaciente(paciente);
        consulta.setFecha(consultas.get(0).getFecha());
        consultaService.createConsultaMedica(consulta);


        assertThrows(IllegalOperationException.class, ()-> pacienteConsultaService.addConsulta(paciente.getId(), consulta.getId()));
    }

    @Test
    public void getConsultasProgramadas()
    {
        List<ConsultaMedicaEntity> consultas=paciente.getConsultas();
        assertNotNull(consultas);

    }
    @Test
    public void getConsultasProgramadasMal()
    {
        PacienteEntity paciente= factory.manufacturePojo(PacienteEntity.class);
        List<ConsultaMedicaEntity> consultas=paciente.getConsultas();
        assertThrows(IllegalOperationException.class, ()-> pacienteConsultaService.getConsultasProgramadas(paciente.getId()));

    }

       
}
