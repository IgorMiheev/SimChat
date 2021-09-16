package com.simbirsoft.simchat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simbirsoft.simchat.domain.dto.Usr;
import com.simbirsoft.simchat.domain.dto.UsrCreate;
import com.simbirsoft.simchat.exception.UsrNotFoundException;
import com.simbirsoft.simchat.service.UsrService;

@RestController
@RequestMapping("/user")
public class UsrController {

	@Autowired
	private UsrService service;

	@PostMapping // Create
	public Usr createUsr(@RequestBody UsrCreate modelCreate) throws UsrNotFoundException {
		return service.create(modelCreate);
	}

	@GetMapping(params = "id") // Read
	public Usr getUsrById(@RequestParam("id") Long id) throws UsrNotFoundException {
		return service.getById(id);
	}

	@PutMapping(params = "id") // Update
	public Usr updateUsr(@RequestParam("id") Long id, @RequestBody UsrCreate modelCreate)
			throws UsrNotFoundException, UsrNotFoundException {
		return service.update(id, modelCreate);
	}

	@DeleteMapping(params = "id") // Delete
	public String deleteUsrById(@RequestParam("id") Long id) throws UsrNotFoundException {
		return service.delete(id);
	}

	/*
	 * 
	 * @GetMapping(params = "id") // Read public ResponseEntity
	 * getUserById(@RequestParam("id") Long user_id) { UsrEntity user =
	 * usersRepository.findById(user_id).orElse(null); try { if (user != null) {
	 * return ResponseEntity.ok(Usr.convertToDto(user)); } else { throw new
	 * UsrNotFoundException("Пользователь с таким id не найден"); } } catch
	 * (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); } }
	 * 
	 * @PutMapping // Update public ResponseEntity updateUser(@RequestBody Usr
	 * userDto) { UsrEntity user =
	 * usersRepository.findById(userDto.getUser_id()).orElse(null); try { if (user
	 * != null) { user.update(userDto.getUsername(), userDto.getPassword(),
	 * userDto.getEmail(), userDto.getIs_banned(), userDto.getBan_endtime()); return
	 * ResponseEntity.ok(Usr.convertToDto(usersRepository.save(user))); } throw new
	 * UsrNotFoundException("Пользователь с таким id не найден"); } catch (Exception
	 * e) { return ResponseEntity.badRequest().body(e.getMessage()); } }
	 * 
	 * @DeleteMapping(params = "id") // Delete public ResponseEntity
	 * deleteUser(@RequestParam("id") Long user_id) { UsrEntity user =
	 * usersRepository.findById(user_id).orElse(null); try { if (user != null) {
	 * usersRepository.delete(user); return
	 * ResponseEntity.ok("Пользователь успешно удален"); } else { throw new
	 * UsrNotFoundException("Пользователя с таким id не существует"); } } catch
	 * (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); } }
	 */

}
