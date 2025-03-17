package com.jasonsjones.app;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.jasonsjones.app.config.Configuration;
import com.jasonsjones.app.config.ConfigurationManager;
import com.jasonsjones.app.core.SocketListener;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppTest {

    @Mock
    private ConfigurationManager configurationManager;

    @Mock
    private Configuration configuration;

    @Mock
    private SocketListener socketListener;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testMain() {
        // Mock the Configuration
        Configuration mockConfig = mock(Configuration.class);
        when(mockConfig.getPort()).thenReturn(8080);

        // Use Mockito's static mocking capability to mock the singleton
        try (MockedStatic<ConfigurationManager> mockedConfigManager = mockStatic(ConfigurationManager.class)) {
            
            // Setup the static mock to return our mocked instance
            mockedConfigManager.when(ConfigurationManager::getInstance).thenReturn(configurationManager);
            when(configurationManager.getCurrentConfiguration()).thenReturn(mockConfig);
            
            // Mock the SocketListener constructor using MockedConstruction
            try (MockedConstruction<SocketListener> mockedSocketListener = Mockito.mockConstruction(SocketListener.class, (mock, context) -> {
                doNothing().when(mock).start();
            })) {

                // Call the main method
                App.main(new String[]{});

                // Verify that getCurrentConfiguration was called
                verify(configurationManager).getCurrentConfiguration();

                // Verify that start was called on the socket listener
                verify(mockedSocketListener.constructed().get(0)).start();
            }
        }
    }
}
