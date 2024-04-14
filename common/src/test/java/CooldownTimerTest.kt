import com.void01.bukkit.voidframework.common.CooldownTimer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

class CooldownTimerTest {
    @Test
    fun test() {
        val cooldownTimer = CooldownTimer()

        cooldownTimer.addCooldown(3500, TimeUnit.MILLISECONDS)
        assertEquals(3, cooldownTimer.getCooldown(TimeUnit.SECONDS))
        assertEquals(false, cooldownTimer.isFinished)

        Thread.sleep(4000)
        assertEquals(0, cooldownTimer.getCooldown(TimeUnit.SECONDS))
        assertEquals(true, cooldownTimer.isFinished)
    }
}