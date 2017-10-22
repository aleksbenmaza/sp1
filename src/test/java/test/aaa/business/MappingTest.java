package test.aaa.business;

import org.aaa.core.business.mapping.*;
import org.aaa.core.business.mapping.damage.Damage;
import org.aaa.core.business.mapping.person.insuree.Customer;
import org.aaa.core.business.repository.DAO;

import org.hibernate.proxy.HibernateProxy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by alexandremasanes on 24/09/2017.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationContext.xml")
public class MappingTest {

    @Autowired
    private DAO dao;

    @Test//(expected = NullPointerException.class)
    public void expectNullPointerForUserAccount() {
        List<UserAccount> userAccounts;

        userAccounts = dao.find(UserAccount.class);

        System.out.println(userAccounts.get(0).getId().getUser().getClass());
    }

    @Test//(expected = NullPointerException.class)
    public void expectNullPointerForDamage() {
        List<Damage> damages;

        damages = dao.find(Damage.class);

        System.out.println(damages.get(0).getSinister().getClass());
    }

    @Test//(expected = NullPointerException.class)
    public void expectNullPointerForDeductible() {
        List<Deductible> deductibles;

        deductibles = dao.find(Deductible.class);

        System.out.println(deductibles.get(0).getId().getDamage().getClass());
    }

    @Test @Transactional(readOnly = true)
    public void testCoveragesByInsurance() {
        List<Insurance> insurances;

        insurances = dao.find(Insurance.class);

        for(Insurance insurance : insurances)
            System.out.println(insurance.getCode() + " " + insurance.getCoveragesBySinisterType());
    }

    @Test @Transactional(readOnly = true)
    public void testIfCallingGetIdTriggersLazyLoading() {
        Customer customer = dao.find(Customer.class, 249);
        System.out.println(">>>> will load ? <<<<");
        customer.getContracts().iterator();
        System.out.println("<<<< will load ? >>>>");
        for(Contract contract : customer.getContracts()) {
            System.out.println(">>>> Contract#getId call ! <<<<");
            System.out.println(contract.getClass());
            contract.getId();
            System.out.println("<<<< Contract#getId call ! >>>>");
            break;
        }
    }

    @Test
    public void testUUID() {
        System.out.println(UUID.randomUUID().hashCode());
    }
}