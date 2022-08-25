//package com.yapp.pet.global.config.routing;
//
//import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
//import org.springframework.transaction.support.TransactionSynchronizationManager;
//
//public class RoutingDataSource extends AbstractRoutingDataSource {
//
//    @Override
//    protected Object determineCurrentLookupKey() {
//        return TransactionSynchronizationManager.isCurrentTransactionReadOnly()
//                ? "secondary"
//                : "primary";
//    }
//}
