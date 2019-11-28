package hermanos.Centro.Clinico.service;

import hermanos.Centro.Clinico.model.Patient;
import hermanos.Centro.Clinico.repository.PatientRepository;
import hermanos.Centro.Clinico.service.interfaces.PatientServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PatientService implements PatientServiceInterface {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PatientRepository patientRepository;

    public Patient findBySocialSecurityNumber(String socialSecurityNumber){
        return  patientRepository.findBySocialSecurityNumber(socialSecurityNumber);
    }

    public Patient save(Patient patient){
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        return patientRepository.save(patient);
    }

}
