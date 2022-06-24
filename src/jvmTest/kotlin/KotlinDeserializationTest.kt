import org.ergoplatform.serialization.MosaikSerializers
import kotlin.test.Test
import kotlin.test.assertNotNull

class KotlinDeserializationTest {
    @Test
    fun testDeserialization() {
        val json = this.javaClass.getResource("/test_tree.json")!!.readText()
        val obj = MosaikSerializers.parseMosaikAppFromJson(json)

        assertNotNull(obj.manifest.appName)
    }
}