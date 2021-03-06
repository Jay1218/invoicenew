package invoice.xr.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import invoice.xr.model.ClientUser;


public interface RegisterDao extends Repository<ClientUser, Integer> {
	
	
	
	@Query("SELECT clientUser from ClientUser clientUser where clientUser.clientName IN (:clientName)")
	@Transactional(readOnly = true)
	List<ClientUser> findClientByClientName(@Param("clientName") String clientName);
	
	@Query("SELECT clientUser from ClientUser clientUser where clientUser.clientId IN (:clientId)")
	@Transactional(readOnly = true)
	ClientUser findClientById(@Param("clientId") String clientId);
	
	void save(ClientUser clientUser);
	
	void deleteById(Integer id);
	
	@Modifying
	@Transactional(readOnly = false)
	@Query("delete from ClientUser clientUser where clientUser.clientName=:clientName")
	void deleteClientByClientName(@Param("clientName") String clientName);
	
	@Query("SELECT clientUser from ClientUser clientUser")
	@Transactional(readOnly = true)
	List<ClientUser> findAllClient();
	
	
	  
}
