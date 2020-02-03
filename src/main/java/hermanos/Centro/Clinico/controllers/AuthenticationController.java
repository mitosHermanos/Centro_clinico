package hermanos.Centro.Clinico.controllers;

import hermanos.Centro.Clinico.dto.FirstPassChangeDTO;
import hermanos.Centro.Clinico.exception.AccountNotActivatedException;
import hermanos.Centro.Clinico.exception.AccountPendingException;
import hermanos.Centro.Clinico.exception.ResourceConflictException;
import hermanos.Centro.Clinico.model.*;
import hermanos.Centro.Clinico.security.TokenUtils;
import hermanos.Centro.Clinico.security.auth.JwtAuthenticationRequest;
import hermanos.Centro.Clinico.service.AuthorityService;
import hermanos.Centro.Clinico.service.CustomUserDetailsService;
import hermanos.Centro.Clinico.service.PatientService;
import hermanos.Centro.Clinico.service.interfaces.PatientRequestServiceInterface;
import hermanos.Centro.Clinico.service.interfaces.PersonServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Null;
import java.security.Principal;
import java.util.Collection;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PatientRequestServiceInterface patientRequestService;

    @Autowired
    private PersonServiceInterface personService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private AuthorityService authorityService;

    static class RoleDTO{
        public String role;
    }


    /** Function that creates patient registration request which will be later approved or declined
     * @param patientRequest
     * @return
     */

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", path = "/register")
    public ResponseEntity<?> createRegisterRequest(@RequestBody PatientRequest patientRequest){

        if(personService.findByEmail(patientRequest.getEmail()) != null ||
            patientRequestService.findByEmail(patientRequest.getEmail()) != null){

            throw new ResourceConflictException("This email is already in use.");
        }

        if(patientService.findBySocialSecurityNumber(patientRequest.getSocialSecurityNumber()) != null ||
            patientRequestService.findBySocialSecurityNumber(patientRequest.getSocialSecurityNumber()) != null){

            throw new ResourceConflictException("This social security number is already is already in use.");
        }

        patientRequestService.save(patientRequest);

        return ResponseEntity.ok().build();
    }

    /** Function that checks credentials and creates token
     * @param authenticationRequest
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest){

        if(patientRequestService.findByEmail(authenticationRequest.getEmail()) != null){
            throw new AccountPendingException("Your account has not yet been reviewed. If your account is not reviewed in" +
                    " the next 24h, please contact our support team.");
        }

        final Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Person person = (Person)userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

        try {
            Patient p = (Patient) person;
            if(!p.isActivated())
                throw new AccountNotActivatedException("Account not activated. Please check your email.");
        } catch (ClassCastException e){

        }
            if (person.isMustChangePass()) {
                return ResponseEntity.ok(true);
            }

        String jwt = tokenUtils.generateToken(person.getEmail());
        int expiresIn = tokenUtils.getExpiredIn();

        return ResponseEntity.ok(new PersonTokenState(jwt, expiresIn));
    }

    @RequestMapping(value = "/role", method = RequestMethod.GET)
    public ResponseEntity<?> getRole(Principal p, RoleDTO roleDTO){

        Person person = personService.findByEmail(p.getName());

        Collection<?> auth = person.getAuthorities();

        if(auth.size() != 1){
            return ResponseEntity.status(500).build();
        }

        roleDTO.role = ((Authority)auth.iterator().next()).getName();

        return ResponseEntity.ok(roleDTO);
    }

    @RequestMapping(value="/firstPassChange", method = RequestMethod.POST)
    public ResponseEntity<?> firstPassChange(@RequestBody FirstPassChangeDTO fp){
        Person person = personService.findByEmail(fp.getEmail());
        person.setPassword(passwordEncoder.encode(fp.getPassword()));
        person.setMustChangePass(false);
        personService.save(person);

        return ResponseEntity.ok(true);
    }
}
