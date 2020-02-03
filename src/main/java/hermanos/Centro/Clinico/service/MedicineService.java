package hermanos.Centro.Clinico.service;

import hermanos.Centro.Clinico.model.Medicine;
import hermanos.Centro.Clinico.repository.MedicineRepository;
import hermanos.Centro.Clinico.service.interfaces.MedicineServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MedicineService implements MedicineServiceInterface {

    @Autowired
    private MedicineRepository medicineRepository;

    @Override
    public Medicine findById(long id){
        return  medicineRepository.findById(id);
    }

    @Override
    public Medicine save(Medicine medicine){
        return medicineRepository.save(medicine);
    }

    public List<Medicine> findAll() {
        List<Medicine> result = medicineRepository.findAll();
        return result;
    }
    @Override
    public void remove(long id){
        medicineRepository.deleteById(id);
    }
}
