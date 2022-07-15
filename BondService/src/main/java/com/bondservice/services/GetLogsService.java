package com.bondservice.services;

import com.bondservice.exceptions.GetLogsException;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

@Service
public class GetLogsService {

    public String getLogs() {
        String logs = "";
        try (FileReader fileReader = new FileReader("logs/LogFile")) {
            Scanner scanner = new Scanner(fileReader);
            while (scanner.hasNextLine()) {
                logs = logs + scanner.nextLine() + "<br/>";
            }
        } catch (FileNotFoundException e) {
            throw new GetLogsException("Ошибка получения логов");
        } catch (IOException e) {
            throw new GetLogsException("Ошибка получения логов");
        }

        return logs;
    }
}
