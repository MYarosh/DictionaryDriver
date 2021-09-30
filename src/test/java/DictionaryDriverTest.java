import org.junit.jupiter.api.*;
import ru.MYarosh.DictionaryDriver.DictionaryDriver;
import ru.MYarosh.DictionaryDriver.ServerConnectException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты модуля драйвера
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DictionaryDriverTest {

    private static DictionaryDriver dictionaryDriver;
    private static String address;
    private static int port;

    @BeforeAll
    static void setUp(){
        address = "localhost";
        port = 36000;
    }

    @Order(1)
    @Test
    void connectionTest(){
        try {
            dictionaryDriver = new DictionaryDriver(address, port);
        }catch (ServerConnectException e){
            fail("По заданным 'координатам' сервера не обнаружено!");
        }
    }

    @Order(2)
    @Test
    void loadTest() throws ServerConnectException {
        dictionaryDriver = new DictionaryDriver(address, port);
        System.out.println(dictionaryDriver.load(
                System.getProperty("os.name").toLowerCase().contains("win") ?
                        System.getProperty("user.dir") + "\\src\\main\\resources\\dump.txt" :
                        System.getProperty("user.dir") + "/src/main/resources/dump.txt"));
    }

    @Order(3)
    @Test
    void getTest() throws ServerConnectException {
        dictionaryDriver = new DictionaryDriver(address, port);
        assertNotNull(dictionaryDriver.get("A1"));
    }

    @Order(4)
    @Test
    void setTest() throws ServerConnectException {
        dictionaryDriver = new DictionaryDriver(address, port);
        assertNotNull(dictionaryDriver.set("A1","NewValue"));
    }

    @Order(4)
    @Test
    void removeTest() throws ServerConnectException {
        dictionaryDriver = new DictionaryDriver(address, port);
        assertNotNull(dictionaryDriver.remove("A1"));
    }
}
