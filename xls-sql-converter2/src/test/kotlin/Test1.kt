import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path

class Test1 {


    @Test
    fun writeCommand() {
        val bytes = byteArrayOf(0x02) + "0010001Z".encodeToByteArray() + byteArrayOf(0x03)

        println(bytes.contentToString())

        Files.write(Path.of("/Users/dvalyaev/mega/ya/PTL/cmd/Z"), bytes)

    }
}