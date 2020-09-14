package by.deathsmell.clientmanager.configuration;

import by.deathsmell.clientmanager.domain.Cashbox;
import by.deathsmell.clientmanager.dto.enums.CashboxModel;
import by.deathsmell.clientmanager.dto.enums.Master;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.Set;

@Configuration
public class CashboxesCheckPatterns {

    @Bean
    @Order(1)
    @Qualifier("CheckCashboxModelPattern")
    public EntityCheckPattern<Cashbox> getModelPattern() {
        return new EntityCheckPattern<>() {
            final CashboxModel[] validValues = CashboxModel.values();

            @Override
            public boolean check(Cashbox entity) {
                for (CashboxModel value : validValues) {
                    if (entity.getModel().equals(value.model())) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void fix(Cashbox entity, String correct) {
                entity.setModel(correct);
            }

            @Override
            public String getFieldType(Cashbox entity) {
                return entity.getModel();
            }

            @Override
            public Integer getEntityId(Cashbox entity) {
                return entity.getId();
            }

            @Override
            public String[] getValidTypes() {
                return Arrays.stream(validValues)
                        .map(CashboxModel::model)
                        .toArray(String[]::new);
            }

            @Override
            public void getInvalidType(Set<String> source, Cashbox entity) {
                if (!check(entity)) source.add(entity.getModel());
            }
        };
    }

    @Bean
    @Order(2)
    @Qualifier("CheckCashboxMasterNamePattern")
    public EntityCheckPattern<Cashbox> getMasterPattern() {
        return new EntityCheckPattern<>() {
            final Master[] validValues = Master.values();

            @Override
            public boolean check(Cashbox entity) {
                for (Master value : validValues) {
                    if (entity.getMaster().equals(value.getAlias())) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void fix(Cashbox entity, String correct) {
                entity.setMaster(correct);
            }

            @Override
            public String getFieldType(Cashbox entity) {
                return entity.getMaster();
            }

            @Override
            public Integer getEntityId(Cashbox entity) {
                return entity.getId();
            }

            @Override
            public String[] getValidTypes() {
                return Arrays.stream(validValues)
                        .map(Master::getAlias)
                        .toArray(String[]::new);
            }

            @Override
            public void getInvalidType(Set<String> source, Cashbox entity) {
                if (!check(entity)) source.add(entity.getMaster());
            }
        };
    }

}
