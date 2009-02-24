package fr.cg95.cvq.dao.request;

import java.util.List;

import fr.cg95.cvq.business.request.RequestType;
import fr.cg95.cvq.business.request.DisplayGroup;
import fr.cg95.cvq.dao.IGenericDAO;

/**
 * @author bor@zenexity.fr
 */
public interface IRequestTypeDAO extends IGenericDAO {

    /**
     * Look up a request type by label.
     */
    RequestType findByLabel(final String requestTypeLabel);

    /**
     * Return the list of all known requests types.
     */
    List<RequestType> listAll();

    /**
     * Return the list of requests types in the given activation state.
     */
    List<RequestType> listByCategoryAndState(final Long categoryId, final Boolean active);

    List<DisplayGroup> listAllDisplayGroup();
}
