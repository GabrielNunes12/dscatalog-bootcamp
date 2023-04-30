package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.RoleDTO;
import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.dto.UserUpdateDTO;
import com.devsuperior.dscatalog.entities.Role;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
	private static Logger logger = LoggerFactory.getLogger(UserService.class);
	//injetando o @Bean da classe de configuração
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository repository;

	@Autowired
	private RoleRepository roleRepository;
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {
		Page<User> list = repository.findAll(pageable);
		return list.map(x -> new UserDTO(x));
	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		User entity = new User();
		copyDtoToEntity(dto, entity);
		//salvando na entidade a senha do usuario já encryptada
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		entity = repository.save(entity);
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) {
		try {
			User entity = repository.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new UserDTO(entity);
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}		
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}
	
	private void copyDtoToEntity(UserDTO dto, User entity) {

		entity.setFirstName(dto.getFirstName());
		entity.setEmail(dto.getEmail());
		entity.setLastName(dto.getLastName());
		
		entity.getRoles().clear();
		for (RoleDTO roleDTO : dto.getRoles()) {
			Role role = roleRepository.getOne(roleDTO.getId());
			entity.getRoles().add(role);
		}
	}
	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		User user = repository.findByEmail(s);
		if(user == null) {
			logger.error("User not found at: " + s);
			throw new UsernameNotFoundException("Email not found!");
		}
		logger.info("User found " + s);
		return user;
	}

}
