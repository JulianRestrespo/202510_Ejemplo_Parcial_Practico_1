package co.edu.uniandes.dse.parcialprueba.services;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.parcialprueba.entities.ConsultaMedicaEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@DataJpaTest
@Transactional
@Import(ConsultaMedicaService.class)
public class ConsultaMedicaServiceTest 
{
    @Autowired
    private ConsultaMedicaService consultaMedicaService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private ConsultaMedicaEntity consulta;

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
        consulta= factory.manufacturePojo(ConsultaMedicaEntity.class);
        entityManager.persist(consulta);
    }

    @Test
    public void createConsultaMedicaTest() throws IllegalOperationException
    {
        ConsultaMedicaEntity consulta=factory.manufacturePojo(ConsultaMedicaEntity.class);
        Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date()); 
		calendar.add(Calendar.DATE, 15);
        consulta.setFecha(calendar.getTime());
        ConsultaMedicaEntity nuevaConsulta=consultaMedicaService.createConsultaMedica(consulta);

        assertNotNull(nuevaConsulta);

        ConsultaMedicaEntity resultado=entityManager.find(ConsultaMedicaEntity.class, nuevaConsulta.getId());

        assertEquals(nuevaConsulta.getId(), resultado.getId());
        assertEquals(nuevaConsulta.getCausa(), resultado.getCausa());   

    }
}
