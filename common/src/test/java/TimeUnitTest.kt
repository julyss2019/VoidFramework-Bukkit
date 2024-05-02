import com.void01.bukkit.voidframework.common.TimeUnit
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TimeUnitTest {
    @Test
    fun test() {
        assertEquals(5, TimeUnit.SECONDS.convert(5000, TimeUnit.MILLISECONDS))
        assertEquals(5000, TimeUnit.SECONDS.toMillis(5))
    }
}