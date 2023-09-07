package com.github.julyss2019.bukkit.voidframework.thirdparty;

import com.github.julyss2019.bukkit.voidframework.common.Validator;
import lombok.NonNull;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;

public class VaultThirdParty {
    private static Economy vaultEconomy;

    public static void setVaultEconomy(@NonNull Economy vaultEconomy) {
        if (VaultThirdParty.vaultEconomy != null) {
            throw new UnsupportedOperationException();
        }

        VaultThirdParty.vaultEconomy = vaultEconomy;
    }

    private static void checkVaultEnabled() {
        Validator.checkState(vaultEconomy != null && vaultEconomy.isEnabled(), "Vault is not enabled");
    }

    /**
     * 增加余额
     *
     * @param player 玩家
     * @param value  值
     */
    public static void depositBalance(@NonNull OfflinePlayer player, double value) {
        checkVaultEnabled();
        vaultEconomy.depositPlayer(player, value);
    }

    /**
     * 扣除余额
     *
     * @param player 玩家
     * @param value  值
     */
    public static void withdrawBalance(@NonNull OfflinePlayer player, double value) {
        checkVaultEnabled();
        double current = getBalance(player);

        if (current - value < 0) {
            throw new RuntimeException(String.format("insufficient player balance, expected %f, actual %f", value, current));
        }

        vaultEconomy.withdrawPlayer(player, value);
    }

    /**
     * 检查余额是否充足
     *
     * @param player  玩家
     * @param balance 余额
     */
    public static boolean hasEnoughBalance(@NonNull OfflinePlayer player, double balance) {
        checkVaultEnabled();
        return getBalance(player) >= balance;
    }

    /**
     * 获取余额
     *
     * @param player 玩家
     */
    public static double getBalance(@NonNull OfflinePlayer player) {
        checkVaultEnabled();
        return vaultEconomy.getBalance(player);
    }
}
