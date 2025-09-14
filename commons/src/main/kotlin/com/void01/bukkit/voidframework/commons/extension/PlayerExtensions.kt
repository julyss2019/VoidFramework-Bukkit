package com.void01.bukkit.voidframework.commons.extension

import com.github.julyss2019.bukkit.voidframework.thirdparty.VaultThirdParty
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.entity.Player

fun Player.setPlaceholders(text: String): String {
    return PlaceholderAPI.setPlaceholders(this, text)
}

fun Player.withdrawMoney(amount: Double) {
    VaultThirdParty.withdrawBalance(this, amount)
}

fun Player.depositMoney(amount: Double) {
    VaultThirdParty.depositBalance(this, amount)
}

fun Player.getMoney(): Double {
    return VaultThirdParty.getBalance(this)
}

fun Player.hasEnoughMoney(amount: Double) : Boolean {
    return VaultThirdParty.hasEnoughBalance(this, amount)
}