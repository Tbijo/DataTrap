package com.example.datatrap.sync.navigation

sealed class SyncScreens(val route: String) {
    object SynchronizeScreen: SyncScreens("synchronize_screen")
}

//sealed class Screens(val route: String)  {
//
//    object AccountsScreen: Screens(route = "accounts_screen")
//
//    object TransactionsScreen: Screens(route = "transactions_screen/{accountIdKey}/{accountNameKey}/{accountBalanceKey}/{accountNumberKey}") {
//
//        const val accountIdKey = "accountIdKey"
//        const val accountNameKey = "accountNameKey"
//        const val accountBalanceKey = "accountBalanceKey"
//        const val accountNumberKey = "accountNumberKey"
//
//        fun passParams(accountIdVal: String, accountNameVal: String, accountBalanceVal: String, accountNumberVal: String): String {
//            return "transactions_screen/$accountIdVal/$accountNameVal/$accountBalanceVal/$accountNumberVal"
//        }
//    }
//
//    object TransactionScreen: Screens(route = "transaction_screen/{transactionIdKey}") {
//
//        const val transactionIdKey = "transactionIdKey"
//
//        fun passParams(transactionIdVal: String): String {
//            return "transaction_screen/$transactionIdVal"
//        }
//    }
//}