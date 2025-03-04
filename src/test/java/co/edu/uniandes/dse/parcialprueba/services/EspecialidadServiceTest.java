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

import co.edu.uniandes.dse.parcialprueba.entities.EspecialidadEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@DataJpaTest
@Transactional
@Import(EspecialidadService.class)
public class EspecialidadServiceTest 
{
    @Autowired
    private EspecialidadService especialidadService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<EspecialidadEntity> especialidadList = new ArrayList<>();

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
            EspecialidadEntity especialidad = factory.manufacturePojo(EspecialidadEntity.class);
            entityManager.persist(especialidad);
            especialidadList.add(especialidad);
        }
    }

    @Test
    public void testCreateEspecialidad() throws IllegalOperationException
    {
        EspecialidadEntity especialidad = factory.manufacturePojo(EspecialidadEntity.class);
        EspecialidadEntity result = especialidadService.createEspecialidad(especialidad);
        assertNotNull(result);
        EspecialidadEntity entity = entityManager.find(EspecialidadEntity.class, result.getId());
        assertEquals(especialidad.getNombre(), entity.getNombre());
    }

    @Test
    public void testGetEspecialidad()
    {
        EspecialidadEntity especialidad = factory.manufacturePojo(EspecialidadEntity.class);
        entityManager.persist(especialidad);
        especialidad.setDescripcion("k");
        assertThrows(IllegalOperationException.class, () -> especialidadService.createEspecialidad(especialidad));
    }

}
