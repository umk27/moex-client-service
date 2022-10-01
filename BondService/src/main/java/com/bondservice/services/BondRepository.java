package com.bondservice.services;

import com.bondservice.exceptions.CorpBondLimitRequestsException;
import com.bondservice.exceptions.GovBondLimitRequestsException;
import com.bondservice.model.Bond;
import com.bondservice.moexclient.CorporateBondClient;
import com.bondservice.moexclient.GovernmentBondClient;
import com.bondservice.parsers.BondParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = {"bondRepo"})
public class BondRepository {

  private final static Logger logger = LoggerFactory.getLogger(BondRepository.class);

    private final CorporateBondClient corporateBondClient;

    private final GovernmentBondClient governmentBondClient;

    private final BondParser bondParser;

    public BondRepository(CorporateBondClient corporateBondClient, GovernmentBondClient governmentBondClient, BondParser bondParser) {
        this.corporateBondClient = corporateBondClient;
        this.governmentBondClient = governmentBondClient;
        this.bondParser = bondParser;
    }

    @Cacheable
    public List<Bond> getBonds() {

        List<Bond> bonds = new ArrayList<>();
        String corpXML = "";
        String govXML = "";
        try {
            corpXML = corporateBondClient.getCorporateBonds();
        } catch (RuntimeException e) {
            logger.error("Мосбиржа не отвечает " +
                    "на запрос данных о корпоративных облигациях");
            throw new CorpBondLimitRequestsException("Мосбиржа не отвечает " +
                    "на запрос данных о корпоративных облигациях");
        }

        try {
            govXML = governmentBondClient.getGovernmentBonds();
        } catch (RuntimeException e) {
            logger.error("Мосбиржа не отвечает " +
                    "на запрос данных о государственных облигациях");
            throw new GovBondLimitRequestsException("Мосбиржа не отвечает " +
                    "на запрос данных о государственных облигациях");
        }

        List<Bond> corpBonds = bondParser.parse(corpXML);

        List<Bond> govBonds = bondParser.parse(govXML);

        bonds.addAll(corpBonds);
        bonds.addAll(govBonds);

        return bonds;

    }
}
