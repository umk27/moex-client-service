package com.shareservice.services;

import com.shareservice.exceptions.DividendsLimitsRequestsException;
import com.shareservice.exceptions.ShareNotFoundException;
import com.shareservice.model.Share;
import com.shareservice.moexclient.DividendsClient;
import com.shareservice.parsers.DividendsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShareService {

    Logger logger = LoggerFactory.getLogger(ShareService.class);

    @Autowired
    ShareRepository shareRepository;

    @Autowired
    DividendsClient dividendsClient;

    @Autowired
    DividendsParser dividendsParser;

    @Autowired
    DividendsIncomeCounter dividendsIncomeCounter;


    public Share getShare(String str) {

        logger.info("Получение списка акций");

        List<Share> shares = shareRepository.getShares();

        logger.info("Список акций получен");

        Share share = null;
        for (Share share1 : shares) {
            if (share1.getSecId().equals(str) || share1.getShortName().equals(str)) {
                share = share1;
            }
        }

        if (share == null) {
            logger.error("Акция " + str + " не найдена на " +
                    "Московской бирже");
            throw new ShareNotFoundException("Акция " + str + " не найдена на " +
                    "Московской бирже");
        }

        logger.info("Акция " + str + " успешно найдена");

        logger.info("Получение списка дивидендов акции " + str);

        String dividendsXML = "";
        try {
            dividendsXML = dividendsClient.getDividends(share.getSecId());
        } catch (RuntimeException e) {
            logger.error("Мосбиржа не отвечает " +
                    " на запрос о получении дивидендов акции");
            throw new DividendsLimitsRequestsException("Мосбиржа не отвечает " +
                    " на запрос о получении дивидендов акции");
        }


        List<Share.Dividends> dividendsList = dividendsParser.parse(dividendsXML);

        share.setDividendsList(dividendsList);

        logger.info("Список дивидендов акции " + str + " получен");

        logger.info("Рассчет годового дохода по дивидендам");

        Share shareResult = dividendsIncomeCounter.countDividendsIncome(share);

        logger.info("Годовой доход по дивидендам рассчитан");

        return shareResult;
    }

}
