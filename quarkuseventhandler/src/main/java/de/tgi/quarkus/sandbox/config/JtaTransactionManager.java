package de.tgi.quarkus.sandbox.config;

import org.axonframework.common.transaction.Transaction;
import org.axonframework.common.transaction.TransactionManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

@ApplicationScoped
public class JtaTransactionManager implements TransactionManager {

    @Inject
    private UserTransaction transaction;

    @Override
    public Transaction startTransaction() {
        try {
            transaction.begin();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JtaTransaction(transaction);
    }

    private class JtaTransaction implements Transaction {

        private UserTransaction transaction;

        public JtaTransaction(UserTransaction transaction) {
            this.transaction = transaction;
        }

        @Override
        public void commit() {
            try {
                transaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void rollback() {
            try {
                transaction.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
