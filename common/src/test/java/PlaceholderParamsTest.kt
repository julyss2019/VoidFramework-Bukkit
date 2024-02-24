import com.void01.bukkit.voidframework.common.PlaceholderParamsParser
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PlaceholderParamsTest {
    @Test
    fun test() {
        val expression = """
            hello_\\a_\_
        """.trimIndent()
        assertEquals(listOf("a", "b", "c"), PlaceholderParamsParser.parse("a_b_c"))
        assertEquals(listOf("hello", "\\a", "_"), PlaceholderParamsParser.parse(expression))
    }
}