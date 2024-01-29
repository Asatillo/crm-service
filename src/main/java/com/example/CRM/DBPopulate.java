package com.example.CRM;

import com.example.CRM.model.*;
import com.example.CRM.model.template.DeviceTemplate;
import com.example.CRM.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Component
public class DBPopulate implements CommandLineRunner {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    PlanRepository planRepository;
    @Autowired
    SubscriptionRepository subscriptionRepository;
    @Autowired
    DeviceTemplateRepository deviceTemplateRepository;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    NetworkEntityRepository networkEntityRepository;

    @Override
    public void run(String... args) throws Exception {
        Service serviceMobile1 = new Service("1.5 GB Internet", "DATA", 1.5F, "MOBILE", 2000D);
        Service serviceMobile2 = new Service("1 hour of Voice", "VOICE", 60F, "MOBILE", 1000D);
        Service serviceMobile3 = new Service("100 SMS", "SMS", 100F, "MOBILE", 2500D);
        Service serviceMobile4 = new Service("3 GB Internet", "DATA", 3F, "MOBILE", 2500D);
        Service serviceMobile5 = new Service("2 hours of Voice", "VOICE", 120F, "MOBILE", 1800D);
        Service serviceMobile6 = new Service("200 SMS", "SMS", 200F, "MOBILE", 3000D);
        Service serviceMobile7 = new Service("5 GB Internet", "DATA", 5F, "MOBILE", 3000D);
        Service serviceMobile8 = new Service("3 hours of Voice", "VOICE", 180F, "MOBILE", 2000D);
        Service serviceMobile9 = new Service("300 SMS", "SMS", 300F, "MOBILE", 3500D);
        Service serviceRouter1 = new Service("10 GB Internet", "DATA", 10F, "ROUTER", 5000D);
        Service serviceRouter2 = new Service("20 GB Internet", "DATA", 20F, "ROUTER", 6000D);
        Service serviceRouter3 = new Service("30 GB Internet", "DATA", 30F, "ROUTER", 7000D);

        serviceRepository.saveAll(List.of(serviceMobile1, serviceMobile2, serviceMobile3, serviceMobile4,
                serviceMobile5, serviceMobile6, serviceMobile7, serviceMobile8, serviceMobile9, serviceRouter1,
                serviceRouter2, serviceRouter3));

        Plan planMobile1 = new Plan("Basic Plan", Period.ofYears(1).toString(), "Basic subscription", 1999., List.of(serviceMobile1, serviceMobile2, serviceMobile3),"MOBILE");
        Plan planMobile2 = new Plan("Premium Plan", Period.ofMonths(3).toString(), "Premium subscription", 3999., List.of(serviceMobile4, serviceMobile5, serviceMobile6), "MOBILE");
        Plan planMobile3 = new Plan("Ultimate Plan", Period.ofMonths(6).toString(), "Ultimate subscription", 5999., List.of(serviceMobile7, serviceMobile8, serviceMobile9), "MOBILE");
        Plan planMobile4 = new Plan("Talk a lot", Period.ofYears(1).toString(), "Talk a lot subscription", 2999., List.of(serviceMobile8, serviceMobile1, serviceMobile3), "MOBILE");
        Plan planMobile5 = new Plan("Surf a lot", Period.ofYears(1).toString(), "Surf a lot subscription", 2999., List.of(serviceMobile7, serviceMobile2, serviceMobile3), "MOBILE");
        Plan planMobile6 = new Plan("SMS a lot", Period.ofYears(1).toString(), "SMS a lot subscription", 3999., List.of(serviceMobile9, serviceMobile1, serviceMobile2), "MOBILE");
        Plan planRouter7 = new Plan("10GB router plan", Period.ofYears(1).toString(), "10GB for Router subscription", 4999., List.of(serviceRouter1), "ROUTER");
        Plan planRouter8 = new Plan("20GB router plan", Period.ofYears(1).toString(), "20GB for Router subscription", 5999., List.of(serviceRouter2), "ROUTER");
        Plan planRouter9 = new Plan("30GB router plan", Period.ofYears(1).toString(), "30GB for Router subscription", 6999., List.of(serviceRouter3), "ROUTER");
        planRepository.saveAll(List.of(planMobile1, planMobile2, planMobile3, planMobile4, planMobile5, planMobile6, planRouter7, planRouter8, planRouter9));

        Customer customer1 = new Customer("Alice", "Smith", "alice@example.com", "123 Main St", "Budapest", LocalDate.of(1990, 5, 15), "PREMIUM");
        Customer customer2 = new Customer("Bob", "Johnson", "bob@example.com", "456 Elm St", "Debrecen", LocalDate.of(1985, 3, 10), "GOLD");
        Customer customer3 = new Customer("Eva", "Andersen", "eva@example.com", "789 Oak St", "Szeged", LocalDate.of(1988, 8, 20), "SILVER");
        Customer customer4 = new Customer("David", "Brown", "david@example.com", "567 Pine St", "Pécs", LocalDate.of(1995, 2, 5), "BRONZE");
        Customer customer5 = new Customer("Sophie", "Miller", "sophie@example.com", "987 Cedar St", "Győr", LocalDate.of(1982, 11, 30), "EXPLORE");
        Customer customer6 = new Customer("Michael", "Wilson", "michael@example.com", "345 Oak St", "Budapest", LocalDate.of(1975, 9, 12), "PREMIUM");
        Customer customer7 = new Customer("Emily", "Harris", "emily@example.com", "678 Elm St", "Debrecen", LocalDate.of(1992, 4, 25), "GOLD");
        Customer customer8 = new Customer("Oliver", "Anderson", "oliver@example.com", "456 Pine St", "Szeged", LocalDate.of(1970, 7, 7), "SILVER");
        Customer customer9 = new Customer("Sophia", "Lee", "sophia@example.com", "789 Maple St", "Pécs", LocalDate.of(1987, 12, 15), "BRONZE");
        Customer customer10 = new Customer("Liam", "Taylor", "liam@example.com", "123 Cedar St", "Győr", LocalDate.of(1986, 2, 28), "EXPLORE");
        customerRepository.saveAll(List.of(customer1, customer2, customer3, customer4, customer5, customer6, customer7, customer8, customer9, customer10));

        NetworkEntity networkMobile1 = new NetworkEntity("+36201234567", "MOBILE", customer1, "Personal Mobile Network");
        NetworkEntity networkMobile2 = new NetworkEntity("+36201234568", "MOBILE", null, "");
        NetworkEntity networkMobile3 = new NetworkEntity("+36201234569", "MOBILE", customer5, "Travel Mobile Network");
        NetworkEntity networkMobile4 = new NetworkEntity("+36201234570", "MOBILE", customer7, "Secondary Mobile Network");
        NetworkEntity networkMobile5 = new NetworkEntity("+36201234571", "MOBILE", customer9, "Emergency Mobile Network");
        NetworkEntity networkMobile6 = new NetworkEntity("+36201234572", "MOBILE", customer1, "Family Mobile Network");
        NetworkEntity networkMobile7 = new NetworkEntity("+36201234573", "MOBILE", null, "");
        NetworkEntity networkMobile8 = new NetworkEntity("+36201234574", "MOBILE", customer5, "Work Mobile Network");
        NetworkEntity networkMobile9 = new NetworkEntity("+36201234575", "MOBILE", customer7, "Development Mobile Network");
        NetworkEntity networkMobile10 = new NetworkEntity("+36201234576", "MOBILE", customer9, "Temporary Mobile Network");
        NetworkEntity networkRouter1 = new NetworkEntity("192.168.1.1", "ROUTER", customer2, "Home Router Network");
        NetworkEntity networkRouter2 = new NetworkEntity("10.0.0.1", "ROUTER", customer4, "Office Router Network");
        NetworkEntity networkRouter3 = new NetworkEntity("172.16.0.1", "ROUTER", null, "");
        NetworkEntity networkRouter4 = new NetworkEntity("192.168.2.1", "ROUTER", customer8, "Backup Router Network");
        NetworkEntity networkRouter5 = new NetworkEntity("10.1.1.1", "ROUTER", customer10, "Holiday Router Network");
        NetworkEntity networkRouter6 = new NetworkEntity("192.168.3.1", "ROUTER", customer2, "Guest Router Network");
        NetworkEntity networkRouter7 = new NetworkEntity("10.2.2.2", "ROUTER", null, "");
        NetworkEntity networkRouter8 = new NetworkEntity("192.168.4.1", "ROUTER", customer6, "Main Router Network");
        NetworkEntity networkRouter9 = new NetworkEntity("10.3.3.3", "ROUTER", customer8, "Test Router Network");
        NetworkEntity networkRouter10 = new NetworkEntity("192.168.5.1", "ROUTER", customer10, "Production Router Network");
        networkEntityRepository.saveAll(List.of(networkMobile1, networkMobile2, networkMobile3, networkMobile4,
                networkMobile5, networkMobile6, networkMobile7, networkMobile8, networkMobile9, networkMobile10,
                networkRouter1, networkRouter2, networkRouter3, networkRouter4, networkRouter5, networkRouter6,
                networkRouter7, networkRouter8, networkRouter9, networkRouter10));

        DeviceTemplate deviceMobileTemplate1 = new DeviceTemplate("Galaxy S20", "Samsung", "MOBILE", Period.ZERO.toString(), 300000.0, "white", 128);
        DeviceTemplate deviceMobileTemplate2 = new DeviceTemplate("Galaxy S10", "Samsung", "MOBILE", Period.ofYears(1).toString(), 200000.0, "black", 128);
        DeviceTemplate deviceMobileTemplate3 = new DeviceTemplate("Iphone XS", "Apple", "MOBILE", Period.ofYears(1).toString(), 400000.0, "silver", 256);
        DeviceTemplate deviceMobileTemplate4 = new DeviceTemplate("Iphone 11", "Apple", "MOBILE", Period.ofYears(2).toString(), 340000.0, "black", 128);
        DeviceTemplate deviceRouterTemplate1 = new DeviceTemplate("Archer AX21", "TP-Link", "ROUTER", Period.ZERO.toString(), 0.0, "white", 0);
        DeviceTemplate deviceRouterTemplate2 = new DeviceTemplate("Hydra Pro 6 (MR5500)", "Linksys", "ROUTER", Period.ZERO.toString(), 0.0, "black", 0);
        DeviceTemplate deviceRouterTemplate3 = new DeviceTemplate("Archer AX6000", "TP-Link", "ROUTER", Period.ZERO.toString(), 0.0, "silver", 0);
        deviceTemplateRepository.saveAll(List.of(deviceMobileTemplate1, deviceMobileTemplate2, deviceMobileTemplate3,
                deviceMobileTemplate4, deviceRouterTemplate1, deviceRouterTemplate2, deviceRouterTemplate3));

        Device deviceMobile1 = new Device(deviceMobileTemplate1, LocalDate.now().atStartOfDay());
        Device deviceMobile2 = new Device(deviceMobileTemplate2, LocalDate.now().atStartOfDay());
        Device deviceMobile3 = new Device(deviceMobileTemplate3, LocalDate.now().atStartOfDay());
        Device deviceMobile4 = new Device(deviceMobileTemplate4, LocalDate.now().atStartOfDay());
        Device deviceMobile5 = new Device(deviceMobileTemplate2, LocalDate.now().atStartOfDay());
        Device deviceMobile6 = new Device(deviceMobileTemplate4, LocalDate.now().atStartOfDay());
        Device deviceMobile7 = new Device(deviceMobileTemplate1, LocalDate.now().atStartOfDay());
        Device deviceRouter1 = new Device(deviceRouterTemplate1, LocalDate.now().atStartOfDay());
        Device deviceRouter2 = new Device(deviceRouterTemplate2, LocalDate.now().atStartOfDay());
        Device deviceRouter3 = new Device(deviceRouterTemplate3, LocalDate.now().atStartOfDay());
        Device deviceRouter4 = new Device(deviceRouterTemplate1, LocalDate.now().atStartOfDay());
        Device deviceRouter5 = new Device(deviceRouterTemplate2, LocalDate.now().atStartOfDay());
        Device deviceRouter6 = new Device(deviceRouterTemplate3, LocalDate.now().atStartOfDay());
        Device deviceRouter7 = new Device(deviceRouterTemplate1, LocalDate.now().atStartOfDay());
        Device deviceRouter8 = new Device(deviceRouterTemplate2, LocalDate.now().atStartOfDay());
        Device deviceRouter9 = new Device(deviceRouterTemplate3, LocalDate.now().atStartOfDay());
        deviceRepository.saveAll(List.of(deviceMobile1, deviceMobile2, deviceMobile3, deviceMobile4, deviceMobile5,
                deviceMobile6, deviceMobile7, deviceRouter1, deviceRouter2, deviceRouter3, deviceRouter4, deviceRouter5,
                deviceRouter6, deviceRouter7, deviceRouter8, deviceRouter9));

        Subscription subscription1 = new Subscription(networkMobile1, planMobile1, deviceMobile1, LocalDate.now().atStartOfDay());
        Subscription subscription2 = new Subscription(networkMobile8, planMobile2, deviceMobile2, LocalDate.now().atStartOfDay());
        Subscription subscription3 = new Subscription(networkMobile3, planMobile3, null, LocalDate.now().plus(Period.ofMonths(1)).atStartOfDay());
        Subscription subscription4 = new Subscription(networkMobile4, planMobile4, deviceMobile3, LocalDate.now().plus(Period.ofDays(1)).atStartOfDay());
        Subscription subscription5 = new Subscription(networkMobile5, planMobile5, deviceMobile4, LocalDate.now().plus(Period.ofDays(2)).atStartOfDay());
        Subscription subscription6 = new Subscription(networkMobile6, planMobile5, null, LocalDate.now().plus(Period.ofDays(3)).atStartOfDay());
        Subscription subscription7 = new Subscription(networkMobile1, planMobile3, deviceMobile5, LocalDate.now().plus(Period.ofDays(4)).atStartOfDay());
        Subscription subscription8 = new Subscription(networkMobile8, planMobile3, null, LocalDate.now().plus(Period.ofDays(5)).atStartOfDay());
        Subscription subscription9 = new Subscription(networkMobile9, planMobile2, deviceMobile6, LocalDate.now().plus(Period.ofDays(6)).atStartOfDay());
        Subscription subscription10 = new Subscription(networkMobile10, planMobile4, deviceMobile7, LocalDate.now().plus(Period.ofDays(7)).atStartOfDay());
        Subscription subscription11 = new Subscription(networkRouter1, planRouter7, deviceRouter1, LocalDate.now().plus(Period.ofDays(8)).atStartOfDay());
        Subscription subscription12 = new Subscription(networkRouter2, planRouter9, deviceRouter2, LocalDate.now().plus(Period.ofDays(9)).atStartOfDay());
        Subscription subscription13 = new Subscription(networkRouter8, planRouter9, deviceRouter3, LocalDate.now().plus(Period.ofDays(10)).atStartOfDay());
        Subscription subscription14 = new Subscription(networkRouter4, planRouter8, deviceRouter4, LocalDate.now().plus(Period.ofDays(11)).atStartOfDay());
        Subscription subscription15 = new Subscription(networkRouter5, planRouter7, deviceRouter5, LocalDate.now().plus(Period.ofDays(12)).atStartOfDay());
        Subscription subscription16 = new Subscription(networkRouter6, planRouter8, deviceRouter6, LocalDate.now().plus(Period.ofDays(13)).atStartOfDay());
        Subscription subscription17 = new Subscription(networkRouter2, planRouter9, deviceRouter7, LocalDate.now().plus(Period.ofDays(14)).atStartOfDay());
        Subscription subscription18 = new Subscription(networkRouter8, planRouter9, deviceRouter8, LocalDate.now().plus(Period.ofDays(15)).atStartOfDay());
        Subscription subscription19 = new Subscription(networkRouter9, planRouter7, deviceRouter9, LocalDate.now().plus(Period.ofDays(16)).atStartOfDay());

        subscriptionRepository.saveAll(List.of(subscription1, subscription2, subscription3, subscription4,
                subscription5, subscription6, subscription7, subscription8, subscription9, subscription10,
                subscription11, subscription12, subscription13, subscription14, subscription15, subscription16,
                subscription17, subscription18, subscription19));

    }
}
