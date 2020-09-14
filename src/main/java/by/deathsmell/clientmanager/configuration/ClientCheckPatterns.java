package by.deathsmell.clientmanager.configuration;

import by.deathsmell.clientmanager.domain.Client;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Set;

@Configuration
public class ClientCheckPatterns {


    @Bean
    @Order(1)
    @Qualifier("Client_*_Pattern")
    public EntityCheckPattern<Client> getClientPattern(){
        return new EntityCheckPattern<Client>() {

            @Override
            public boolean check(Client entity) {
                return true;
            }

            @Override
            public void fix(Client entity, String correct) {
            }

            @Override
            public String getFieldType(Client entity) {
                return null;
            }

            @Override
            public Integer getEntityId(Client entity) {
                return null;
            }

            @Override
            public String[] getValidTypes() {
                return null;
            }

            @Override
            public void getInvalidType(Set<String> source, Client entity) {
                if (check(entity)) source.add(entity.getName());
            }
        };
    }




}
