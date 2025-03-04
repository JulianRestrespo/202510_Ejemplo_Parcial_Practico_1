package co.edu.uniandes.dse.parcialprueba.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import co.edu.uniandes.dse.parcialprueba.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@DataJpaTest
@Transactional
@Import(MedicoService.class)
public class MedicoTest 
{
    @Autowired
    private MedicoService medicoService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<MedicoEntity> medicoList = new ArrayList<>();

    @BeforeEach
    public void setUp() 
    {
        clearData();
        insertData();
    }

    public void clearData() 
    {
        entityManager.getEntityManager().createQuery("delete from MedicoEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from EspecialidadEntity").executeUpdate();
    }

    public void insertData()
    {
        for (int i = 0; i < 3; i++) 
        {
            MedicoEntity medico = factory.manufacturePojo(MedicoEntity.class);
            entityManager.persist(medico);
            medicoList.add(medico);
        }
    }

    @Test
    public void testCreateMedicoTest() throws IllegalOperationException 
    {
        MedicoEntity medico = factory.manufacturePojo(MedicoEntity.class);
        medico.setRegistroMedico("RM1234");
        MedicoEntity result = medicoService.createMedico(medico);
        assertNotNull(result);
        MedicoEntity entity = entityManager.find(MedicoEntity.class, result.getId());
        assertNotNull(entity);
        assertEquals(medico.getNombre(), entity.getNombre());
        assertEquals(medico.getApellido(), entity.getApellido());
    }

    @Test
    public void testCreateMedicoFallido() 
    {
        MedicoEntity medico = factory.manufacturePojo(MedicoEntity.class);
        medico.setRegistroMedico("1234");
        assertThrows(IllegalOperationException.class, () -> medicoService.createMedico(medico));
    }
}