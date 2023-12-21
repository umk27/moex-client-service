package com.shareservice.services;

import com.shareservice.exceptions.*;
import com.shareservice.model.Share;
import com.shareservice.moexclient.DividendsClient;
import com.shareservice.parsers.DividendsParser;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ShareService {

   private final static Logger logger = LoggerFactory.getLogger(ShareService.class);

  private final ShareRepository shareRepository;

  private final DividendsClient dividendsClient;

  private final DividendsParser dividendsParser;

  private final DividendsIncomeCounter dividendsIncomeCounter;

    public ShareService(ShareRepository shareRepository, DividendsClient dividendsClient, DividendsParser dividendsParser, DividendsIncomeCounter dividendsIncomeCounter) {
        this.shareRepository = shareRepository;
        this.dividendsClient = dividendsClient;
        this.dividendsParser = dividendsParser;
        this.dividendsIncomeCounter = dividendsIncomeCounter;
    }

    @Retry(name = "share-exception", fallbackMethod = "shareEx")
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

    public Share shareEx(String str, ShareNotFoundException exception) {
        Share share = new Share();
        share.setError("Акция " + str + " не найдена на " +
                "Московской бирже. Проверьте правильность ввода SecId/ShortName");
        return share;
    }

    public Share shareEx(String str, ShareLimitsRequestsException exception) {
        Share share = new Share();
        share.setError("Мосбиржа не отвечает на запрос " +
                "о получении информации об акциях");
        return share;
    }

    public Share shareEx(String str, DividendsLimitsRequestsException exception) {
        Share share = new Share();
        share.setError("Мосбиржа не отвечает на запрос " +
                " о получении информации о дивидендах");
        return share;
    }

    public Share shareEx(String str, ShareXMLParsingException exception) {
        Share share = new Share();
        share.setError("Ошибка парсинга XML файла акций");
        return share;
    }

    public Share shareEx(String str, DividendsXMLParsingException exception) {
        Share share = new Share();
        share.setError("Ошибка парсинга XML файла дивидендов");
        return share;
    }

}
