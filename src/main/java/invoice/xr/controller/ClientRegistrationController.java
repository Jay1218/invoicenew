package invoice.xr.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.core.net.SyslogOutputStream;
import invoice.xr.model.ClientUser;
import invoice.xr.model.InvoiceUser;
import invoice.xr.service.RegistrationService;
import invoice.xr.model.Status;

@RestController
public class ClientRegistrationController {

	@Autowired
	RegistrationService registrationService;

	@GetMapping("/")
	public String home() {
		return "Welcome";
	}

	@GetMapping("/showAllClient")
	public ResponseEntity<List<ClientUser>> getAllClient() {
		List<ClientUser> clientUsersList = registrationService.getAllClientDetail();
		return new ResponseEntity<>(clientUsersList, HttpStatus.OK);
	}

	@GetMapping("/findClient")
	public ResponseEntity<List<ClientUser>> findClientByName(@RequestParam(value = "lastName") String lastName) {
		List<ClientUser> clientUsersList = registrationService.findClientByName(lastName);
		return new ResponseEntity<>(clientUsersList, HttpStatus.OK);
	}

	@PostMapping("/registerClient")
	public ResponseEntity<ClientUser> registerClient(@RequestBody ClientUser userDetails) {

		try {

			System.out.println(userDetails.getId());
			ClientUser responseClientUser = registrationService.registerNewClient(userDetails);
			System.out.println(userDetails.getId());
			System.out.println(userDetails.getFirstName());
			System.out.println(userDetails.getLastName());
			System.out.println(userDetails.getContactNo());
			System.out.println(userDetails.getUserType());

			return new ResponseEntity<>(responseClientUser, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/removeClient/{id}")
	public String removeClientById(@PathVariable("id") Integer id) {
		registrationService.removeClientById(id);
		return "Delete by id called";
	}

	@DeleteMapping("/removeClient")
	public String removeClientByName(@RequestParam(value = "firstName") String firstName) {
		registrationService.removeClientByFirstName(firstName);
		return "Delete by name called";
	}

	@GetMapping("/showAllInvoiceUser")
	public ResponseEntity<List<InvoiceUser>> getAllInvoiceUser() {
		List<InvoiceUser> invoiceUsersList = registrationService.getAllInvoiceUserDetail();
		return new ResponseEntity<>(invoiceUsersList, HttpStatus.OK);
	}

	@GetMapping("/findInvoiceUser")
	public ResponseEntity<List<InvoiceUser>> findInvoiceUserByName(@RequestParam(value = "fullName") String fullName) {
		List<InvoiceUser> invoiceUsersList = registrationService.findInvoiceUserByName(fullName);
		return new ResponseEntity<>(invoiceUsersList, HttpStatus.OK);
	}

	@PostMapping("/registerInvoiceUser")
	public ResponseEntity<Status> registerInvoiceUser(@RequestBody InvoiceUser invoiceUserDetails) {
		try {

			if (registrationService.checkInvoiceUser(invoiceUserDetails)) {
				registrationService.registerNewInvoiceUser(invoiceUserDetails);
				return new ResponseEntity<>(Status.SUCCESS, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(Status.USER_ALREADY_EXISTS, HttpStatus.CREATED);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/invoiceUser/login")
    public Status invoiceUserLogin(@RequestBody InvoiceUser loginUser) {
        List<InvoiceUser> users = registrationService.getAllInvoiceUserDetail();
        for (InvoiceUser other : users) {
            if (other.equals(loginUser)) {
            	loginUser.setLoggedIn(true);
                registrationService.registerNewInvoiceUser(loginUser);
                return Status.SUCCESS;
            }
        }
        return Status.FAILURE;
    }

	@PostMapping("/invoiceUser/logout")
    public Status invoiceUserLogout(@RequestBody InvoiceUser logoutUser) {
        List<InvoiceUser> users = registrationService.getAllInvoiceUserDetail();
        for (InvoiceUser other : users) {
            if (other.equals(logoutUser)) {
            	logoutUser.setLoggedIn(false);
            	registrationService.registerNewInvoiceUser(logoutUser);
                return Status.SUCCESS;
            }
        }
        return Status.FAILURE;
    }
	
	@DeleteMapping("/removeInvoiceUser/{id}")
	public String removeInvoiceUserById(@PathVariable("id") Integer id) {
		registrationService.removeInvoiceUserById(id);
		return "Delete by id called";
	}

	@DeleteMapping("/removeInvoiceUser")
	public String removeInvoiceUserByName(@RequestParam(value = "fullName") String fullName) {
		registrationService.removeInvoiceUserByName(fullName);
		return "Delete by name called";
	}
}
