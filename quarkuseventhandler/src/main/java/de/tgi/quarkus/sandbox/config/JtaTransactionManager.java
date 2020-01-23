package de.tgi.quarkus.sandbox.config;

import org.axonframework.common.transaction.Transaction;
import org.axonframework.common.transaction.TransactionManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class JtaTransactionManager implements TransactionManager {

    @Inject
    private javax.transaction.TransactionManager transactionManager;

    @Override
    public Transaction startTransaction() {
        try {

            System.out.println("Blub Status " + String.valueOf(transactionManager.getStatus()));
            if (transactionManager.getStatus() != 0) {
                transactionManager.begin();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JtaTransaction(transactionManager);
    }

    private class JtaTransaction implements Transaction {

        private javax.transaction.TransactionManager transactionManager;

        public JtaTransaction(javax.transaction.TransactionManager transactionManager) {
            this.transactionManager = transactionManager;
        }

        @Override
        public void commit() {
            try {
                transactionManager.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void rollback() {
            try {
                transactionManager.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
