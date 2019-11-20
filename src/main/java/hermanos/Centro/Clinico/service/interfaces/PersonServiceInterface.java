package hermanos.Centro.Clinico.service.interfaces;

import hermanos.Centro.Clinico.model.Person;
import org.springframework.stereotype.Service;

@Service
public interface PersonServiceInterface {

    public Person findById(long id);
    public Person findByEmail(String email);
    public Person save(Person person);

}
