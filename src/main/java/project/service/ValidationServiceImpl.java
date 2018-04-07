package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dao.validation.ValidationDao;
import project.model.Change;
import project.model.query.SearchParams;
import project.model.validation.Validation;
import project.model.validation.ValidationDto;

import java.util.List;

@Service
@Transactional
public class ValidationServiceImpl implements ValidationService {

    private final ValidationDao dao;

    @Autowired
    public ValidationServiceImpl(ValidationDao dao) {
        this.dao = dao;
    }

    @Override
    public Validation load(String validationId) {
        return dao.load(validationId);
    }

    @Override
    public void create(Validation validation) {
        dao.create(validation);
    }

    @Override
    public void update(Validation validation) {
        dao.update(validation);
    }

    @Override
    public void remove(Validation validation) {
        dao.remove(validation);
    }

    @Override
    public List<ValidationDto> find(SearchParams searchParams) {
        return dao.find(searchParams);
    }

    @Override
    public List<Change> getChanges(String validationId) {
        return dao.getChanges(validationId);
    }

    @Override
    public Validation loadVersion(int versionId) {
        return dao.loadVersion(versionId);
    }

}
