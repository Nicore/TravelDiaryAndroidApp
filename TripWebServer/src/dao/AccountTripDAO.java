/**
 * 
 */
package dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import domain.AccountTrip;
import domain.Trip;

/**
 * @author jonda741
 *
 */
public interface AccountTripDAO {
	
	public Collection<AccountTrip> getAll() throws SQLException;
	
	public void create(AccountTrip accountTrip) throws SQLException;
	
	public Collection<AccountTrip> getByFlagByUser(String username, String flag) throws SQLException;
	
	public void delete(String username, int tripId) throws SQLException;
}
