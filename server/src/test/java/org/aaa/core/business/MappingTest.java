package org.aaa.core.business;

import org.aaa.core.business.mapping.entity.damage.Damage;
import org.aaa.core.business.mapping.entity.Contract;
import org.aaa.core.business.mapping.entity.Deductible;
import org.aaa.core.business.mapping.entity.Insurance;
import org.aaa.core.business.mapping.entity.UserAccount;
import org.aaa.core.business.mapping.entity.person.Person;
import org.aaa.core.business.mapping.entity.person.insuree.Customer;
import org.aaa.core.business.repository.DAO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
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
    @Transactional(readOnly = true)
    public void testSameQueryCall() {
        List<Person> persons = dao.find(Person.class);
        System.out.println(persons == dao.find(Person.class));
    }

    @Test
    @Transactional(readOnly = true)
    public void blazebit_entityView_doesWork() {
        System.out.println(dao.getVehicleCountByInsureeAndModel());
    }

    @Test
    @Transactional
    public void idAllocator_should_work() {
        System.out.println(new Customer("titi").getId());
        System.out.println(new Customer("toto").getId());
    }

    @Test
    @Transactional(readOnly = true)
    public void daoImpl_getVehicleCountByInsureeAndModel_shouldWork() {
        System.out.println(dao.getVehicleCountByInsureeAndModel());
    }

    @Test
    public void testUUID() {
        System.out.println(UUID.randomUUID().hashCode());
    }
}