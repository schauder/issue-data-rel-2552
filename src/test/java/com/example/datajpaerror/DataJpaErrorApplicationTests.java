package com.example.datajpaerror;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.example.datajpaerror.entity.CityEntity;
import com.example.datajpaerror.entity.CityPersonEntity;
import com.example.datajpaerror.entity.DocAcmeEntity;
import com.example.datajpaerror.entity.DocXptoEntity;
import com.example.datajpaerror.repository.CityPersonRepository;
import com.example.datajpaerror.repository.CityRepository;
import com.example.datajpaerror.repository.DocumentRepository;

@DataJpaTest
class DataJpaErrorApplicationTests {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CityPersonRepository cityPersonRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Test
    void runTest() {

        String personName = "Test person";

        CityEntity city = new CityEntity();
        city.setId(1122);
        city.setName("Test city");
        cityRepository.save(city);

        Optional<CityEntity> optCity = cityRepository.findById(city.getId());
        if (!optCity.isPresent()) {
            throw new RuntimeException("Error city");
        }

        CityPersonEntity person = new CityPersonEntity();
        person.setName(personName);
        person.setEmail("test.person@test.com");
        person.setCity(optCity.get());
        person = cityPersonRepository.save(person);
        cityPersonRepository.flush();

        DocAcmeEntity docAcme = new DocAcmeEntity();
        docAcme.setNumber("1234");
        docAcme.setNewDoc(true);
        docAcme.setFieldX("valX");
        docAcme.setPerson(person);
        person.setDocAcme(docAcme);
        documentRepository.save(docAcme);

        DocXptoEntity docXpto = new DocXptoEntity();
        docXpto.setNumber("9876");
        docXpto.setCity(optCity.get());
        docXpto.setNewDoc(true);
        docXpto.setFieldY("valY");
        docXpto.setPerson(person);
        person.setDocXpto(docXpto);
        documentRepository.save(docXpto);
        documentRepository.flush();

        List<CityPersonEntity> listPeople = cityPersonRepository.findAll();
        for (CityPersonEntity entity : listPeople) {
            System.out.println("\n========\nID: " + entity.getId() + ", Name: " + entity.getName() + "\nDocAcme: "
                    + entity.getDocAcme().getNumber() + ", fieldX: " + entity.getDocAcme().getFieldX() + "\nDocXpto: "
                    + entity.getDocXpto().getNumber() + ", fieldY: " + entity.getDocXpto().getFieldY() + "\n\n");

            assertThat(entity.getName()).isEqualTo(personName);
        }
    }

}
