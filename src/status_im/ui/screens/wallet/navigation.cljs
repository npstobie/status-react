(ns status-im.ui.screens.wallet.navigation
  (:require [re-frame.core :as re-frame]
            [status-im.ui.screens.db :as db]
            [status-im.ui.screens.navigation :as navigation]
            [status-im.utils.ethereum.core :as ethereum]
            [status-im.utils.ethereum.tokens :as tokens]))

(defmethod navigation/preload-data! :wallet
  [db _]
  (re-frame/dispatch [:update-wallet (map :symbol (tokens/tokens-for (ethereum/network->chain-keyword (:network db))))])
  (assoc-in db [:wallet :current-tab] 0))

(defmethod navigation/preload-data! :transactions-history
  [db _]
  (re-frame/dispatch [:update-transactions])
  db)

(defmethod navigation/preload-data! :wallet-request-transaction
  [db [event]]
  (if (= event :navigate-back)
    db
    (-> db
        (update :wallet dissoc :request-transaction)
        (assoc-in [:wallet :send-transaction] db/transaction-send-default))))

(defmethod navigation/preload-data! :wallet-send-transaction
  [db [event]]
  (if (= event :navigate-back)
    db
    (assoc-in db [:wallet :send-transaction] db/transaction-send-default)))
