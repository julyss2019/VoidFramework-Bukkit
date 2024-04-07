import com.void01.bukkit.voidframework.common.Cooldown
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

class CooldownTest {
    @Test
    fun test() {
        val cooldown = Cooldown()

        cooldown.add(3500, TimeUnit.MILLISECONDS)
        assertEquals(3, cooldown.get(TimeUnit.SECONDS))
        assertEquals(false, cooldown.isFinished)

        Thread.sleep(4000)
        assertEquals(0, cooldown.get(TimeUnit.SECONDS))
        assertEquals(true, cooldown.isFinished)
    }
}