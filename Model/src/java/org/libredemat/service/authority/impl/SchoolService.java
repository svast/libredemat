package org.libredemat.service.authority.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.libredemat.business.authority.School;
import org.libredemat.dao.authority.ISchoolDAO;
import org.libredemat.exception.CvqModelException;
import org.libredemat.security.annotation.Context;
import org.libredemat.security.annotation.ContextPrivilege;
import org.libredemat.security.annotation.ContextType;
import org.libredemat.service.authority.ISchoolService;


/**
 * This class provides School related business logic and
 * services to other layers
 *
 * @author bor@zenexity.fr
 *
 */
public class SchoolService implements ISchoolService {

    private static Logger logger = Logger.getLogger(SchoolService.class);

    private ISchoolDAO schoolDAO;

    @Override
    public School getByName(final String schoolName) {
        return schoolDAO.findByName(schoolName);
    }

    @Override
    public School getById(Long id) {
        return schoolDAO.findById(id);
    }

    @Override
    public List<School> getAll() {
        return schoolDAO.listAll();
    }

    @Override
    public List<School> getActive() {
        return schoolDAO.getActive();
    }

    @Override
    @Context(types = {ContextType.ADMIN}, privilege = ContextPrivilege.WRITE)
    public Long create(final School school) throws CvqModelException {
        Long schoolId = null;

        if (school != null) {
            if (exists(school.getName(), school.getId())) {
                logger.error("create() there is already a school with name : " + school.getName());
                throw new CvqModelException("school.error.nameAlreadyExists");
            }
            schoolId = schoolDAO.create(school).getId();
        }
        logger.debug("Created school object with id : " + schoolId);
        return schoolId;
    }

    @Override
    @Context(types = {ContextType.ADMIN}, privilege = ContextPrivilege.WRITE)
    public void modify(final School school)
        throws CvqModelException {

        if (school == null)
            return;
        
        if (exists(school.getName(), school.getId())) {
            logger.error("modify() there is already a school with name : " + school.getName());
            throw new CvqModelException("school.error.nameAlreadyExists");
        }
        schoolDAO.update(school);
    }

    @Override
    @Context(types={ContextType.ADMIN}, privilege=ContextPrivilege.WRITE)
    public void delete(final School school) {

        if (school != null)
            schoolDAO.delete(school);
    }

    @Override
    public boolean exists(String name, Long id) {
        return schoolDAO.exists(name, id);
    }

    public void setDAO(ISchoolDAO schoolDAO) {
        this.schoolDAO = schoolDAO;
    }
}
