package com.shareservice.services;

import com.shareservice.exceptions.ShareLimitsRequestsException;
import com.shareservice.model.Share;
import com.shareservice.moexclient.ShareClient;
import com.shareservice.parsers.ShareParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = {"shareRepo"})
public class ShareRepository {

    Logger logger = LoggerFactory.getLogger(ShareRepository.class);

    @Autowired
    ShareClient shareClient;

    @Autowired
    ShareParser shareParser;

    @Cacheable
    public List<Share> getShares() {
        String shareXML = "";
        try {
            shareXML = shareClient.getShares();
        } catch (RuntimeException e) {
            logger.error("Мосбиржа не отвечает " +
                    " на запрос о получении данных об акциях");
            throw new ShareLimitsRequestsException("Мосбиржа не отвечает " +
                    " на запрос о получении данных об акциях");
        }
        List<Share> shares = shareParser.parse(shareXML);
        return shares;
    }

}
