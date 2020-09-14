package by.deathsmell.clientmanager.service;

import by.deathsmell.clientmanager.configuration.EntityCheckPattern;
import by.deathsmell.clientmanager.domain.Cashbox;
import by.deathsmell.clientmanager.domain.Client;
import by.deathsmell.clientmanager.dto.enums.CashboxModel;
import by.deathsmell.clientmanager.repository.CashboxRepository;
import by.deathsmell.clientmanager.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.*;

import static by.deathsmell.clientmanager.utils.StringColorizeUtil.setColor;
import static org.fusesource.jansi.Ansi.Color.*;

@Slf4j
@Component
public class InvalidTypeChanger {

    final CashboxRepository cashboxRepository;
    final ClientRepository clientRepository;
    final List<EntityCheckPattern<Cashbox>> cashboxCheckPatterns;
    final List<EntityCheckPattern<Client>> clientCheckPatterns;


    Set<String> skippingTypes;
    Map<String, Integer> famousInvalidTypes;

    public InvalidTypeChanger(CashboxRepository cashboxRepository,
                              ClientRepository clientRepository,
                              @Autowired(required = false) List<EntityCheckPattern<Cashbox>> cashboxCheckPatterns,
                              @Autowired(required = false) List<EntityCheckPattern<Client>> clientCheckPatterns) {
        this.cashboxRepository = cashboxRepository;
        this.clientRepository = clientRepository;
        this.cashboxCheckPatterns = cashboxCheckPatterns;
        this.clientCheckPatterns = clientCheckPatterns;
        this.skippingTypes = new HashSet<>();
        this.famousInvalidTypes = new HashMap<>();
    }

    public void validate() {
        for (int i = 0; i < cashboxCheckPatterns.size(); i++) {
            try {
                log.info("Start cashboxes validation." + setColor("({}/{})", RED), i + 1, cashboxCheckPatterns.size());
                validateCashbox(i);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < clientCheckPatterns.size(); i++) {
            log.info("Start client validation." + setColor("({}/{})", RED), i + 1, clientCheckPatterns.size());
            try {
                validateClient(i);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, Set<String>> getInvalidClientTypes(boolean printToConsole) {
        List<Client> clientFromDB = clientRepository.findAll();
        HashMap<String, Set<String>> result = new HashMap<>();
        for (EntityCheckPattern<Client> clientCheckPattern : clientCheckPatterns) {
            Set<String> invalidTypes = new HashSet<>();
            for (Client client : clientFromDB) {
                clientCheckPattern.getInvalidType(invalidTypes, client);
            }
            String patternName = getPatternNameQualifierOrClassName(clientCheckPattern);
            result.put(patternName, invalidTypes);

            if (printToConsole)
                printInvalidTypes(invalidTypes, patternName);
        }
        return result;
    }


    public Map<String, Set<String>> getInvalidCashboxesTypes(boolean printToConsole) {
        List<Cashbox> cashboxesFromDB = cashboxRepository.findAll();
        HashMap<String, Set<String>> result = new HashMap<>();
        for (EntityCheckPattern<Cashbox> cashboxCheckPattern : cashboxCheckPatterns) {
            Set<String> invalidTypes = new HashSet<>();
            for (Cashbox cashbox : cashboxesFromDB) {
                cashboxCheckPattern.getInvalidType(invalidTypes, cashbox);
            }
            String patternName = getPatternNameQualifierOrClassName(cashboxCheckPattern);
            result.put(patternName, invalidTypes);

            if (printToConsole)
                printInvalidTypes(invalidTypes, patternName);
        }
        return result;
    }


    private void printInvalidTypes(Set<String> invalidTypes, String patternName) {
        System.out.printf("Pattern %s have %d invalid data types.\n",
                patternName,
                invalidTypes.size());
        int i = 0;
        for (String invalidType : invalidTypes) {
            System.out.printf("%d) %s\n", ++i, invalidType);
        }
    }

    private <T> String getPatternNameQualifierOrClassName(EntityCheckPattern<T> cashboxCheckPattern) {
        Class<? extends EntityCheckPattern> aClass = cashboxCheckPattern.getClass();
        return aClass.getAnnotation(Qualifier.class) != null ?
                aClass.getAnnotation(Qualifier.class).value()
                : aClass.getName();
    }


    protected void validateCashbox(int pattern) throws NoSuchAlgorithmException {
        if (cashboxCheckPatterns.size() <= pattern) throw new NoSuchAlgorithmException("No such cashbox check pattern");
        EntityCheckPattern<Cashbox> cashboxCheckPattern = cashboxCheckPatterns.get(pattern);
        List<Cashbox> cashboxesFromDB = cashboxRepository.findAll();
        List<Cashbox> invalidEntity = chekFieldsAndGetInvalidEntity(cashboxesFromDB, pattern);
        if (!invalidEntity.isEmpty()) {
            List<Cashbox> validate = validate(invalidEntity, cashboxCheckPattern);
            cashboxRepository.saveAll(validate);
        }
    }

    protected void validateClient(int pattern) throws NoSuchAlgorithmException {
        if (clientCheckPatterns.size() <= pattern) throw new NoSuchAlgorithmException("No such cashbox check pattern");
        EntityCheckPattern<Client> clientCheckPattern = clientCheckPatterns.get(pattern);
        List<Client> clientFromDB = clientRepository.findAll();
        List<Client> invalidEntity = chekFieldsAndGetInvalidEntity(clientFromDB, pattern);
        if (!invalidEntity.isEmpty()) {
            System.out.println("INVALID");
            System.out.println(invalidEntity.size() + " SIZE");
            List<Client> validate = validate(invalidEntity, clientCheckPattern);
            clientRepository.saveAll(validate);
        }
    }

    protected <T> List<T> validate(List<T> invalidEntity,
                                   EntityCheckPattern<T> entityCheckPattern) {
        Scanner in = new Scanner(System.in);
        int countInvalidEntity = invalidEntity.size();
        int countWatchedEntity = 0;

        for (T entity : invalidEntity) {
            String invalidModelType = entityCheckPattern.getFieldType(entity);
            Integer entityId = entityCheckPattern.getEntityId(entity);
            countWatchedEntity++;

            if (isNeedToSkip(invalidModelType)) continue;
            if (isFamousType(invalidModelType)) {
                Integer change = this.famousInvalidTypes.get(invalidModelType);
                entityCheckPattern.fix(entity, entityCheckPattern.getValidTypes()[change]);
                continue;
            }


            System.out.printf(setColor("(%d/%d)", BLUE, YELLOW), countWatchedEntity, countInvalidEntity);
            System.out.printf("%d ID Entity have invalid type :%s\n",
                    entityId, setColor(invalidModelType, RED));
            System.out.println("Change valid type in list: ");

            int i = 0;
            for (String validType : entityCheckPattern.getValidTypes()) {
                System.out.printf("%d) %-20.20s", ++i, validType);
                if (i % 2 == 0) System.out.println();
            }

            System.out.printf("%d) I dont know what type needed. Skip it\n", ++i);

            int change = -1;
            int maxInvar = CashboxModel.values().length + 1;
            while (change <= 0 || change > maxInvar) {
                System.out.print("Enter her: ");
                change = in.nextInt();
            }
            if (change != maxInvar) {
                addFamousInvalidType(invalidModelType, change);
                entityCheckPattern.fix(entity, entityCheckPattern.getValidTypes()[change - 1]);
            } else {
                this.skippingTypes.add(invalidModelType);
                System.out.printf("%d ID Entity skipped\n", entityId);
            }
        }
        in.close();
        return invalidEntity;
    }

    private void addFamousInvalidType(String invalidEntityType, int change) {
        this.famousInvalidTypes.put(invalidEntityType, change - 1);
    }

    private boolean isFamousType(String invalidModelType) {
        for (String ft : this.famousInvalidTypes.keySet()) {
            if (invalidModelType.equals(ft)) {
                return true;
            }
        }
        return false;
    }

    private boolean isNeedToSkip(String invalidEntityType) {
        for (String st : this.skippingTypes) {
            if (invalidEntityType.equals(st)) {
                return true;
            }
        }
        return false;
    }

    private <T> List<T> chekFieldsAndGetInvalidEntity(List<T> entities, int index) {
        List<T> cashboxes = new ArrayList<>();
        T t = entities.get(0);
        if (t instanceof Cashbox) {
            for (T entity : entities) {
                if (!cashboxCheckPatterns.get(index).check((Cashbox) entity))
                    cashboxes.add(entity);
            }
        } else if (t instanceof Client) {
            for (T entity : entities) {
                if (!clientCheckPatterns.get(index).check((Client) entity))
                    cashboxes.add(entity);
            }
        }
        return cashboxes;
    }
}


