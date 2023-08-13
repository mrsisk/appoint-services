package mrsisk.github.io.appointservice.service;

import jakarta.transaction.Transactional;
import mrsisk.github.io.appointservice.dto.AgentRegistrationDto;
import mrsisk.github.io.appointservice.dto.CreateAgentDto;
import mrsisk.github.io.appointservice.dto.InviteAgentDto;
import mrsisk.github.io.appointservice.dto.User;
import mrsisk.github.io.appointservice.models.Agent;
import mrsisk.github.io.appointservice.models.Department;
import mrsisk.github.io.appointservice.repository.AgentRepository;
import mrsisk.github.io.appointservice.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AgentService {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private AgentRegistrationService agentRegistrationService;

    @Autowired
    private UserService userService;


    public String invite(InviteAgentDto dto) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("department", dto.departmentId());
        claims.put("email", dto.email());
        return tokenService.doGenerateToken(claims, dto.email());
    }

    public Mono<Agent> handleInvite(CreateAgentDto dto) {

            if (tokenService.isTokenValid(dto.token())) {
                long departmentId = Long.parseLong(tokenService.getClaim(dto.token(), "department"));
                Department department = departmentRepository.findById(departmentId).orElseThrow();

                String email = tokenService.getSubjectFromToken(dto.token());
                return registerAgentUserAccount(new AgentRegistrationDto(email, dto.password(), dto.firstName(), dto.lastName()), department);
            }
            return Mono.error(new RuntimeException("Token invalid"));
    }


    private Agent registerAgentInternal(User user, AgentRegistrationDto dto, Department department){
        var agent = new Agent(dto.firstName(), dto.lastName(), dto.email(), department, user.id());
        return agentRepository.save(agent);
    }

    public Mono<Agent> registerAgentUserAccount(AgentRegistrationDto dto, Department department){
      return agentRegistrationService.register(dto)
              .flatMap(code -> {
                  if (code.is2xxSuccessful()){
                      return userService.getUserByEmail(dto.email());
                  }else if (code == HttpStatus.CONFLICT){
                      return Mono.error(new RuntimeException("user already exist"));
                  }
                  return Mono.error(new RuntimeException("Failed to register user account"));
              })
              .map(u -> registerAgentInternal(u, dto, department));
    }

    public List<Agent> getAgentsInternal(){
        return agentRepository.findAll();
    }


}
