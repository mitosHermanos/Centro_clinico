package hermanos.Centro.Clinico.service;

import hermanos.Centro.Clinico.model.Checkup;
import hermanos.Centro.Clinico.repository.CheckupRepository;
import hermanos.Centro.Clinico.service.interfaces.CheckupServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckupService implements CheckupServiceInterface {
    @Autowired
    CheckupRepository checkupRepository;

    @Override
    public Checkup findById(long id) {
        return checkupRepository.findById(id);
    }

    @Override
    public Checkup save(Checkup checkup) {
        return checkupRepository.save(checkup);
    }

    @Override
    public boolean isValid(Checkup checkup){


        for(Checkup c : checkupRepository.findAll()){
            if(c.getDate().isEqual(checkup.getDate()) &&
                    !(
                        (c.getStartEnd().getStartTime().isAfter(checkup.getStartEnd().getEndTime())) ||
                        (c.getStartEnd().getEndTime().isBefore(checkup.getStartEnd().getStartTime()))
                    )){
                if(c.getDoctor().getId().equals(checkup.getDoctor().getId()))
                    return false;
            }
        }
        return true;
    }

    public void deleteById(long id){
        checkupRepository.deleteById(id);}
}
