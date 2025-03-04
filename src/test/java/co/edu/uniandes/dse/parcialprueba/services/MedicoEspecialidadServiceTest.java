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
import co.edu.uniandes.dse.parcialprueba.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@DataJpaTest
@Transactional
@Import({MedicoService.class, EspecialidadService.class, MedicoEspecialidadService.class})
public class MedicoEspecialidadServiceTest
{
    @Autowired
    private MedicoEspecialidadService medicoEspecialidadService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private MedicoEntity medico = new MedicoEntity();
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
        medico = factory.manufacturePojo(MedicoEntity.class);
        entityManager.persist(medico);

        for (int i = 0; i < 3; i++) 
        {
            EspecialidadEntity especialidad = factory.manufacturePojo(EspecialidadEntity.class);
            entityManager.persist(especialidad);
            especialidadList.add(especialidad);
            especialidad.getMedicos().add(medico);
            medico.getEspecialidades().add(especialidad);
        
        }
    }

    @Test
    public void addEspecialidadTest() throws IllegalOperationException
    {
        EspecialidadEntity especialidad = factory.manufacturePojo(EspecialidadEntity.class);
        entityManager.persist(especialidad);
        medicoEspecialidadService.addEspecialidad(medico.getId(), especialidad.getId());
        MedicoEntity entity = entityManager.find(MedicoEntity.class, medico.getId());
        assertNotNull(entity);
        assertEquals(4, entity.getEspecialidades().size());
    }

    @Test
    public void addEspecialidadNoMedicoTest() throws IllegalOperationException
    {
        EspecialidadEntity especialidad = factory.manufacturePojo(EspecialidadEntity.class);
        assertThrows(IllegalOperationException.class, () -> medicoEspecialidadService.addEspecialidad(0L, especialidad.getId()));
    }

    @Test
    public void addEspecialidadNoEspecialidadTest() throws IllegalOperationException
    {
        assertThrows(IllegalOperationException.class, () -> medicoEspecialidadService.addEspecialidad(medico.getId(), 0L));
    }

}
