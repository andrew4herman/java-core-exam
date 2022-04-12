package carsharing.database.dao;

public class DaoContainer {

    private final CompanyDao companyDao;
    private final CarDao carDao;
    private final CustomerDao customerDao;

    public DaoContainer(CompanyDao companyDao, CarDao carDao, CustomerDao customerDao) {
        this.companyDao = companyDao;
        this.carDao = carDao;
        this.customerDao = customerDao;
    }

    public CompanyDao getCompanyDao() {
        return companyDao;
    }

    public CarDao getCarDao() {
        return carDao;
    }

    public CustomerDao getCustomerDao() {
        return customerDao;
    }
}
