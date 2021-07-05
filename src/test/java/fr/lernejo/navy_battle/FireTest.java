package fr.lernejo.navy_battle;


    import org.junit.jupiter.api.Assertions;
    import org.junit.jupiter.api.Test;

    import java.io.IOException;
    import java.net.URI;
    import java.net.http.HttpClient;
    import java.net.http.HttpRequest;
    import java.net.http.HttpResponse;

    import static org.junit.jupiter.api.Assertions.assertEquals;
    import static org.junit.jupiter.api.Assertions.assertThrows;

    import static org.junit.jupiter.api.Assertions.assertThrows;

class FireTest {


        @Test
        public void fromAPI() {
            assertEquals(SetFire.HIT, SetFire.fromAPI("hit"));
        }

        @Test
        public void fromAPIBad() {
            assertThrows(Exception.class, () -> SetFire.fromAPI("badvalue"));
        }
    }
